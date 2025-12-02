package com.example.petpal.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.petpal.data.model.Pet
import com.example.petpal.data.repository.AuthRepository
import com.example.petpal.presentation.component.PetPalBottomBar
import com.example.petpal.presentation.view.AddPetScreen
import com.example.petpal.presentation.view.ChangePasswordScreen
import com.example.petpal.presentation.view.EditProfileScreen
import com.example.petpal.presentation.view.HistoryScreen
import com.example.petpal.presentation.view.HomeScreen
import com.example.petpal.presentation.view.LoginScreen
import com.example.petpal.presentation.view.OnBoardingScreen
import com.example.petpal.presentation.view.OrderFormScreen
import com.example.petpal.presentation.view.PemesananScreen
import com.example.petpal.presentation.view.PetListScreen
import com.example.petpal.presentation.view.PetSelectionScreen
import com.example.petpal.presentation.view.ProfileScreen
import com.example.petpal.presentation.view.RegisterScreen
import com.example.petpal.presentation.view.TierSelectionScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val authRepository = AuthRepository()

    // Cek apakah user sudah login
//    val startDestination = if (authRepository.currentUser != null) Screen.Home.route else Screen.OnBoarding.route
    val startDestination = Screen.Login.route
//    val startDestination = Screen.Home.route

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

            // PESANAN
            composable(Screen.Pesanan.route) {
                PemesananScreen(
                    onNavigateToOrderForm = { serviceType ->
                        navController.navigate(Screen.OrderForm.createRoute(serviceType))
                    }
                )
            }

            composable(Screen.Riwayat.route) {
                HistoryScreen(
                    onNavigateToHistory = { navController.navigate(Screen.Riwayat.route)},
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onNavigateToPetList = { navController.navigate(Screen.PetList.route) },
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

            composable(Screen.PetList.route) {
                PetListScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddPet = { navController.navigate(Screen.AddPet.route) }
                )
            }

            composable(Screen.AddPet.route) {
                AddPetScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Order Form
            composable(
                route = Screen.OrderForm.route,
                arguments = listOf(
                    navArgument("serviceType") { type = NavType.StringType }
                ),
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 300)
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 300)
                    )
                }
            ) { backStackEntry ->
                val serviceType = backStackEntry.arguments?.getString("serviceType") ?: "Boarding"
                val savedStateHandle = backStackEntry.savedStateHandle
                val selectedTier = savedStateHandle.getStateFlow<String?>("selected_tier", null).collectAsState().value
                val selectedPets = savedStateHandle.get<List<Pet>>("selected_pets")

                OrderFormScreen(
                    serviceType = serviceType,
                    selectedTierFromNav = selectedTier,
                    selectedPetsFromNav = selectedPets,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToPetSelection = {
                        navController.navigate(Screen.PetSelection.route)
                    },
                    onNavigateToTierSelection = {
                        navController.navigate(Screen.TierSelection.route)
                    },
                    onSubmitOrder = {
                        navController.popBackStack()
                    }
                )
            }

            // Pet Selection
            composable(
                route = Screen.PetSelection.route,
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 300)
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 300)
                    )
                }
            ) { backStackEntry ->
                val previousEntry = navController.previousBackStackEntry
                val initiallySelected = previousEntry?.savedStateHandle?.get<List<Pet>>("selected_pets").orEmpty()

                PetSelectionScreen(
                    initialSelectedPets = initiallySelected,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddPet = {
                        navController.navigate(Screen.AddPet.route)
                    },
                    onPetsSelected = { pets ->
                        previousEntry?.savedStateHandle?.set("selected_pets", pets)
                        navController.popBackStack()
                    }
                )
            }

            // Tier Selection
            composable(
                route = Screen.TierSelection.route,
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 300)
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 300)
                    )
                }
            ) { backStackEntry ->
                val previousEntry = navController.previousBackStackEntry

                TierSelectionScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTierSelected = { tier ->
                        previousEntry?.savedStateHandle?.set("selected_tier", tier)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
