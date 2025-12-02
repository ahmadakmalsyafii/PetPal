package com.example.petpal.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpal.data.model.Order
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.White
import com.example.petpal.utils.OrderStatusBadge

@Composable
fun OrderHome(
    orders: List<Order>,
    onClick: (Order) -> Unit = {}
) {
    if (orders.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Pesanan Terbaru",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BlackText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            orders.take(2).forEach { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(105.dp),
                    shape = CardDefaults.shape,
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = { onClick(order) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = order.service,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlackText
                        )

                        Text(
                            text = "Cabang: ${order.branch}",
                            fontSize = 13.sp,
                            color = GrayText
                        )

                        OrderStatusBadge(order.status)
                    }
                }
            }
        }
    }
}




