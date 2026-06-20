package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage

@Composable
fun PermissionBlockScreen(
    language: AppLanguage,
    onGrantRequest: () -> Unit,
    onExitRequest: () -> Unit
) {
    val context = LocalContext.current
    val surfaceColor = MaterialTheme.colorScheme.surface
    val mainPurple = Color(0xFF7E57C2) // Cozy elegant coffee/developer purple accent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Decorative clean background glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            mainPurple.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = surfaceColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Soft elegant pulsing icon container
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(mainPurple.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Notifications",
                        tint = mainPurple,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Text(
                    text = if (language == AppLanguage.BENGALI) "নোটিফিকেশন অনুমতি প্রয়োজন" else if (language == AppLanguage.HINDI) "नोटिफिकेशन अनुमति आवश्यक" else "Notification Permission",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (language == AppLanguage.BENGALI) 
                        "পূজা অনলাইন ক্যাফে অ্যাপের মাধ্যমে আমাদের থেকে ইনস্ট্যান্ট আপডেট, পোস্ট ও অর্ডার নোটিফিকেশন পেতে এর অনুমতি আবশ্যক। অনুগ্রহ করে অ্যাপটি ব্যবহার করতে অনুমতি প্রদান করুন।" 
                    else if (language == AppLanguage.HINDI) 
                        "पूजा ऑनलाइन कैफ़े ऐप के द्वारा तुरंत अपडेट, पोस्ट और आर्डर स्टेटस की जानकारी पाने के लिए नोटिफिकेशन चालू करना अनिवार्य है। कृपया आगे बढ़ने के लिए अनुमति दें।" 
                    else 
                        "Puja Online Cafe requires notification persistence to deliver instant real-time food updates, new cafe posts, and status changes even when the app is closed.\n\nWithout this, background synchronization cannot stay active. Please grant this permission to continue.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Grant Button
                Button(
                    onClick = onGrantRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainPurple
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_grant_permission")
                ) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "অনুমতি দিন" else if (language == AppLanguage.HINDI) "अनुमति प्रदान करें" else "Grant Notification Permission",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                // If permanently denied, assist to open Settings
                OutlinedButton(
                    onClick = {
                        try {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Fallback to system apps
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_open_settings"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = mainPurple
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(mainPurple.copy(alpha = 0.5f))
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.BENGALI) "অ্যাপ সেটিংস খুলুন" else if (language == AppLanguage.HINDI) "ऐप सेटिंग्स खोलें" else "Open App Settings",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                // Exit button
                TextButton(
                    onClick = onExitRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_exit_app")
                ) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "অ্যাপ থেকে বের হোন" else if (language == AppLanguage.HINDI) "ऐप बंद करें" else "Exit Application",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
