package com.example.petpal.data.repository

import com.example.petpal.data.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val ordersCollection = firestore.collection("order")

    // Get active order for current user (status: "Pending" or "Accepted")
    suspend fun getActiveOrder(): Order? {
        return try {
            val userId = auth.currentUser?.uid ?: return null

            val snapshot = ordersCollection
                .whereEqualTo("owner_id", userId)
                .whereIn("status", listOf("Pending", "Accepted"))
                .limit(1)
                .get()
                .await()

            if (snapshot.documents.isNotEmpty()) {
                snapshot.documents.first().toObject(Order::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Create new order
    suspend fun createOrder(order: Order): Result<String> {
        return try {
            val docRef = ordersCollection.document()
            val orderWithId = order.copy(id = docRef.id)
            docRef.set(orderWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

