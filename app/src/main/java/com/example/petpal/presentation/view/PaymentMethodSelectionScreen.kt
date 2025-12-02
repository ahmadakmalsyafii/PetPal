package com.example.petpal.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

data class PaymentMethod(
    val id: String,
    val name: String,
    val logoRes: Int,
    val category: PaymentCategory
)

enum class PaymentCategory {
    TRANSFER_BANK,
    E_WALLET
}

@Composable
fun PaymentMethodSelectionScreen(
    onNavigateBack: () -> Unit,
    onPaymentMethodSelected: (PaymentMethod) -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    var isTransferBankExpanded by remember { mutableStateOf(false) }
    var isEWalletExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val transferBankMethods = listOf(
        PaymentMethod("bca", "BCA", R.drawable.logo_bca, PaymentCategory.TRANSFER_BANK),
        PaymentMethod("bni", "BNI", R.drawable.logo_bni, PaymentCategory.TRANSFER_BANK),
        PaymentMethod("bri", "BRI", R.drawable.logo_bri, PaymentCategory.TRANSFER_BANK),
        PaymentMethod("mandiri", "Mandiri", R.drawable.logo_mandiri, PaymentCategory.TRANSFER_BANK)
    )

    val eWalletMethods = listOf(
        PaymentMethod("dana", "DANA", R.drawable.logo_dana, PaymentCategory.E_WALLET),
        PaymentMethod("gopay", "GoPay", R.drawable.logo_gopay, PaymentCategory.E_WALLET),
        PaymentMethod("shopeepay", "ShopeePay", R.drawable.logo_shopeepay, PaymentCategory.E_WALLET)
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
                text = "Metode Pembayaran",
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
                .padding(top = 16.dp)
        ) {
            // Transfer Bank Section
            PaymentCategorySection(
                icon = R.drawable.icon_tier_foreground,
                title = "Transfer Bank",
                isExpanded = isTransferBankExpanded,
                onToggle = { isTransferBankExpanded = !isTransferBankExpanded }
            )

            AnimatedVisibility(
                visible = isTransferBankExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    transferBankMethods.forEach { method ->
                        PaymentMethodItem(
                            method = method,
                            isSelected = selectedPaymentMethod?.id == method.id,
                            onSelect = { selectedPaymentMethod = method }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // E-Wallet Section
            PaymentCategorySection(
                icon = R.drawable.icon_tier_foreground,
                title = "E-Wallet",
                isExpanded = isEWalletExpanded,
                onToggle = { isEWalletExpanded = !isEWalletExpanded }
            )

            AnimatedVisibility(
                visible = isEWalletExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    eWalletMethods.forEach { method ->
                        PaymentMethodItem(
                            method = method,
                            isSelected = selectedPaymentMethod?.id == method.id,
                            onSelect = { selectedPaymentMethod = method }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
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
            Button(
                onClick = {
                    selectedPaymentMethod?.let { onPaymentMethodSelected(it) }
                },
                enabled = selectedPaymentMethod != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PetPalDarkGreen,
                    disabledContainerColor = GrayText.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(
                    text = "Pilih Metode Pembayaran",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

@Composable
private fun PaymentCategorySection(
    icon: Int,
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
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
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(12.dp),
        color = White,
        shadowElevation = if (isSelected) 4.dp else 1.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) PetPalDarkGreen else GrayText.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = method.logoRes),
                contentDescription = method.name,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = method.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = BlackText,
                modifier = Modifier.weight(1f)
            )

            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PetPalDarkGreen,
                    unselectedColor = GrayText
                )
            )
        }
    }
}

