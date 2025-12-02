package com.example.petpal.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpal.R
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.theme.White

@Composable
fun BranchSelectionScreen(
    onNavigateBack: () -> Unit,
    onBranchSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_arrowleft_foreground),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Pilih Cabang",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp)
        ) {
            BranchCard(
                iconRes = R.drawable.icon_branchoption_foreground,
                title = "Cabang 1 : PetPal Prime",
                address = "Taman Borobudur 1 Blok E Nomor 9, Kelapa Dua, Tangerang",
                onClick = { onBranchSelected("Cabang 1 : PetPal Prime") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BranchCard(
                iconRes = R.drawable.icon_branchoption_foreground,
                title = "Cabang 2 : PetPal Next",
                address = "Jalan Sigura-Gura V Nomor 5, Lowokwaru, Malang",
                onClick = { onBranchSelected("Cabang 2 : PetPal Next") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BranchCard(
                iconRes = R.drawable.icon_branchoption_foreground,
                title = "Cabang 3 : PetPal Lux",
                address = "Jalan Camp Nou Nomor 6, Barcelona, Spain",
                onClick = { onBranchSelected("Cabang 3 : PetPal Lux") }
            )
        }
    }
}

@Composable
private fun BranchCard(
    iconRes: Int,
    title: String,
    address: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = White,
        shadowElevation = 2.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PetPalDarkGreen),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = address,
                    fontSize = 14.sp,
                    color = GrayText,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

