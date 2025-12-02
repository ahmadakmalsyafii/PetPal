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
import com.example.petpal.presentation.theme.White

@Composable
fun TierSelectionScreen(
    onNavigateBack: () -> Unit,
    onTierSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Header aligned with other screens
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close_foreground),
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Pilih Tingkat Layanan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            TierCard(
                iconRes = R.drawable.icon_vvip_foreground,
                title = "VVIP",
                description = "Layanan premium, jumlah update foto lebih banyak, kamar/area privat, pejemputan, live cam, makan, grooming.",
                onClick = { onTierSelected("VVIP") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TierCard(
                iconRes = R.drawable.icon_vip_foreground,
                title = "VIP",
                description = "Layanan premium, jumlah update foto lebih banyak, kamar/area privat, cam, grooming.",
                onClick = { onTierSelected("VIP") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TierCard(
                iconRes = R.drawable.icon_reguler_foreground,
                title = "Reguler",
                description = "Layanan dasar dengan fitur standar, live cam, makan.",
                onClick = { onTierSelected("Regular") }
            )
        }
    }
}

@Composable
private fun TierCard(
    iconRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = White,
        shadowElevation = 0.dp,
        border = ButtonDefaults.outlinedButtonBorder
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(GrayText.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = GrayText
                )
            }
        }
    }
}

