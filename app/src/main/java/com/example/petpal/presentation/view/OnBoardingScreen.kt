package com.example.petpal.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpal.R
import com.example.petpal.presentation.component.PetPalOutlinedButton
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText

@Composable
fun OnBoardingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.systemBars), // Safe Area
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 1. Teks Atas
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 60.dp)
            ) {
                Text(
                    text = "Selamat Datang!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pilih cara masuk,",
                    fontSize = 16.sp,
                    color = GrayText,
                    textAlign = TextAlign.Center
                )
            }

            // 2. Logo Tengah
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo_petpal),
                    contentDescription = "Logo PetPal",
                    modifier = Modifier.size(160.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "PetPal",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PetPalDarkGreen
                )
            }

            // 3. Tombol Bawah
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PetPalPrimaryButton(
                    text = "Masuk",
                    onClick = onNavigateToLogin
                )

                PetPalOutlinedButton(
                    text = "Daftar",
                    onClick = onNavigateToRegister
                )
            }
        }
    }
}