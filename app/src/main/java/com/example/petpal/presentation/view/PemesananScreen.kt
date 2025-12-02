package com.example.petpal.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.petpal.presentation.component.PetPalPrimaryButtonWithIcon
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.viewmodel.OrderViewModel
import com.example.petpal.utils.UiState

@Composable
fun PemesananScreen(
    viewModel: OrderViewModel = viewModel(),
    onNavigateToOrderForm: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
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
                .padding(16.dp),
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
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (val result = state) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Error -> {
                    Text(
                        text = result.message,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is UiState.Success -> {
                    if (result.data == null) {
                        // No active order - Show empty state
                        Column(
                            modifier = Modifier.fillMaxSize()
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
                        // Has active order - Show order details (will be implemented later)
                        Text("Ada pesanan aktif", color = BlackText)
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

