package com.example.petpal.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Cek user yang sedang login
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    // Login Email & Password
    suspend fun loginWithEmail(email: String, pass: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            result.user
        } catch (e: Exception) {
            throw e // Lempar error ke ViewModel untuk ditangani
        }
    }

    // Register Email & Password
    suspend fun registerWithEmail(email: String, pass: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            result.user
        } catch (e: Exception) {
            throw e
        }
    }

    // Login Google (Menerima Credential dari Google Sign In UI)
    suspend fun signInWithGoogle(credential: AuthCredential): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user
        } catch (e: Exception) {
            throw e
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}