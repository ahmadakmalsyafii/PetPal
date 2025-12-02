package com.example.petpal.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petpal.R
import com.example.petpal.presentation.component.ServiceCard
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceSelectionBottomSheet(
    onDismiss: () -> Unit,
    onServiceSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            // Custom drag handle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            color = BlackText.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Title
            Text(
                text = "Pilih Jenis Layanan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Boarding Option
            ServiceCard(
                iconRes = R.drawable.icon_boarding_foreground,
                title = "Boarding",
                description = "Layanan penitipan menginap di tempat\npenitipan Pet Sitter",
                onClick = {
                    onServiceSelected("Boarding")
                    onDismiss()
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Daycare Option
            ServiceCard(
                iconRes = R.drawable.icon_daycare_foreground,
                title = "Daycare",
                description = "Layanan penitipan beberapa jam tanpa\nmenginap",
                onClick = {
                    onServiceSelected("Daycare")
                    onDismiss()
                }
            )
        }
    }
}

