package com.example.petpal.presentation.view

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petpal.R
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.component.PetPalTextField
import com.example.petpal.presentation.viewmodel.AuthViewModel
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.utils.UiState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Google Sign In Setup
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { token ->
                    val credential = GoogleAuthProvider.getCredential(token, null)
                    viewModel.loginWithGoogleCredential(credential)
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(authState) {
        if (authState is UiState.Success && (authState as UiState.Success).data != null) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .windowInsetsPadding(WindowInsets.systemBars) // Aman dari navigasi HP
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // 1. Logo & Judul
        Image(
            painter = painterResource(id = R.drawable.logo_petpal),
            contentDescription = "PetPal Logo",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "PetPal",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PetPalDarkGreen
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 2. Input Email (Custom Component)
        PetPalTextField(
            label = "Email",
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Input Password (Custom Component)
        PetPalTextField(
            label = "Kata Sandi",
            value = password,
            onValueChange = { password = it },
            placeholder = "Kata Sandi",
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password",
                        tint = GrayText
                    )
                }
            }
        )


        Spacer(modifier = Modifier.height(32.dp))

        // 4. Tombol Masuk (Custom Component)
        if (authState is UiState.Loading) {
            CircularProgressIndicator(color = PetPalDarkGreen)
        } else {
            PetPalPrimaryButton(
                text = "Masuk",
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(email, password)
                    } else {
                        Toast.makeText(context, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            Text(
                text = "atau masuk dengan",
                fontSize = 12.sp,
                color = GrayText,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Google Sign In
        Surface(
            onClick = { googleLauncher.launch(googleSignInClient.signInIntent) },
            shape = CircleShape,
            shadowElevation = 4.dp,
            color = White,
            modifier = Modifier.size(50.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Ganti Text "G" dengan Image resource logo google jika ada
                Text("G", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 7. Footer
        val annotatedString = buildAnnotatedString {
            append("Belum punya akun? ")
            withStyle(style = SpanStyle(color = PetPalDarkGreen, fontWeight = FontWeight.Bold)) {
                append("Daftar")
            }
        }

        Text(
            text = annotatedString,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .clickable { onNavigateToRegister() },
            fontSize = 14.sp,
            color = GrayText
        )
    }
}