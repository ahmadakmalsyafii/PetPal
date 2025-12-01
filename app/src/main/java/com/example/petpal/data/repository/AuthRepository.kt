//package com.example.petpal.data.repository
//
//import com.google.firebase.auth.AuthCredential
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import kotlinx.coroutines.tasks.await
//
//class AuthRepository {
//    private val firebaseAuth = FirebaseAuth.getInstance()
//
//    // Cek user yang sedang login
//    val currentUser: FirebaseUser?
//        get() = firebaseAuth.currentUser
//
//    // Login Email & Password
//    suspend fun loginWithEmail(email: String, pass: String): FirebaseUser? {
//        return try {
//            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
//            result.user
//        } catch (e: Exception) {
//            throw e // Lempar error ke ViewModel untuk ditangani
//        }
//    }
//
//    // Register Email & Password
//    suspend fun registerWithEmail(email: String, pass: String): FirebaseUser? {
//        return try {
//            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
//            result.user
//        } catch (e: Exception) {
//            throw e
//        }
//    }
//
//    // Login Google (Menerima Credential dari Google Sign In UI)
//    suspend fun signInWithGoogle(credential: AuthCredential): FirebaseUser? {
//        return try {
//            val result = firebaseAuth.signInWithCredential(credential).await()
//            result.user
//        } catch (e: Exception) {
//            throw e
//        }
//    }
//
//    fun logout() {
//        firebaseAuth.signOut()
//    }
//}

package com.example.petpal.data.repository

import com.example.petpal.data.model.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    // Login
    suspend fun loginWithEmail(email: String, pass: String): FirebaseUser? {
        val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
        return result.user
    }

    // Register & Simpan Data User ke Firestore
    suspend fun registerWithEmail(email: String, pass: String, name: String, phone: String): FirebaseUser? {
        // 1. Buat Akun Auth
        val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
        val user = result.user

        // 2. Jika sukses, simpan detail ke Firestore
        user?.let {
            val newUser = User(
                uid = it.uid,
                email = email,
                name = name,
                phoneNumber = phone
            )
            usersCollection.document(it.uid).set(newUser).await()
        }
        return user
    }

    // Google Sign In
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