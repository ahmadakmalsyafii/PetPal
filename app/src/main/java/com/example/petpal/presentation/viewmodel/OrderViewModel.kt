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

    private val _createOrderState = MutableStateFlow<UiState<String>?>(null)
    val createOrderState: StateFlow<UiState<String>?> = _createOrderState

    private val _activeOrders = MutableStateFlow<UiState<List<Order>>>(UiState.Loading)
    val activeOrders: StateFlow<UiState<List<Order>>> = _activeOrders

    init {
        loadActiveOrder()
        loadActiveOrders()
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

    fun loadActiveOrders() {
        repository.getRecentOrder { state ->
            _activeOrders.value = state
        }
    }

    fun createOrder(order: Order, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _createOrderState.value = UiState.Loading
            val result = repository.createOrderSuspend(order)
            result.fold(
                onSuccess = { orderId ->
                    _createOrderState.value = UiState.Success(orderId)
                    loadActiveOrder()
                    loadActiveOrders()
                    onSuccess()
                },
                onFailure = { error ->
                    _createOrderState.value = UiState.Error(error.message ?: "Gagal membuat pesanan")
                    onError(error.message ?: "Gagal membuat pesanan")
                }
            )
        }
    }

    fun resetCreateOrderState() {
        _createOrderState.value = null
    }

    fun createOrderFromPayment(
        pets: List<com.example.petpal.data.model.Pet>,
        serviceType: String,
        startTime: String,
        startDate: String,
        endTime: String,
        endDate: String,
        tier: String,
        branch: String,
        notes: String,
        totalPrice: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _createOrderState.value = UiState.Loading

            try {
                // Convert dates and times to Timestamp
                val dateTimeFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                val startTimestamp = com.google.firebase.Timestamp(dateTimeFormat.parse("$startDate $startTime")!!)
                val endTimestamp = com.google.firebase.Timestamp(dateTimeFormat.parse("$endDate $endTime")!!)

                // Get first pet ID (for single pet orders) or comma-separated IDs (for multiple pets)
                val petId = pets.joinToString(",") { it.id }

                val order = Order(
                    petId = petId,
                    branch = branch,
                    service = serviceType,
                    startTime = startTimestamp,
                    endTime = endTimestamp,
                    notes = notes,
                    status = "Accepted",
                    price = totalPrice,
                    tier = tier,
                    ownerId = "" // Will be set by repository
                )

                val result = repository.createOrderSuspend(order)
                result.fold(
                    onSuccess = { orderId ->
                        _createOrderState.value = UiState.Success(orderId)
                        loadActiveOrder()
                        loadActiveOrders()
                        onSuccess()
                    },
                    onFailure = { error ->
                        _createOrderState.value = UiState.Error(error.message ?: "Gagal membuat pesanan")
                        onError(error.message ?: "Gagal membuat pesanan")
                    }
                )
            } catch (e: Exception) {
                _createOrderState.value = UiState.Error(e.message ?: "Gagal membuat pesanan")
                onError(e.message ?: "Gagal membuat pesanan")
            }
        }
    }
}

