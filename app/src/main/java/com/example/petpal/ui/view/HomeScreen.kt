package com.example.petpal.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petpal.ui.component.PetCard
import com.example.petpal.ui.viewmodel.HomeViewModel
import com.example.petpal.utils.UiState

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val result = state) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text(text = result.message)
            is UiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(result.data) { pet ->
                        PetCard(pet = pet)
                    }
                }
            }
        }
    }
}