package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.localization.AppLanguage
import com.example.localization.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userProfile: UserProfile?,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    shareLink: String,
    onLogoutClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showLanguageDialog by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header Hero Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(vertical = 32.dp, horizontal = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(36.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = userProfile?.name ?: "Client Profile",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = userProfile?.email ?: "anonymous@cafe.com",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Settings Fields List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // User Details Expansion Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "My Identity",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        // Row Name
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Name:", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(userProfile?.name ?: "...")
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Row Email
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Email:", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(userProfile?.email ?: "...")
                        }
                        
                        if (!userProfile?.whatsapp.isNullOrEmpty()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            // Row WhatsApp
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("WhatsApp:", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(userProfile?.whatsapp ?: "")
                            }
                        }
                    }
                }

                // Controls: Language Selected
                Card(
                    onClick = { showLanguageDialog = true },
                    modifier = Modifier.fillMaxWidth().testTag("language_option"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(Localization.getString("language", currentLanguage), fontWeight = FontWeight.Bold) },
                        supportingContent = { Text(currentLanguage.displayName, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold) },
                        leadingContent = { Icon(Icons.Default.Translate, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = { Icon(Icons.Default.ArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                    )
                }

                // Day/Night Toggle Switch
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("theme_option"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(Localization.getString("day_night_toggle", currentLanguage), fontWeight = FontWeight.Bold) },
                        leadingContent = { Icon(imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = {
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = onThemeChange,
                                modifier = Modifier.testTag("theme_switch")
                            )
                        }
                    )
                }

                // Share App Card Option
                Card(
                    onClick = {
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(shareLink))
                        android.widget.Toast.makeText(context, Localization.getString("link_copied", currentLanguage), android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth().testTag("share_app_option"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(Localization.getString("share_app", currentLanguage), fontWeight = FontWeight.Bold) },
                        supportingContent = { Text(shareLink, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis) },
                        leadingContent = { Icon(Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("logout_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Exit icon")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Localization.getString("logout", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Language Chooser Dialog
        if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = { Text(Localization.getString("language", currentLanguage), fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppLanguage.values().forEach { lang ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onLanguageChange(lang)
                                        showLanguageDialog = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = lang.displayName,
                                    fontSize = 16.sp,
                                    fontWeight = if (currentLanguage == lang) FontWeight.Bold else FontWeight.Medium,
                                    color = if (currentLanguage == lang) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                if (currentLanguage == lang) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showLanguageDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
