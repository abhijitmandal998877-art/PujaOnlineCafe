package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Post
import com.example.data.ShopDetails
import com.example.data.UserProfile
import com.example.localization.AppLanguage
import com.example.localization.Localization
import com.example.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: MainViewModel,
    shopDetails: ShopDetails,
    allProfiles: List<UserProfile>,
    posts: List<Post>,
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var adminTab by remember { mutableStateOf(0) } // 0 = Shop, 1 = Post, 2 = Customers

    // Shop Form States
    var formShopName by remember(shopDetails) { mutableStateOf(shopDetails.shopName) }
    var formOwnerName by remember(shopDetails) { mutableStateOf(shopDetails.ownerName) }
    var formLocation by remember(shopDetails) { mutableStateOf(shopDetails.shopLocation) }
    var formMobile by remember(shopDetails) { mutableStateOf(shopDetails.mobileNumber) }
    var formWhatsapp by remember(shopDetails) { mutableStateOf(shopDetails.whatsappNumber) }
    var formShareLink by remember(shopDetails) { mutableStateOf(shopDetails.shareLink) }

    // Post Form States
    var editingPostId by remember { mutableStateOf<String?>(null) }
    var formPostTitle by remember { mutableStateOf("") }
    var formShortDesc by remember { mutableStateOf("") }
    var formFullDesc by remember { mutableStateOf("") }
    var postTimestampRecord by remember { mutableStateOf(0L) }

    // Load customer data when tab becomes "Customers"
    LaunchedEffect(adminTab) {
        if (adminTab == 2) {
            viewModel.loadAllCustomersForAdmin()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.getString("admin_panel", language), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Admin Tabs row
            TabRow(selectedTabIndex = adminTab) {
                Tab(
                    selected = adminTab == 0,
                    onClick = { adminTab = 0 },
                    text = { Text(Localization.getString("shop", language), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = adminTab == 1,
                    onClick = { adminTab = 1 },
                    text = { Text(Localization.getString("publish_post", language), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = adminTab == 2,
                    onClick = { adminTab = 2 },
                    text = { Text(Localization.getString("manage_customers", language), fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }

            when (adminTab) {
                0 -> {
                    // Update Shop details Panel
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Update Shop Metadata",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        OutlinedTextField(
                            value = formShopName,
                            onValueChange = { formShopName = it },
                            label = { Text("Shop Name") },
                            leadingIcon = { Icon(Icons.Default.Store, null) },
                            modifier = Modifier.fillMaxWidth().testTag("admin_shop_name")
                        )

                        OutlinedTextField(
                            value = formOwnerName,
                            onValueChange = { formOwnerName = it },
                            label = { Text("Owner Name") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            modifier = Modifier.fillMaxWidth().testTag("admin_owner_name")
                        )

                        OutlinedTextField(
                            value = formLocation,
                            onValueChange = { formLocation = it },
                            label = { Text("Shop Location") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                            modifier = Modifier.fillMaxWidth().testTag("admin_shop_location")
                        )

                        OutlinedTextField(
                            value = formMobile,
                            onValueChange = { formMobile = it },
                            label = { Text("Mobile Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth().testTag("admin_mobile")
                        )

                        OutlinedTextField(
                            value = formWhatsapp,
                            onValueChange = { formWhatsapp = it },
                            label = { Text("WhatsApp Number") },
                            leadingIcon = { Icon(Icons.Default.Chat, null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth().testTag("admin_whatsapp")
                        )

                        OutlinedTextField(
                            value = formShareLink,
                            onValueChange = { formShareLink = it },
                            label = { Text("App Share Link") },
                            leadingIcon = { Icon(Icons.Default.Share, null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            modifier = Modifier.fillMaxWidth().testTag("admin_share_link")
                        )

                        Button(
                            onClick = {
                                viewModel.saveShopDetails(
                                    shopName = formShopName.trim(),
                                    ownerName = formOwnerName.trim(),
                                    shopLocation = formLocation.trim(),
                                    mobileNumber = formMobile.trim(),
                                    whatsappNumber = formWhatsapp.trim(),
                                    shareLink = formShareLink.trim(),
                                    onSuccess = {
                                        Toast.makeText(context, "Shop details updated!", Toast.LENGTH_SHORT).show()
                                    },
                                    onFailure = { err ->
                                        Toast.makeText(context, "Error: $err", Toast.LENGTH_LONG).show()
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("save_shop_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Localization.getString("save_changes", language), fontWeight = FontWeight.Bold)
                        }
                    }
                }
                1 -> {
                    // Create & Publish / Edit news posts panel
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = if (editingPostId != null) Localization.getString("edit_post", language)
                                   else Localization.getString("publish_post", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        OutlinedTextField(
                            value = formPostTitle,
                            onValueChange = { formPostTitle = it },
                            label = { Text(Localization.getString("post_title", language)) },
                            leadingIcon = { Icon(Icons.Default.Title, null) },
                            modifier = Modifier.fillMaxWidth().testTag("admin_post_title")
                        )

                        OutlinedTextField(
                            value = formShortDesc,
                            onValueChange = { formShortDesc = it },
                            label = { Text(Localization.getString("post_short_desc", language)) },
                            leadingIcon = { Icon(Icons.Default.Notes, null) },
                            modifier = Modifier.fillMaxWidth().testTag("admin_post_short"),
                            maxLines = 2
                        )

                        OutlinedTextField(
                            value = formFullDesc,
                            onValueChange = { formFullDesc = it },
                            label = { Text(Localization.getString("post_full_desc", language)) },
                            leadingIcon = { Icon(Icons.Default.Description, null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .testTag("admin_post_full"),
                            maxLines = 100
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    if (formPostTitle.isBlank() || formShortDesc.isBlank() || formFullDesc.isBlank()) {
                                        Toast.makeText(context, "Please fill in all post fields", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val currentId = editingPostId
                                    if (currentId != null) {
                                        // Edit mode
                                        viewModel.editPost(
                                            id = currentId,
                                            title = formPostTitle.trim(),
                                            shortDesc = formShortDesc.trim(),
                                            fullDesc = formFullDesc.trim(),
                                            timestamp = postTimestampRecord,
                                            onSuccess = {
                                                Toast.makeText(context, "Post updated successfully", Toast.LENGTH_SHORT).show()
                                                editingPostId = null
                                                formPostTitle = ""
                                                formShortDesc = ""
                                                formFullDesc = ""
                                            },
                                            onFailure = { err -> Toast.makeText(context, err, Toast.LENGTH_SHORT).show() }
                                        )
                                    } else {
                                        // Create mode
                                        viewModel.publishPost(
                                            title = formPostTitle.trim(),
                                            shortDesc = formShortDesc.trim(),
                                            fullDesc = formFullDesc.trim(),
                                            onSuccess = {
                                                Toast.makeText(context, Localization.getString("post_published", language), Toast.LENGTH_SHORT).show()
                                                formPostTitle = ""
                                                formShortDesc = ""
                                                formFullDesc = ""
                                            },
                                            onFailure = { err -> Toast.makeText(context, err, Toast.LENGTH_SHORT).show() }
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f).height(50.dp).testTag("publish_post_btn")
                            ) {
                                Text(
                                    text = if (editingPostId != null) "Update" else "Publish",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (editingPostId != null) {
                                OutlinedButton(
                                    onClick = {
                                        editingPostId = null
                                        formPostTitle = ""
                                        formShortDesc = ""
                                        formFullDesc = ""
                                    },
                                    modifier = Modifier.weight(1f).height(50.dp)
                                ) {
                                    Text("Cancel")
                                }
                            }
                        }

                        if (posts.isNotEmpty()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            Text("Click to Edit existing lines:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            
                            posts.forEach { post ->
                                Card(
                                    onClick = {
                                        editingPostId = post.id
                                        formPostTitle = post.title
                                        formShortDesc = post.shortDesc
                                        formFullDesc = post.fullDesc
                                        postTimestampRecord = post.timestamp
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("edit_post_shortcut_${post.id}")
                                ) {
                                    ListItem(
                                        headlineContent = { Text(post.title, fontWeight = FontWeight.Bold) },
                                        supportingContent = { Text(post.shortDesc) },
                                        trailingContent = {
                                            IconButton(
                                                onClick = {
                                                    viewModel.deletePost(
                                                        post.id,
                                                        onSuccess = { Toast.makeText(context, Localization.getString("post_deleted", language), Toast.LENGTH_SHORT).show() },
                                                        onFailure = { err -> Toast.makeText(context, err, Toast.LENGTH_SHORT).show() }
                                                    )
                                                }
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // View all registration profiles and manage dynamic Banning
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(allProfiles) { profile ->
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("customer_card_${profile.uid}"),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (profile.isBanned) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                                                     else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            ) {
                                ListItem(
                                    headlineContent = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(profile.name, fontWeight = FontWeight.Bold)
                                            if (profile.isAdmin) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                SuggestionChip(
                                                    onClick = {},
                                                    label = { Text("Admin") }
                                                )
                                            }
                                            if (profile.isBanned) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(MaterialTheme.colorScheme.error)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text("Banned", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    },
                                    supportingContent = {
                                        Column {
                                            Text("Email: ${profile.email}", fontSize = 12.sp)
                                            Text("WA: ${profile.whatsapp}", fontSize = 12.sp)
                                        }
                                    },
                                    leadingContent = {
                                        Icon(
                                            imageVector = Icons.Default.AccountBox,
                                            contentDescription = null,
                                            tint = if (profile.isBanned) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    },
                                    trailingContent = {
                                        if (!profile.isAdmin) {
                                            Button(
                                                onClick = {
                                                    viewModel.setCustomerBanStatus(
                                                        uid = profile.uid,
                                                        isBanned = !profile.isBanned,
                                                        onSuccess = {
                                                            Toast.makeText(
                                                                context,
                                                                if (profile.isBanned) Localization.getString("customer_unbanned", language)
                                                                else Localization.getString("customer_banned", language),
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        },
                                                        onFailure = { err -> Toast.makeText(context, err, Toast.LENGTH_SHORT).show() }
                                                    )
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (profile.isBanned) MaterialTheme.colorScheme.primary
                                                                     else MaterialTheme.colorScheme.error
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.testTag("ban_toggle_${profile.uid}")
                                            ) {
                                                Text(
                                                    text = if (profile.isBanned) Localization.getString("unban", language)
                                                           else Localization.getString("ban", language),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
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
