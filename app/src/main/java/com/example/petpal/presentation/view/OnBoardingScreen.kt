package com.example.petpal.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpal.R // Pastikan import R benar
import com.example.petpal.ui.theme.PetPalDarkGreen

@Composable
fun OnBoardingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Bagian Atas: Teks Selamat Datang
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Selamat Datang!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PetPalDarkGreen
            )
            Text(
                text = "Pilih cara masuk,",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Bagian Tengah: Logo (Ganti dengan logo PetPal kamu)
        // Pastikan file 'petpal_logo' ada di res/drawable
        // Gunakan ikon launcher sementara jika belum ada logo vektor
        Image(
            painter = painterResource(id = R.drawable.logo_petpal),
            contentDescription = "PetPal Logo",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "PetPal",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PetPalDarkGreen
        )

        // Bagian Bawah: Tombol
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PetPalDarkGreen),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(text = "Masuk", fontSize = 16.sp)
            }

            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PetPalDarkGreen),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(text = "Daftar", color = PetPalDarkGreen, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}