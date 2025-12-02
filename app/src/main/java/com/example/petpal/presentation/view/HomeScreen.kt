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
import com.example.petpal.presentation.viewmodel.HomeViewModel
import com.example.petpal.presentation.component.OrderHome
import com.example.petpal.presentation.component.PetCard
import com.example.petpal.presentation.component.PetPalCarousel
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.White
import com.example.petpal.ui.components.PetTiles
import com.example.petpal.utils.UiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val petsState by viewModel.petsState.collectAsState()
    val orderState by viewModel.orderState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refreshAll() }
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {

            // ============================
            // HEADER (fixed section)
            // ============================
            item {
                Text(
                    text = "Home Page",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // ============================
            // CAROUSEL
            // ============================
            item {
                PetPalCarousel(
                    images = listOf(
                        "https://picsum.photos/600/400",
                        "https://picsum.photos/600/400",
                        "https://picsum.photos/600/400"
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            // ============================
            // ORDER HOME
            // ============================
            item {
                when (val result = orderState) {
                    is UiState.Success -> OrderHome(result.data.take(3))
                    else -> {}
                }
            }

            // ============================
            // PET TILES
            // ============================
            item {
                when (val result = petsState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(result.message, color = Color.Red)
                        }
                    }

                    is UiState.Success -> {
                        PetTiles(
                            pets = result.data,
                            onPetClick = {},
                            onAllPetsClick = {}
                        )
                    }
                }
            }

        }
    }
}





