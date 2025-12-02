package com.example.petpal.data.repository

import com.example.petpal.data.model.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val petsCollection = db.collection("pet")

    // Ambil semua hewan milik user yang sedang login
    suspend fun getUserPets(): List<Pet> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = petsCollection.whereEqualTo("ownerId", uid).get().await()
            snapshot.toObjects(Pet::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Tambah Hewan Baru
    suspend fun addPet(pet: Pet) {
        val uid = auth.currentUser?.uid ?: throw Exception("User belum login")
        val docRef = petsCollection.document() // Generate ID baru
        val newPet = pet.copy(id = docRef.id, ownerId = uid)
        docRef.set(newPet).await()
    }
}