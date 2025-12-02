package com.example.petpal.data.repository

import com.example.petpal.data.model.Rating
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RatingRepository {

    private val db = FirebaseFirestore.getInstance()
    private val ratingCollection = db.collection("ratings")

    // Add a new rating
    suspend fun addRating(rating: Rating): Result<Void?> {
        return try {
            ratingCollection.add(rating).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get all ratings
    suspend fun getAllRatings(): Result<List<Rating>> {
        return try {
            val snapshot = ratingCollection.get().await()
            val ratings = snapshot.toObjects(Rating::class.java)
            Result.success(ratings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
