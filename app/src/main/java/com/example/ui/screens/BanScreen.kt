package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Localization

@Composable
fun BanScreen(
    language: AppLanguage,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val contactPhone = "+917908833286"
    val contactEmail = "imm.abhijit@gmail.com"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = "Banned",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = Localization.getString("banned_title", language),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = Localization.getString("banned_message", language),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = Localization.getString("banned_contact", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Clickable dial phone card
                Card(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$contactPhone")
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not dial", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("ban_phone_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                    )
                ) {
                    ListItem(
                        headlineContent = { Text(contactPhone, fontWeight = FontWeight.Bold) },
                        leadingContent = { Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Clickable send email card
                Card(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$contactEmail")
                                putExtra(Intent.EXTRA_SUBJECT, "Puja Online Cafe App - Appeal")
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not open email client", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("ban_email_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                    )
                ) {
                    ListItem(
                        headlineContent = { Text(contactEmail, fontWeight = FontWeight.Bold) },
                        leadingContent = { Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Back to Login / Sign out button
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().testTag("ban_exit_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Exit")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Out", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
