package com.example.petpal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.data.model.Order
import com.example.petpal.data.model.Pet
import com.example.petpal.data.model.Rating
import com.example.petpal.data.repository.OrderRepository
import com.example.petpal.data.repository.PetRepository
import com.example.petpal.data.repository.RatingRepository
import com.example.petpal.utils.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val petRepository = PetRepository()
    private val orderRepository = OrderRepository()
    private val ratingRepository = RatingRepository()



    // REFRESH STATE
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    //RETINGS STATE
    private val _ratingsState = MutableStateFlow<UiState<List<Rating>>>(UiState.Loading)
    val ratingsState: StateFlow<UiState<List<Rating>>> = _ratingsState

    // PET STATE
    private val _petsState = MutableStateFlow<UiState<List<Pet>>>(UiState.Loading)
    val petsState: StateFlow<UiState<List<Pet>>> = _petsState

    // ORDER STATE
    private val _orderState = MutableStateFlow<UiState<List<Order>>>(UiState.Loading)
    val orderState: StateFlow<UiState<List<Order>>> = _orderState

    init {
        refreshHomeData()
    }

    // ==========================================================
    // REFRESH HOME DATA (called on init & pull refresh)
    // ==========================================================
    private fun refreshHomeData() {
        fetchPets()
        fetchRatings()
        fetchRecentOrders()
    }

    fun refreshAll() {
        viewModelScope.launch {
            _isRefreshing.value = true

            refreshHomeData()

            delay(700) // smooth animation
            _isRefreshing.value = false
        }
    }

    // ==========================================================
    // FETCH PETS FROM FIREBASE
    // ==========================================================
    private fun fetchPets() {
        _petsState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val pets = petRepository.getUserPets()   // suspend fun
                _petsState.value = UiState.Success(pets)
            } catch (e: Exception) {
                _petsState.value = UiState.Error(
                    e.message ?: "Failed to load pets"
                )
            }
        }
    }

    fun fetchRatings() {
        _ratingsState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val ratings = ratingRepository.getAllRatings().getOrThrow() // throws exception if failed
                _ratingsState.value = UiState.Success(ratings)
            } catch (e: Exception) {
                _ratingsState.value = UiState.Error(
                    e.message ?: "Failed to load ratings"
                )
            }
        }
    }


    // ==========================================================
    // FETCH RECENT ORDERS (3 latest)
    // ==========================================================
    private fun fetchRecentOrders() {
        _orderState.value = UiState.Loading

        orderRepository.getRecentOrder {
            state ->
            when (state) {
                is UiState.Success -> {
                    _orderState.value = UiState.Success(
                        state.data.take(2)
                    )
                }

                is UiState.Error -> {
                    _orderState.value = UiState.Error(
                        state.message
                    )
                }

                is UiState.Loading -> {
                    _orderState.value = UiState.Loading
                }
            }
        }
    }
}
