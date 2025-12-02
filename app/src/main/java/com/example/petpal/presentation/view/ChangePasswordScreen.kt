package com.example.petpal.presentation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.component.PetPalTextField
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.viewmodel.AuthViewModel
import com.example.petpal.utils.UiState

@Composable
fun ChangePasswordScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val updateState by viewModel.updateState.collectAsState()
    val context = LocalContext.current

    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

    LaunchedEffect(updateState) {
        if (updateState is UiState.Success && (updateState as UiState.Success).data) {
            Toast.makeText(context, "Password Berhasil Diganti", Toast.LENGTH_SHORT).show()
            viewModel.resetUpdateState()
            onNavigateBack()
        }
        if (updateState is UiState.Error) {
            Toast.makeText(context, (updateState as UiState.Error).message, Toast.LENGTH_LONG).show()
            viewModel.resetUpdateState() // Reset agar bisa coba lagi
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = PetPalDarkGreen)
            }
            Text(
                text = "Edit Profil", // Sesuai UI gambarnya tulisannya Edit Profil meski isinya password
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = PetPalDarkGreen,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        PetPalTextField(
            label = "Password Lama",
            value = oldPass,
            onValueChange = { oldPass = it },
            placeholder = "Password Lama",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))

        PetPalTextField(
            label = "Password Baru",
            value = newPass,
            onValueChange = { newPass = it },
            placeholder = "Password Baru",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))

        PetPalTextField(
            label = "Konfirmasi Password Baru",
            value = confirmPass,
            onValueChange = { confirmPass = it },
            placeholder = "Konfirmasi Password Baru",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (updateState is UiState.Loading) {
            CircularProgressIndicator(color = PetPalDarkGreen)
        } else {
            PetPalPrimaryButton(
                text = "Simpan",
                onClick = {
                    if (newPass == confirmPass && oldPass.isNotEmpty()) {
                        viewModel.changePassword(oldPass, newPass)
                    } else {
                        Toast.makeText(context, "Password tidak cocok atau kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}