package com.example.petpal.presentation.navigation


sealed class Screen(val route: String) {
    object OnBoarding : Screen("welcome_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")

    // Bottom Nav Screens
    object Home : Screen("home")
    object Pesanan : Screen("pesanan")
    object Riwayat : Screen("riwayat")
    object Profile : Screen("profile")

    // Sub-Screens
    object EditProfile : Screen("edit_profile")
    object ChangePassword : Screen("change_password")
}