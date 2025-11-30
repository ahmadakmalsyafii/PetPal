package com.example.petpal.data.repository

import com.example.petpal.data.model.Pet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetRepository {
    private val db = FirebaseFirestore.getInstance()
    private val petsCollection = db.collection("pets")

    // Contoh fungsi ambil data
    suspend fun getAllPets(): List<Pet> {
        return try {
            val snapshot = petsCollection.get().await()
            snapshot.toObjects(Pet::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Contoh fungsi tambah booking/pet
    suspend fun addPet(pet: Pet) {
        petsCollection.document(pet.id).set(pet).await()
    }
}