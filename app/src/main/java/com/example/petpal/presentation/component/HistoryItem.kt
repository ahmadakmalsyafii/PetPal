package com.example.petpal.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpal.data.model.Order
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.White
import com.example.petpal.utils.OrderStatusBadge

@Composable
fun HistoryItem(order: Order) {

    // Warna badge berdasarkan tier
    val tierColor = when (order.tier) {
        "VVIP" -> Color.Black
        "VIP" -> Color(0xFFFFC107) // Kuning
        else -> Color(0xFF2196F3) // Biru (Regular)
    }

    Card(
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ==========================
            // LEFT SIDE (INFO)
            // ==========================
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = order.service,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackText
                )

                Text(
                    text = "Harga: Rp${order.price}",
                    fontSize = 14.sp,
                    color = BlackText
                )

                Text(
                    text = "Cabang: ${order.branch}",
                    fontSize = 14.sp,
                    color = GrayText
                )

                // STATUS BADGE
                Spacer(modifier = Modifier.height(8.dp))
                OrderStatusBadge(order.status)
            }

            // ==========================
            // RIGHT SIDE (TIER BADGE)
            // ==========================
            Box(
                modifier = Modifier
                    .background(
                        color = tierColor,
                        shape = CardDefaults.shape
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = order.tier ?: "Regular",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
