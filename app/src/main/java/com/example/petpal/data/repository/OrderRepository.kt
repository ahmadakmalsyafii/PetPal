package com.example.petpal.data.repository

import com.example.petpal.data.model.Order
import com.example.petpal.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val orderCollection = db.collection("order")

    // -----------------------------
    // GET ALL ORDERS FOR LOGGED USER
    // -----------------------------
    fun getRecentOrder(
        result: (UiState<List<Order>>) -> Unit
    ){
        val userId = auth.currentUser?.uid
        if (userId == null) {
            result(UiState.Error("User not logged in"))
            return
        }

        result(UiState.Loading)

        orderCollection
            .whereEqualTo("owner_id", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Order::class.java)?.copy(id = doc.id)
                }
                    .filter { order ->
                        order.status != "completed"
                    }
                result(UiState.Success(list))
            }
            .addOnFailureListener { error ->
                result(UiState.Error(error.message ?: "Failed to load orders"))
            }
    }

    fun getHistory(
        result: (UiState<List<Order>>) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            result(UiState.Error("User not logged in"))
            return
        }

        result(UiState.Loading)

        orderCollection
            .whereEqualTo("owner_id", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Order::class.java)?.copy(id = doc.id)
                }
                    .filter { order ->
                        order.status == "completed"
                    }
                result(UiState.Success(list))
            }
            .addOnFailureListener { error ->
                result(UiState.Error(error.message ?: "Failed to load orders"))
            }
    }

    fun getOrdersByUser(
        result: (UiState<List<Order>>) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            result(UiState.Error("User not logged in"))
            return
        }

        result(UiState.Loading)

        orderCollection
            .whereEqualTo("owner_id", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Order::class.java)?.copy(id = doc.id)
                }
                result(UiState.Success(list))
            }
            .addOnFailureListener { error ->
                result(UiState.Error(error.message ?: "Failed to load orders"))
            }
    }



    // -----------------------------
    // CREATE NEW ORDER
    // -----------------------------
    fun createOrder(
        order: Order,
        result: (UiState<String>) -> Unit
    ) {
        val doc = orderCollection.document() // auto ID

        val finalOrder = order.copy(id = doc.id)

        doc.set(finalOrder)
            .addOnSuccessListener {
                result(UiState.Success(doc.id))
            }
            .addOnFailureListener { error ->
                result(UiState.Error(error.message ?: "Failed to create order"))
            }
    }

    // -----------------------------
    // GET ORDER BY ID
    // -----------------------------
    fun getOrderById(
        orderId: String,
        result: (UiState<Order>) -> Unit
    ) {
        orderCollection
            .document(orderId)
            .get()
            .addOnSuccessListener { doc ->
                val order = doc.toObject(Order::class.java)
                if (order != null) {
                    result(UiState.Success(order.copy(id = doc.id)))
                } else {
                    result(UiState.Error("Order not found"))
                }
            }
            .addOnFailureListener { error ->
                result(UiState.Error(error.message ?: "Failed to fetch order"))
            }
    }

    // -----------------------------
    // UPDATE ONLY STATUS
    // -----------------------------
    fun updateOrderStatus(
        orderId: String,
        newStatus: String,
        result: (UiState<String>) -> Unit
    ) {
        orderCollection
            .document(orderId)
            .update("status", newStatus)
            .addOnSuccessListener {
                result(UiState.Success("Status updated"))
            }
            .addOnFailureListener { error ->
                result(UiState.Error(error.message ?: "Failed to update status"))
            }
    }
}
