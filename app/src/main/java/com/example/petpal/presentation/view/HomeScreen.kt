package com.example.petpal.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petpal.presentation.component.PetCard
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.viewmodel.HomeViewModel
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.utils.UiState

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(), onLogout: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Header Sederhana
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Daftar Hewan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText
            )
        }


        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (val result = state) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text(text = result.message, color = Color.Red)
                is UiState.Success -> {
                    if (result.data.isEmpty()) {
                        Text(text = "Belum ada data hewan.", color = Color.Gray)
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(result.data) { pet ->
                                PetCard(pet = pet)
                            }
                        }
                    }
                }
            }
        }
    }
}