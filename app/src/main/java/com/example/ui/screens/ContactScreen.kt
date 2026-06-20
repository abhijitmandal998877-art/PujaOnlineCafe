package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShopDetails
import com.example.localization.AppLanguage
import com.example.localization.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    shopDetails: ShopDetails,
    language: AppLanguage
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
            // Header Image/Hero Banner Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ContactSupport,
                        contentDescription = "Contact Logo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Localization.getString("contact", language),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Cards content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Owner Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    ListItem(
                        headlineContent = { Text(Localization.getString("contact_owner", language), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                        supportingContent = { Text(shopDetails.ownerName, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface) },
                        leadingContent = { Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    )
                }

                // Shop Location
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    ListItem(
                        headlineContent = { Text(Localization.getString("contact_location", language), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                        supportingContent = { Text(shopDetails.shopLocation, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface) },
                        leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    )
                }

                // Mobile Call Action
                Card(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${shopDetails.mobileNumber}")
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not open dialer", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("call_owner_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    ListItem(
                        headlineContent = { Text(Localization.getString("call_now", language), fontWeight = FontWeight.Bold) },
                        supportingContent = { Text(shopDetails.mobileNumber, fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
                        leadingContent = { Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = { Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary) }
                    )
                }

                // WhatsApp Chat Action
                Card(
                    onClick = {
                        try {
                            // Trim non-digits for standard API path
                            val formattedNumber = shopDetails.whatsappNumber.replace("+", "").replace(" ", "")
                            val url = "https://api.whatsapp.com/send?phone=$formattedNumber"
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(url)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not launch WhatsApp", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("chat_whatsapp_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    ListItem(
                        headlineContent = { Text(Localization.getString("chat_on_whatsapp", language), fontWeight = FontWeight.Bold) },
                        supportingContent = { Text(shopDetails.whatsappNumber, fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
                        leadingContent = { Icon(Icons.Default.Chat, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
                        trailingContent = { Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary) }
                    )
                }
            }
        }
    }
}
