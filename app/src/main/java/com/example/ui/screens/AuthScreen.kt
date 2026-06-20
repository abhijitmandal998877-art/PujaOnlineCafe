package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.Localization
import com.example.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: MainViewModel,
    language: AppLanguage,
    onAuthSuccess: () -> Unit
) {
    var isLogin by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var showResetSuccessDialog by remember { mutableStateOf(false) }

    // Google Sign-In dialog mock selector
    var showGoogleSelector by remember { mutableStateOf(false) }
    var customGoogleEmail by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title Header Card (Centered)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Puja Logo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = Localization.getString("app_name", language),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "com.example.pujaonlinecafe".uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        letterSpacing = 1.5.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Auth Selector Switch (Tab Row Style)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { isLogin = true; errorMessage = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLogin) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (isLogin) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tab_login")
                ) {
                    Text(Localization.getString("login", language), fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { isLogin = false; errorMessage = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isLogin) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (!isLogin) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tab_signup")
                ) {
                    Text(Localization.getString("signup", language), fontWeight = FontWeight.SemiBold)
                }
            }

            // Messages
            errorMessage?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            successMessage?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Input Fields
            if (!isLogin) {
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(Localization.getString("name", language)) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("input_name"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Email or Login Identifier
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        if (isLogin) Localization.getString("whats_app_or_email", language)
                        else Localization.getString("email", language)
                    )
                },
                leadingIcon = {
                    Icon(
                        if (isLogin) Icons.Default.ContactMail else Icons.Default.Email,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .testTag("input_identifier"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp)
            )

            if (!isLogin) {
                // WhatsApp Number Field for SignUp
                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text(Localization.getString("whatsapp", language)) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("input_whatsapp"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(Localization.getString("password", language)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .testTag("input_password"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp)
            )

            // Forgot Password Hyperlink
            if (isLogin) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Forgot Password?",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable {
                                if (email.trim().isEmpty()) {
                                    errorMessage = "Please enter your email above before requesting reset link."
                                } else if (!email.trim().contains("@")) {
                                    errorMessage = "Please enter a valid email address."
                                } else {
                                    loading = true
                                    errorMessage = null
                                    successMessage = null
                                    viewModel.sendPasswordResetEmail(
                                        email = email.trim(),
                                        onSuccess = {
                                            loading = false
                                            showResetSuccessDialog = true
                                        },
                                        onFailure = { err ->
                                            loading = false
                                            errorMessage = err
                                        }
                                    )
                                }
                            }
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }

                if (showResetSuccessDialog) {
                    AlertDialog(
                        onDismissRequest = { showResetSuccessDialog = false },
                        title = { Text("Reset Email Sent! 📢") },
                        text = {
                            Text("A password reset link has been successfully issued to:\n\n${email.trim()}\n\n⚠️ IMPORTANT: Please check your SPAM / JUNK / UPDATES folder in Gmail or your mail app, as automatic Firebase password reset links often get filtered to spam folders!")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = { showResetSuccessDialog = false }
                            ) {
                                Text("I'll Check Spam Folder", fontWeight = FontWeight.Bold)
                            }
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Submit Button
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                Button(
                    onClick = {
                        loading = true
                        errorMessage = null
                        successMessage = null
                        if (isLogin) {
                            viewModel.loginWithEmailAndPasswordOrWhatsapp(
                                identifier = email.trim(),
                                password = password,
                                onSuccess = {
                                    loading = false
                                    onAuthSuccess()
                                },
                                onFailure = { err ->
                                    loading = false
                                    errorMessage = err
                                }
                            )
                        } else {
                            viewModel.signUpWithEmailAndPassword(
                                name = name.trim(),
                                email = email.trim(),
                                whatsapp = whatsapp.trim(),
                                password = password,
                                onSuccess = {
                                    loading = false
                                    successMessage = "Signup Successful! Logged in automatically."
                                    onAuthSuccess()
                                },
                                onFailure = { err ->
                                    loading = false
                                    errorMessage = err
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_submit"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isLogin) Localization.getString("login", language)
                               else Localization.getString("signup", language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Switch message link
                Text(
                    text = if (isLogin) Localization.getString("dont_have_account", language)
                           else Localization.getString("already_have_account", language),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            isLogin = !isLogin
                            errorMessage = null
                            successMessage = null
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}
