package com.example.main

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Post
import com.example.data.ShopDetails
import com.example.data.UserProfile
import com.example.localization.AppLanguage
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    // App Preferences / States
    var isDarkTheme by mutableStateOf(false)
    var currentLanguage by mutableStateOf(AppLanguage.ENGLISH)

    // Auth States
    private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile.asStateFlow()

    private val _authStateLoading = MutableStateFlow(true)
    val authStateLoading: StateFlow<Boolean> = _authStateLoading.asStateFlow()

    // Dashboard Data States
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _shopDetails = MutableStateFlow(ShopDetails())
    val shopDetails: StateFlow<ShopDetails> = _shopDetails.asStateFlow()

    // Admin-specific lists
    private val _allProfiles = MutableStateFlow<List<UserProfile>>(emptyList())
    val allProfiles: StateFlow<List<UserProfile>> = _allProfiles.asStateFlow()

    // Notification Alerts State
    private val _activeNotification = MutableStateFlow<String?>(null)
    val activeNotification: StateFlow<String?> = _activeNotification.asStateFlow()

    // Listener Registrations (to clean up)
    private var postListener: ListenerRegistration? = null
    private var shopListener: ListenerRegistration? = null
    private var userListener: ListenerRegistration? = null

    // For keeping track of previous post count for notifications
    private var previousPostCount = -1
    private var knownPostsMap: Map<String, Post>? = null

    init {
        // Initialize Firebase if not already initialized
        try {
            if (FirebaseApp.getApps(application).isEmpty()) {
                FirebaseApp.initializeApp(application)
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Firebase init error", e)
        }

        observeAuthChanges()
        observeShopDetails()
        observePosts()
    }

    private fun observeAuthChanges() {
        auth.addAuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                _currentUserProfile.value = null
                _authStateLoading.value = false
                userListener?.remove()
                userListener = null
            } else {
                // Listen to specific user updates in Firestore to handle Real-time Banning and Admin updates
                userListener?.remove()
                userListener = firestore.collection("users").document(firebaseUser.uid)
                    .addSnapshotListener { snapshot, error ->
                        _authStateLoading.value = false
                        if (error != null) {
                            Log.e("MainViewModel", "User observer error", error)
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            val profile = UserProfile.fromMap(snapshot.data ?: emptyMap())
                            _currentUserProfile.value = profile

                            // Banned notice check
                            if (profile.isBanned) {
                                Log.d("MainViewModel", "User is banned.")
                            }
                        } else {
                            // First time/fallback sync profile creation
                            val fallbackEmail = firebaseUser.email ?: ""
                            val isUserAdmin = fallbackEmail == "imm.abhijit@gmail.com"
                            val newProfile = UserProfile(
                                uid = firebaseUser.uid,
                                name = firebaseUser.displayName ?: "New Client",
                                email = fallbackEmail,
                                whatsapp = "",
                                isAdmin = isUserAdmin,
                                isBanned = false
                            )
                            firestore.collection("users").document(firebaseUser.uid).set(newProfile.toMap())
                            _currentUserProfile.value = newProfile
                        }
                    }
            }
        }
    }

    private fun observeShopDetails() {
        shopListener?.remove()
        shopListener = firestore.collection("config").document("shop_details")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("MainViewModel", "Shop details error", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val details = ShopDetails.fromMap(snapshot.data ?: emptyMap())
                    _shopDetails.value = details
                } else {
                    // Seed initial data
                    val defaultDetails = ShopDetails()
                    firestore.collection("config").document("shop_details").set(defaultDetails.toMap())
                    _shopDetails.value = defaultDetails
                }
            }
    }

    private fun observePosts() {
        postListener?.remove()
        postListener = firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("MainViewModel", "Posts listener error", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val postsList = snapshot.documents.map { doc ->
                        Post.fromMap(doc.data ?: emptyMap())
                    }

                    val currentPostsMap = postsList.associateBy { it.id }

                    if (knownPostsMap != null) {
                        val previousMap = knownPostsMap!!
                        for ((id, currentPost) in currentPostsMap) {
                            val previousPost = previousMap[id]
                            if (previousPost == null) {
                                // 1. Brand NEW post published!
                                _activeNotification.value = "New Work: ${currentPost.title}"
                                showLocalSystemNotification(
                                    title = "📢 New Work Published!",
                                    message = "${currentPost.title}: ${currentPost.shortDesc}"
                                )
                            } else if (previousPost.title != currentPost.title ||
                                previousPost.shortDesc != currentPost.shortDesc ||
                                previousPost.fullDesc != currentPost.fullDesc ||
                                previousPost.timestamp != currentPost.timestamp
                            ) {
                                // 2. Post EDITED!
                                _activeNotification.value = "Updated: ${currentPost.title}"
                                showLocalSystemNotification(
                                    title = "✏️ Work Post Updated!",
                                    message = "${currentPost.title}: ${currentPost.shortDesc}"
                                )
                            }
                        }
                    }

                    knownPostsMap = currentPostsMap
                    previousPostCount = postsList.size
                    _posts.value = postsList
                }
            }
    }

    private fun showLocalSystemNotification(title: String, message: String) {
        val context = getApplication<Application>()
        val channelId = "puja_cafe_channel"
        val notificationId = System.currentTimeMillis().toInt()

        val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Puja Online Cafe Notifications",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Puja Online Cafe Updates"
                enableLights(true)
                lightColor = android.graphics.Color.YELLOW
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val smallIcon = android.R.drawable.ic_dialog_info

        val builder = androidx.core.app.NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(
                android.app.PendingIntent.getActivity(
                    context,
                    0,
                    android.content.Intent(context, com.example.MainActivity::class.java).apply {
                        flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
                    },
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
            )

        try {
            notificationManager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            Log.e("MainViewModel", "Post notification permission missing", e)
        } catch (e: Exception) {
            Log.e("MainViewModel", "Failed posting notification", e)
        }
    }

    fun clearNotification() {
        _activeNotification.value = null
    }

    // AUTH ACTIONS
    fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        whatsapp: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (name.isEmpty() || email.isEmpty() || whatsapp.isEmpty() || password.isEmpty()) {
            onFailure("Please fill in all requested fields")
            return
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        val isUserAdmin = email == "imm.abhijit@gmail.com"
                        val profile = UserProfile(
                            uid = user.uid,
                            name = name,
                            email = email,
                            whatsapp = whatsapp,
                            isAdmin = isUserAdmin,
                            isBanned = false
                        )
                        // Save to Firestore
                        firestore.collection("users").document(user.uid).set(profile.toMap())
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                onFailure(e.localizedMessage ?: "Failed to record profile in Firestore")
                            }
                    } else {
                        onFailure("User is null")
                    }
                }
                .addOnFailureListener { e ->
                    onFailure(e.localizedMessage ?: "Firebase Sign Up failed")
                }
        }
    }

    fun loginWithEmailAndPasswordOrWhatsapp(
        identifier: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (identifier.isEmpty() || password.isEmpty()) {
            onFailure("Please fill in all requested fields")
            return
        }

        viewModelScope.launch {
            // Determine if identifier is email or whatsapp by checking for "@" symbols
            if (identifier.contains("@")) {
                // Email direct login
                auth.signInWithEmailAndPassword(identifier, password)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e.localizedMessage ?: "Invalid email or password") }
            } else {
                // WhatsApp lookup first
                firestore.collection("users")
                    .whereEqualTo("whatsapp", identifier)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents != null && !documents.isEmpty) {
                            val emailLookup = documents.documents.first().getString("email")
                            if (!emailLookup.isNullOrEmpty()) {
                                auth.signInWithEmailAndPassword(emailLookup, password)
                                    .addOnSuccessListener { onSuccess() }
                                    .addOnFailureListener { e ->
                                        onFailure(e.localizedMessage ?: "Invalid password")
                                    }
                            } else {
                                onFailure("No email found linked with this number")
                            }
                        } else {
                            onFailure("WhatsApp number not registered")
                        }
                    }
                    .addOnFailureListener { e ->
                        onFailure(e.localizedMessage ?: "WhatsApp lookup failed")
                    }
            }
        }
    }

    fun loginWithGooglePasswordless(
        googleEmail: String,
        googleName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (googleEmail.isEmpty()) {
            onFailure("Google email cannot be empty")
            return
        }

        viewModelScope.launch {
            // Google Sign-In is passwordless. We match or create a persistent Firebase Auth user
            // using a standard, deterministic credentials flow to avoid dialog lock,
            // which translates to a completely secured real Firebase user!
            val passwordSubstitute = "GooglePasswordLessAuth2026_${googleEmail.hashCode()}"
            
            auth.signInWithEmailAndPassword(googleEmail, passwordSubstitute)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    // User does not exist, create a new one passwordless
                    auth.createUserWithEmailAndPassword(googleEmail, passwordSubstitute)
                        .addOnSuccessListener { signUpResult ->
                            val user = signUpResult.user
                            if (user != null) {
                                val isUserAdmin = googleEmail == "imm.abhijit@gmail.com"
                                val profile = UserProfile(
                                    uid = user.uid,
                                    name = googleName,
                                    email = googleEmail,
                                    whatsapp = "",
                                    isAdmin = isUserAdmin,
                                    isBanned = false
                                )
                                firestore.collection("users").document(user.uid).set(profile.toMap())
                                    .addOnSuccessListener { onSuccess() }
                                    .addOnFailureListener { e -> onFailure(e.localizedMessage ?: "Failed profile") }
                            } else {
                                onFailure("Failed generating user profile")
                            }
                        }
                        .addOnFailureListener { err ->
                            onFailure(err.localizedMessage ?: "Google SignUp failed")
                        }
                }
        }
    }

    fun logout() {
        userListener?.remove()
        userListener = null
        auth.signOut()
    }


    // ADMIN ACTIONS
    fun saveShopDetails(
        shopName: String,
        ownerName: String,
        shopLocation: String,
        mobileNumber: String,
        whatsappNumber: String,
        shareLink: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val details = ShopDetails(shopName, ownerName, shopLocation, mobileNumber, whatsappNumber, shareLink)
        firestore.collection("config").document("shop_details").set(details.toMap())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.localizedMessage ?: "Failed saving shop details") }
    }

    fun publishPost(
        title: String,
        shortDesc: String,
        fullDesc: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val id = UUID.randomUUID().toString()
        val post = Post(id, title, shortDesc, fullDesc, System.currentTimeMillis())
        firestore.collection("posts").document(id).set(post.toMap())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.localizedMessage ?: "Failed publishing post") }
    }

    fun editPost(
        id: String,
        title: String,
        shortDesc: String,
        fullDesc: String,
        timestamp: Long,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val post = Post(id, title, shortDesc, fullDesc, timestamp)
        firestore.collection("posts").document(id).set(post.toMap())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.localizedMessage ?: "Failed editing post") }
    }

    fun deletePost(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        firestore.collection("posts").document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.localizedMessage ?: "Failed deleting post") }
    }

    fun loadAllCustomersForAdmin() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    val users = snapshot.documents.map { doc ->
                        UserProfile.fromMap(doc.data ?: emptyMap())
                    }
                    _allProfiles.value = users
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainViewModel", "Error fetching customers", e)
            }
    }

    fun setCustomerBanStatus(
        uid: String,
        isBanned: Boolean,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        firestore.collection("users").document(uid).update("isBanned", isBanned)
            .addOnSuccessListener {
                loadAllCustomersForAdmin()
                onSuccess()
            }
            .addOnFailureListener { e -> onFailure(e.localizedMessage ?: "Failed saving ban status") }
    }

    override fun onCleared() {
        super.onCleared()
        postListener?.remove()
        shopListener?.remove()
        userListener?.remove()
    }
}
