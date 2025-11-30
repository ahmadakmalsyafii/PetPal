package com.example.petpal.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val photoUrl: String = "",
    val userType: UserType = UserType.PET_OWNER,
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserType {
    PET_OWNER,
    PET_SITTER
}