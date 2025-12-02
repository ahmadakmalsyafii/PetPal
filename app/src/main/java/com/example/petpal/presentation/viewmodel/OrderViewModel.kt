package com.example.petpal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.data.model.Order
import com.example.petpal.data.repository.OrderRepository
import com.example.petpal.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val repository = OrderRepository()

    private val _uiState = MutableStateFlow<UiState<Order?>>(UiState.Loading)
    val uiState: StateFlow<UiState<Order?>> = _uiState

    init {
        loadActiveOrder()
    }

    fun loadActiveOrder() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val order = repository.getActiveOrder()
                _uiState.value = UiState.Success(order)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun createOrder(order: Order, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.createOrder(order)
            result.fold(
                onSuccess = {
                    loadActiveOrder()
                    onSuccess()
                },
                onFailure = { onError(it.message ?: "Gagal membuat pesanan") }
            )
        }
    }
}

