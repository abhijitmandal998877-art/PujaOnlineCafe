package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.localization.AppLanguage
import com.example.localization.Localization
import com.example.main.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request notification permission at runtime for Android 13+ (Tiramisu)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher = registerForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
            ) { _ -> }
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Start background Firestore listening service for instant notifications even when closed
        try {
            val serviceIntent = android.content.Intent(this, com.example.service.NotificationService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Failed starting notification service", e)
        }

        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()

            // Observe Dark Mode state
            val isDarkTheme = viewModel.isDarkTheme
            // Observe Language state
            val currentLanguage = viewModel.currentLanguage

            MyApplicationTheme(darkTheme = isDarkTheme) {
                // Intro Loading Screen state
                var showIntroLoading by remember { mutableStateOf(true) }

                if (showIntroLoading) {
                    LoadingScreen(
                        language = currentLanguage,
                        onFinished = { showIntroLoading = false }
                    )
                } else {
                    val authStateLoading by viewModel.authStateLoading.collectAsState()
                    
                    if (authStateLoading) {
                        // Safe loader while checking auth profile
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        val userProfile by viewModel.currentUserProfile.collectAsState()

                        if (userProfile == null) {
                            // User needs credentials or sign up
                            AuthScreen(
                                viewModel = viewModel,
                                language = currentLanguage,
                                onAuthSuccess = {
                                    // Signed in! State updates automatically
                                }
                            )
                        } else {
                            val profile = userProfile!!
                            
                            // 1. If user is Banned, block access completely
                            if (profile.isBanned) {
                                BanScreen(
                                    language = currentLanguage,
                                    onLogout = { viewModel.logout() }
                                )
                            } else {
                                // 2. Regular app shell with 3 sections
                                var activeTab by remember { mutableStateOf(0) } // 0 = Dashboard, 1 = Contact, 2 = Settings
                                var showAdminArea by remember { mutableStateOf(false) }

                                val posts by viewModel.posts.collectAsState()
                                val shopDetails by viewModel.shopDetails.collectAsState()
                                val allProfiles by viewModel.allProfiles.collectAsState()
                                val activeNotification by viewModel.activeNotification.collectAsState()

                                if (showAdminArea && profile.isAdmin) {
                                    // View Admin Control Panel full overlay
                                    AdminScreen(
                                        viewModel = viewModel,
                                        shopDetails = shopDetails,
                                        allProfiles = allProfiles,
                                        posts = posts,
                                        language = currentLanguage,
                                        onBack = { showAdminArea = false }
                                    )
                                } else {
                                    Scaffold(
                                        modifier = Modifier.fillMaxSize(),
                                        bottomBar = {
                                            NavigationBar(
                                                modifier = Modifier
                                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                                    .testTag("bottom_nav_bar")
                                            ) {
                                                // Dashboard tab
                                                NavigationBarItem(
                                                    selected = activeTab == 0,
                                                    onClick = { activeTab = 0 },
                                                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                                    label = { Text(Localization.getString("dashboard", currentLanguage)) },
                                                    modifier = Modifier.testTag("tab_dashboard")
                                                )
                                                
                                                // Contact tab
                                                NavigationBarItem(
                                                    selected = activeTab == 1,
                                                    onClick = { activeTab = 1 },
                                                    icon = { Icon(Icons.Default.ContactPhone, contentDescription = "Contact") },
                                                    label = { Text(Localization.getString("contact", currentLanguage)) },
                                                    modifier = Modifier.testTag("tab_contact")
                                                )

                                                // Settings tab
                                                NavigationBarItem(
                                                    selected = activeTab == 2,
                                                    onClick = { activeTab = 2 },
                                                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                                    label = { Text(Localization.getString("settings", currentLanguage)) },
                                                    modifier = Modifier.testTag("tab_settings")
                                                )
                                            }
                                        }
                                    ) { innerPadding ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(innerPadding)
                                        ) {
                                            when (activeTab) {
                                                0 -> DashboardScreen(
                                                    posts = posts,
                                                    language = currentLanguage,
                                                    isAdmin = profile.isAdmin,
                                                    onAdminClick = { showAdminArea = true },
                                                    activeNotification = activeNotification,
                                                    onClearNotification = { viewModel.clearNotification() },
                                                    onEditPostRequested = { post ->
                                                        // Request edit redirection in admin
                                                        showAdminArea = true
                                                    },
                                                    onDeletePostRequested = { postId ->
                                                        viewModel.deletePost(postId, {}, {})
                                                    }
                                                )
                                                1 -> ContactScreen(
                                                    shopDetails = shopDetails,
                                                    language = currentLanguage
                                                )
                                                2 -> SettingsScreen(
                                                    userProfile = profile,
                                                    currentLanguage = currentLanguage,
                                                    onLanguageChange = { newLang ->
                                                        viewModel.currentLanguage = newLang
                                                    },
                                                    isDarkTheme = isDarkTheme,
                                                    onThemeChange = { isDark ->
                                                        viewModel.isDarkTheme = isDark
                                                    },
                                                    shareLink = shopDetails.shareLink,
                                                    onLogoutClick = {
                                                        viewModel.logout()
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
