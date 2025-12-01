package com.example.petpal.presentation.navigation


sealed class Screen(val route: String) {
    object OnBoarding : Screen("welcome_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Home : Screen("home_screen")
}