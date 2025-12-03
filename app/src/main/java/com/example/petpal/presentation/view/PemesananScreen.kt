package com.example.petpal.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petpal.R
import com.example.petpal.data.model.Order
import com.example.petpal.presentation.component.PetPalPrimaryButtonWithIcon
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.viewmodel.OrderViewModel
import com.example.petpal.utils.UiState

@Composable
fun PemesananScreen(
    viewModel: OrderViewModel = viewModel(),
    onNavigateToOrderForm: (String) -> Unit = {},
    onNavigateToOrderDetail: (String) -> Unit = {}
) {
    val activeOrdersState by viewModel.activeOrders.collectAsState()
    var showServiceSelection by remember { mutableStateOf(false) }

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
                .padding(top = 8.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pemesanan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText
            )
        }

        // Content
        when (val result = activeOrdersState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = result.message,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            is UiState.Success -> {
                if (result.data.isEmpty()) {
                    // No active orders - Show empty state
                    Column(
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    ) {
                        // Content area (centered)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            NoActiveOrderContent()
                        }

                        // Button at bottom
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 16.dp)
                        ) {
                            PetPalPrimaryButtonWithIcon(
                                text = "Sewa Jasa Sitter",
                                iconRes = R.drawable.logo_petpal_white,
                                onClick = { showServiceSelection = true },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    // Has active orders - Show list
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(result.data) { order ->
                            ActiveOrderCard(
                                order = order,
                                onClick = { onNavigateToOrderDetail(order.id) }
                            )
                        }
                    }

                    // Button at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        PetPalPrimaryButtonWithIcon(
                            text = "Sewa Jasa Sitter",
                            iconRes = R.drawable.logo_petpal_white,
                            onClick = { showServiceSelection = true },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Show service selection bottom sheet
        if (showServiceSelection) {
            ServiceSelectionBottomSheet(
                onDismiss = { showServiceSelection = false },
                onServiceSelected = { serviceType ->
                    showServiceSelection = false
                    onNavigateToOrderForm(serviceType)
                }
            )
        }
    }
}

@Composable
private fun NoActiveOrderContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon/Illustration
        Image(
            painter = painterResource(id = R.drawable.icon_noactiveorder_foreground),
            contentDescription = "Tidak ada pesanan",
            modifier = Modifier
                .size(240.dp)
                .padding(bottom = 24.dp)
        )

        // Title
        Text(
            text = "Tidak Ada Pesanan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BlackText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Subtitle
        Text(
            text = "Untuk menampilkan pesanan, silahkan\nlakukan pemesanan dulu yaa",
            fontSize = 14.sp,
            color = GrayText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun ActiveOrderCard(
    order: Order,
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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row with service type and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PetPalDarkGreen
                ) {
                    Text(
                        text = order.service,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (order.status == "Accepted") PetPalDarkGreen else GrayText
                ) {
                    Text(
                        text = order.status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Order details
            OrderDetailItem("Cabang", order.branch)
            OrderDetailItem("Tingkat Layanan", order.tier)
            OrderDetailItem("Harga", "Rp ${formatOrderPrice(order.price)}")

            if (order.notes.isNotEmpty()) {
                OrderDetailItem("Catatan", order.notes)
            }
        }
    }
}

@Composable
private fun OrderDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = GrayText
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = BlackText
        )
    }
}

private fun formatOrderPrice(price: Double): String {
    val formatter = java.text.NumberFormat.getInstance(java.util.Locale.forLanguageTag("id-ID"))
    return formatter.format(price.toLong())
}
