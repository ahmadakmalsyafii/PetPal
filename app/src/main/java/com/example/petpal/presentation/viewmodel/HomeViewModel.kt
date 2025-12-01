package com.example.petpal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.data.model.Pet
import com.example.petpal.data.repository.PetRepository
import com.example.petpal.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = PetRepository()

    private val _uiState = MutableStateFlow<UiState<List<Pet>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Pet>>> = _uiState

    init {
        fetchPets()
    }

    private fun fetchPets() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val pets = repository.getAllPets()
                _uiState.value = UiState.Success(pets)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}