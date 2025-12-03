package com.example.petpal.data.model

import com.google.firebase.Timestamp

data class Order(
    val id: String = "",
    val petId: String = "",
    val branch: String = "",
    val service: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val notes: String = "",
    val status: String = "",
    val price: Double = 0.0,
    val tier: String = "",
    val ownerId: String = ""
)

