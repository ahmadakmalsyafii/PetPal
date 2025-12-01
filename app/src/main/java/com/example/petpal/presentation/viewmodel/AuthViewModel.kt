package com.example.petpal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.data.repository.AuthRepository
import com.example.petpal.utils.UiState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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
            } catch (e: FirebaseAuthInvalidUserException) {
                // Error jika email tidak terdaftar
                _authState.value = UiState.Error("Akun tidak ditemukan. Silakan daftar terlebih dahulu.")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                // Error jika password salah atau format email salah
                _authState.value = UiState.Error("Email atau Kata Sandi salah.")
            } catch (e: Exception) {
                // Error lainnya
                _authState.value = UiState.Error(e.message ?: "Login Gagal")
            }
        }
    }

    // Fungsi register tetap sama, atau bisa ditambahkan error handling serupa
    fun register(email: String, pass: String, name: String, phone: String) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                val user = repository.registerWithEmail(email, pass, name, phone)
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

    // Reset state agar error tidak muncul terus menerus jika navigasi balik
    fun resetState() {
        _authState.value = UiState.Success(repository.currentUser)
    }
}