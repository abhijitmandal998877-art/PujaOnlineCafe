package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.Post
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class NotificationService : Service() {

    private var firestoreListener: ListenerRegistration? = null
    private val channelId = "puja_cafe_service_channel"
    private val notificationChannelId = "puja_cafe_channel"
    private var isFirstLoad = true

    override fun onCreate() {
        super.onCreate()
        Log.d("NotificationService", "Service created")
        startForegroundService()
        setupFirestoreListener()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotificationService", "Service started")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundService() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Cafe Syncer Service",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = "Optimizes live content updates and sync."
            }
            manager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val smallIcon = android.R.drawable.ic_dialog_info

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Puja Cafe Sync")
            .setContentText("Keeping content up-to-date")
            .setSmallIcon(smallIcon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setOngoing(true)
            .build()

        startForeground(999, notification)
    }

    private fun setupFirestoreListener() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            val firestore = FirebaseFirestore.getInstance()
            
            firestoreListener = firestore.collection("posts")
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        Log.e("NotificationService", "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        if (isFirstLoad) {
                            isFirstLoad = false
                            Log.d("NotificationService", "First load completed with ${snapshots.size()} posts")
                            return@addSnapshotListener
                        }

                        for (dc in snapshots.documentChanges) {
                            val post = Post.fromMap(dc.document.data)
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    triggerLocalSystemNotification(
                                        title = "📢 " + post.title,
                                        message = post.shortDesc
                                    )
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    triggerLocalSystemNotification(
                                        title = "✏️ Update: " + post.title,
                                        message = post.shortDesc
                                    )
                                }
                                DocumentChange.Type.REMOVED -> {
                                    // Soft-ignore removals
                                }
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e("NotificationService", "Error setting up listener", e)
        }
    }

    private fun triggerLocalSystemNotification(title: String, message: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mainChannelId = notificationChannelId
        val notificationId = System.currentTimeMillis().toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                mainChannelId,
                "Puja Online Cafe Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Puja Online Cafe Updates"
                enableLights(true)
                lightColor = android.graphics.Color.YELLOW
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val smallIcon = android.R.drawable.ic_dialog_info

        val notification = NotificationCompat.Builder(this, mainChannelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            manager.notify(notificationId, notification)
        } catch (e: SecurityException) {
            Log.e("NotificationService", "Post notification permission missing", e)
        } catch (e: Exception) {
            Log.e("NotificationService", "Failed posting notification", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firestoreListener?.remove()
        Log.d("NotificationService", "Service destroyed")
    }
}
