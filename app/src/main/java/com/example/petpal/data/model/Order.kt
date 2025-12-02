package com.example.petpal.data.model

import com.google.firebase.Timestamp

data class Order(
    val id: String = "",
    val pet_id: String = "",
    val branch: String = "",
    val service: String = "",
    val start_time: Timestamp? = null,
    val end_time: Timestamp? = null,
    val notes: String = "",
    val status: String = "",
    val price: Double = 0.0,
    val tier: String = "",
    val owner_id: String = ""
)

