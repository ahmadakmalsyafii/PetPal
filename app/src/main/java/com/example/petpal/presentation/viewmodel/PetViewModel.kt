package com.example.petpal.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.data.model.Pet
import com.example.petpal.data.repository.PetRepository
import com.example.petpal.utils.CloudinaryHelper
import com.example.petpal.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PetViewModel : ViewModel() {
    private val repository = PetRepository()

    // State untuk List Hewan
    private val _petsState = MutableStateFlow<UiState<List<Pet>>>(UiState.Loading)
    val petsState: StateFlow<UiState<List<Pet>>> = _petsState

    // State untuk Proses Tambah Hewan
    private val _addPetState = MutableStateFlow<UiState<Boolean>>(UiState.Success(false))
    val addPetState: StateFlow<UiState<Boolean>> = _addPetState

    fun loadUserPets() {
        viewModelScope.launch {
            _petsState.value = UiState.Loading
            try {
                val pets = repository.getUserPets()
                _petsState.value = UiState.Success(pets)
            } catch (e: Exception) {
                _petsState.value = UiState.Error(e.message ?: "Gagal memuat data hewan")
            }
        }
    }

    fun addPet(
        name: String,
        type: String,
        age: String,
        gender: String,
        notes: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _addPetState.value = UiState.Loading
            try {
                // 1. Upload Foto jika ada
                val photoUrl = if (imageUri != null) {
                    CloudinaryHelper.uploadImage(imageUri)
                } else {
                    ""
                }

                // 2. Buat objek Pet
                val newPet = Pet(
                    name = name,
                    type = type,
                    age = age,
                    gender = gender,
                    notes = notes,
                    photoUrl = photoUrl
                )

                // 3. Simpan ke Firestore
                repository.addPet(newPet)

                _addPetState.value = UiState.Success(true)
                loadUserPets() // Refresh list setelah menambah
            } catch (e: Exception) {
                _addPetState.value = UiState.Error(e.message ?: "Gagal menambah hewan")
            }
        }
    }

    fun resetAddState() {
        _addPetState.value = UiState.Success(false)
    }
}