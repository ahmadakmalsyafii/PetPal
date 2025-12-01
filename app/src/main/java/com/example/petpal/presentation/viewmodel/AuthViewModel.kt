package com.example.petpal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.data.repository.AuthRepository
import com.example.petpal.utils.UiState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<UiState<FirebaseUser?>>(UiState.Success(repository.currentUser))
    val authState: StateFlow<UiState<FirebaseUser?>> = _authState

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                val user = repository.loginWithEmail(email, pass)
                _authState.value = UiState.Success(user)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Login Gagal")
            }
        }
    }

    fun register(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                val user = repository.registerWithEmail(email, pass)
                _authState.value = UiState.Success(user)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Register Gagal")
            }
        }
    }

    fun loginWithGoogleCredential(credential: AuthCredential) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                val user = repository.signInWithGoogle(credential)
                _authState.value = UiState.Success(user)
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "Google Sign In Gagal")
            }
        }
    }
}