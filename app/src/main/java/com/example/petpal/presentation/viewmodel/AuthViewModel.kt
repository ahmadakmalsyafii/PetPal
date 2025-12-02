package com.example.petpal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.data.model.User
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

    private val _userData = MutableStateFlow<UiState<User?>>(UiState.Loading)
    val userData: StateFlow<UiState<User?>> = _userData

    private val _updateState = MutableStateFlow<UiState<Boolean>>(UiState.Success(false))
    val updateState: StateFlow<UiState<Boolean>> = _updateState

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

    fun loadCurrentUser() {
        val uid = repository.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                _userData.value = UiState.Loading
                try {
                    val user = repository.getUserData(uid)
                    _userData.value = UiState.Success(user)
                } catch (e: Exception) {
                    _userData.value = UiState.Error(e.message ?: "Gagal memuat profil")
                }
            }
        }
    }

    fun updateProfile(name: String, phone: String, location: String) {
        val uid = repository.currentUser?.uid ?: return
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                repository.updateUserProfile(uid, name, phone, location)
                _updateState.value = UiState.Success(true)
                loadCurrentUser() // Refresh data
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message ?: "Gagal update profil")
            }
        }
    }

    fun changePassword(oldPass: String, newPass: String) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                repository.changePassword(oldPass, newPass)
                _updateState.value = UiState.Success(true)
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message ?: "Gagal ganti password")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UiState.Success(false)
    }

    fun logout() {
        repository.logout()
    }

    fun resetState() {
        _authState.value = UiState.Success(repository.currentUser)
    }
}