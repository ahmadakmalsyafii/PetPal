package com.example.petpal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.petpal.data.repository.AuthRepository
import com.example.petpal.presentation.view.HomeScreen
import com.example.petpal.presentation.view.LoginScreen
import com.example.petpal.presentation.view.OnBoardingScreen
import com.example.petpal.presentation.view.RegisterScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    val authRepository = AuthRepository()
    // Cek apakah user sudah login
    val startDestination = if (authRepository.currentUser != null) Screen.Home.route else Screen.OnBoarding.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ONBOARDING
        composable(Screen.OnBoarding.route) {
            OnBoardingScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        // LOGIN
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route){
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OnBoarding.route) { inclusive = true }
                    }
                }
            )
        }

        // REGISTER
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route){
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    // Langsung ke Home setelah sukses daftar
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OnBoarding.route) { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    authRepository.logout()
                    // Kembali ke Login dan hapus history Home
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}