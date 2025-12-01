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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextAlign
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

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // Setup Google Sign In
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
                errorMessage = "Google Sign In Error: ${e.message}"
            }
        }
    }

    // Observer Auth State
    LaunchedEffect(authState) {
        when (val state = authState) {
            is UiState.Success -> {
                if (state.data != null) {
                    errorMessage = null
                    onLoginSuccess()
                }
            }
            is UiState.Error -> {
                errorMessage = state.message // Tangkap pesan error dari ViewModel
            }
            is UiState.Loading -> {
                errorMessage = null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .windowInsetsPadding(WindowInsets.systemBars)
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

        Spacer(modifier = Modifier.height(32.dp))

        errorMessage?.let { msg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = msg,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 2. Input Email
        PetPalTextField(
            label = "Email",
            value = email,
            onValueChange = {
                email = it
                errorMessage = null // Hapus error saat user mengetik ulang
            },
            placeholder = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Input Password
        PetPalTextField(
            label = "Kata Sandi",
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
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


        Spacer(modifier = Modifier.height(24.dp))

        // 4. Tombol Masuk
        if (authState is UiState.Loading) {
            CircularProgressIndicator(color = PetPalDarkGreen)
        } else {
            PetPalPrimaryButton(
                text = "Masuk",
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        errorMessage = "Mohon isi email dan kata sandi"
                    } else {
                        viewModel.login(email, password)
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
            onClick = {
                googleSignInClient.signOut().addOnCompleteListener {
                    googleLauncher.launch(googleSignInClient.signInIntent)
                }
            },
            shape = RoundedCornerShape(32.dp),
            shadowElevation = 4.dp,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Sign in with Google",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PetPalDarkGreen
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.logo_google),
                        contentDescription = "Logo Google",
                        modifier = Modifier.size(24.dp)
                    )
                }
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