package com.example.petpal.presentation.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpal.R
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.viewmodel.OrderViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PaymentScreen(
    serviceType: String,
    petNames: String,
    petImageUrl: String? = null,
    startTime: String,
    startDate: String,
    endTime: String,
    endDate: String,
    tier: String,
    branch: String,
    durationHours: Int,
    tierPrice: Double,
    totalPrice: Double,
    selectedPaymentMethod: String? = null,
    onNavigateBack: () -> Unit,
    onNavigateToPaymentMethod: () -> Unit,
    onConfirmPayment: (com.example.petpal.presentation.viewmodel.OrderViewModel) -> Unit
) {
    val orderViewModel: OrderViewModel = viewModel()
    var isPriceExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val iconRotation by animateFloatAsState(
        targetValue = if (isPriceExpanded) 0f else 180f,
        animationSpec = tween(durationMillis = 300),
        label = "icon rotation"
    )

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
                text = "Pembayaran",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            // Order Card Section
            Text(
                text = "Pesanan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.padding(bottom = 12.dp, top = 16.dp)
            )

            OrderCard(
                serviceType = serviceType,
                petNames = petNames,
                petImageUrl = petImageUrl,
                startTime = startTime,
                startDate = startDate,
                endTime = endTime,
                endDate = endDate,
                tier = tier,
                branch = branch
            )

            // Payment Method Section
            Text(
                text = "Metode Pembayaran",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.padding(bottom = 12.dp, top = 24.dp)
            )

            PaymentMethodSelector(
                selectedMethod = selectedPaymentMethod,
                onClick = onNavigateToPaymentMethod
            )

            // Price Detail Section (Expandable)
            Text(
                text = "Rincian Harga",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.padding(bottom = 12.dp, top = 24.dp)
            )

            // Total Price Header (Clickable)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isPriceExpanded = !isPriceExpanded },
                shape = RoundedCornerShape(12.dp),
                color = GrayText.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Harga : Rp ${formatPrice(totalPrice)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackText,
                        modifier = Modifier.weight(1f)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.icon_pricedetail_foreground),
                        contentDescription = "Price detail",
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(iconRotation)
                    )
                }
            }

            // Expanded Price Details
            if (isPriceExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    PriceDetailRow(
                        label = "Durasi",
                        value = "$durationHours jam"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PriceDetailRow(
                        label = "Harga per jam",
                        value = "Rp ${formatPrice(tierPrice)}"
                    )

                    HorizontalDivider(
                        color = GrayText.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    PriceDetailRow(
                        label = "Total",
                        value = "Rp ${formatPrice(totalPrice)}",
                        isBold = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Bottom Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp, top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (selectedPaymentMethod != null) PetPalDarkGreen else GrayText.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .clickable(enabled = selectedPaymentMethod != null) {
                        if (selectedPaymentMethod != null) {
                            onConfirmPayment(orderViewModel)
                        }
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_payment_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Buat Pesanan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

@Composable
private fun OrderCard(
    serviceType: String,
    petNames: String,
    petImageUrl: String?,
    startTime: String,
    startDate: String,
    endTime: String,
    endDate: String,
    tier: String,
    branch: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = White,
        shadowElevation = 2.dp,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Pet Info Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Pet Image
                if (petImageUrl != null) {
                    AsyncImage(
                        model = petImageUrl,
                        contentDescription = "Pet Image",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(GrayText.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_petoption_foreground),
                            contentDescription = "Pet",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = petNames,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackText
                    )
                }

                // Service Type Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PetPalDarkGreen
                ) {
                    Text(
                        text = serviceType,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(
                color = GrayText.copy(alpha = 0.3f),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Order Details
            OrderDetailRow(
                icon = R.drawable.icon_calendar_foreground,
                label = "Durasi",
                value = if (serviceType == "Boarding") {
                    "$startTime $startDate - $endTime $endDate"
                } else {
                    "$startTime - $endTime, $startDate"
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OrderDetailRow(
                icon = R.drawable.icon_tier_foreground,
                label = "Tingkat Layanan",
                value = tier
            )

            Spacer(modifier = Modifier.height(8.dp))

            OrderDetailRow(
                icon = R.drawable.icon_branch_foreground,
                label = "Cabang",
                value = branch
            )
        }
    }
}

@Composable
private fun OrderDetailRow(
    icon: Int,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
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
}

@Composable
private fun PaymentMethodSelector(
    selectedMethod: String?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = PetPalDarkGreen
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_tier_foreground),
                    contentDescription = "Payment Method",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = selectedMethod ?: "Pilih Metode Pembayaran",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Arrow",
                tint = White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun PriceDetailRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = BlackText
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = BlackText
        )
    }
}

private fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"))
    return formatter.format(price.toLong())
}
