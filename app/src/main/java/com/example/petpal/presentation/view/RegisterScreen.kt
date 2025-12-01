package com.example.petpal.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.utils.UiState

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is UiState.Success && (authState as UiState.Success).data != null) {
            Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
        }
        if (authState is UiState.Error) {
            Toast.makeText(context, (authState as UiState.Error).message, Toast.LENGTH_LONG).show()
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
        Spacer(modifier = Modifier.height(40.dp))

        // 1. Logo Kecil
        Image(
            painter = painterResource(id = R.drawable.logo_petpal),
            contentDescription = "PetPal Logo",
            modifier = Modifier.size(60.dp)
        )
        Text(
            text = "PetPal",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PetPalDarkGreen
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Judul
        Text(
            text = "Daftar",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = BlackText,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Form Input
        PetPalTextField(
            label = "Email",
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        PetPalTextField(
            label = "No. Telepon",
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = "085",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        PetPalTextField(
            label = "Nama",
            value = name,
            onValueChange = { name = it },
            placeholder = "Username",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(8.dp))

        PetPalTextField(
            label = "Kata Sandi",
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Tombol Daftar
        if (authState is UiState.Loading) {
            CircularProgressIndicator(color = PetPalDarkGreen)
        } else {
            PetPalPrimaryButton(
                text = "Daftar",
                onClick = {
                    if (email.isNotEmpty() && phoneNumber.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.register(email, password, name, phoneNumber)
                    } else {
                        Toast.makeText(context, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 5. Footer
        val annotatedString = buildAnnotatedString {
            append("Sudah Punya akun? ")
            withStyle(style = SpanStyle(color = BlackText, fontWeight = FontWeight.Bold)) {
                append("Masuk")
            }
        }

        Text(
            text = annotatedString,
            modifier = Modifier
                .padding(vertical = 32.dp)
                .clickable { onNavigateToLogin() },
            fontSize = 14.sp,
            color = GrayText
        )
    }
}