package com.example.petpal.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petpal.data.repository.AuthRepository
import com.example.petpal.presentation.component.PetPalBottomBar
import com.example.petpal.presentation.view.ChangePasswordScreen
import com.example.petpal.presentation.view.EditProfileScreen
import com.example.petpal.presentation.view.HomeScreen
import com.example.petpal.presentation.view.LoginScreen
import com.example.petpal.presentation.view.OnBoardingScreen
import com.example.petpal.presentation.view.ProfileScreen
import com.example.petpal.presentation.view.RegisterScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val authRepository = AuthRepository()

    // Cek apakah user sudah login
//    val startDestination = if (authRepository.currentUser != null) Screen.Home.route else Screen.OnBoarding.route
    val startDestination = Screen.Login.route

    Scaffold (
        bottomBar = { PetPalBottomBar(navController) }
    ){ paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
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

            composable(Screen.Pesanan.route) {
                // Placeholder Halaman Pesanan
                Text("Halaman Pesanan")
            }
            composable(Screen.Riwayat.route) {
                // Placeholder Halaman Riwayat
                Text("Halaman Riwayat")
            }
            composable(Screen.Profile.route) {
                ProfileScreen (
                    onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Sub-Pages Profile
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToChangePassword = { navController.navigate(Screen.ChangePassword.route) }
                )
            }
            composable(Screen.ChangePassword.route) {
                ChangePasswordScreen (
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
