package com.example.petpal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.data.model.Order
import com.example.petpal.data.repository.OrderRepository
import com.example.petpal.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val orderRepository = OrderRepository()

    private val _orderState = MutableStateFlow<UiState<List<Order>>>(UiState.Loading)
    val orderState: StateFlow<UiState<List<Order>>> = _orderState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        fetchHistory()
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            fetchHistory()
            _isRefreshing.value = false
        }
    }

    private fun fetchHistory() {
        _orderState.value = UiState.Loading

        orderRepository.getHistory { state ->
            when (state) {

                is UiState.Success -> {
                    _orderState.value = UiState.Success(
                        state.data // tampilkan semua riwayat
                    )
                }

                is UiState.Error -> {
                    _orderState.value = UiState.Error(state.message)
                }

                is UiState.Loading -> Unit
            }
        }
    }
}
