package com.example.petpal.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
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
import com.example.petpal.data.repository.AuthRepository
import com.example.petpal.data.model.Pet
import com.example.petpal.presentation.view.AddPetScreen
import com.example.petpal.presentation.view.ChangePasswordScreen
import com.example.petpal.presentation.view.AddPetScreen
import com.example.petpal.presentation.view.BranchSelectionScreen
import com.example.petpal.presentation.view.ChangePasswordScreen
import com.example.petpal.presentation.view.EditProfileScreen
import com.example.petpal.presentation.view.HistoryScreen
import com.example.petpal.presentation.view.HomeScreen
import com.example.petpal.presentation.view.LoginScreen
import com.example.petpal.presentation.view.OnBoardingScreen
import com.example.petpal.presentation.view.OrderFormScreen
import com.example.petpal.presentation.view.PaymentScreen
import com.example.petpal.presentation.view.PaymentMethodSelectionScreen
import com.example.petpal.presentation.view.PemesananScreen
import com.example.petpal.presentation.view.PetListScreen
import com.example.petpal.presentation.view.PetSelectionScreen
import com.example.petpal.presentation.view.ProfileScreen
import com.example.petpal.presentation.view.RegisterScreen
import com.example.petpal.presentation.view.TierSelectionScreen
import com.example.petpal.presentation.component.PetPalBottomBar

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
                val selectedBranch = savedStateHandle.getStateFlow<String?>("selected_branch", null).collectAsState().value
                val startTime = savedStateHandle.getStateFlow<String?>("start_time", null).collectAsState().value
                val startDate = savedStateHandle.getStateFlow<String?>("start_date", null).collectAsState().value
                val endTime = savedStateHandle.getStateFlow<String?>("end_time", null).collectAsState().value
                val endDate = savedStateHandle.getStateFlow<String?>("end_date", null).collectAsState().value

                OrderFormScreen(
                    serviceType = serviceType,
                    onNavigateBack = { navController.popBackStack() },
                    selectedTierFromNav = selectedTier,
                    selectedPetsFromNav = selectedPets,
                    selectedBranchFromNav = selectedBranch,
                    startTimeFromNav = startTime,
                    startDateFromNav = startDate,
                    endTimeFromNav = endTime,
                    endDateFromNav = endDate,
                    onNavigateToPetSelection = { sTime, sDate, eTime, eDate ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("start_time", sTime)
                            set("start_date", sDate)
                            set("end_time", eTime)
                            set("end_date", eDate)
                        }
                        navController.navigate(Screen.PetSelection.route)
                    },
                    onNavigateToTierSelection = { sTime, sDate, eTime, eDate ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("start_time", sTime)
                            set("start_date", sDate)
                            set("end_time", eTime)
                            set("end_date", eDate)
                        }
                        navController.navigate(Screen.TierSelection.route)
                    },
                    onNavigateToBranchSelection = { sTime, sDate, eTime, eDate ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("start_time", sTime)
                            set("start_date", sDate)
                            set("end_time", eTime)
                            set("end_date", eDate)
                        }
                        navController.navigate(Screen.BranchSelection.route)
                    },
                    onSubmitOrder = { pets, petNames, sTime, sDate, eTime, eDate, tier, branch, notes, duration, tPrice, total ->
                        navController.currentBackStackEntry?.savedStateHandle?.apply {
                            set("payment_service_type", serviceType)
                            set("payment_pets", pets)
                            set("payment_pet_names", petNames)
                            set("payment_start_time", sTime)
                            set("payment_start_date", sDate)
                            set("payment_end_time", eTime)
                            set("payment_end_date", eDate)
                            set("payment_tier", tier)
                            set("payment_branch", branch)
                            set("payment_notes", notes)
                            set("payment_duration", duration)
                            set("payment_tier_price", tPrice)
                            set("payment_total_price", total)
                        }
                        navController.navigate(Screen.Payment.route)
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
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddPet = {
                        navController.navigate(Screen.AddPet.route)
                    },
                    onPetsSelected = { pets ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_pets", pets)
                        navController.popBackStack()
                    },
                    initialSelectedPets = initiallySelected
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
            ) {
                TierSelectionScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTierSelected = { tier ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_tier", tier)
                        navController.popBackStack()
                    }
                )
            }

            // Branch Selection
            composable(
                route = Screen.BranchSelection.route,
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
            ) {
                BranchSelectionScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onBranchSelected = { branch ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_branch", branch)
                        navController.popBackStack()
                    }
                )
            }

            // Payment Screen
            composable(
                route = Screen.Payment.route,
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
                val savedStateHandle = previousEntry?.savedStateHandle

                val serviceType = savedStateHandle?.get<String>("payment_service_type") ?: "Boarding"
                val pets = savedStateHandle?.get<List<Pet>>("payment_pets") ?: emptyList()
                val petNames = savedStateHandle?.get<String>("payment_pet_names") ?: ""
                val startTime = savedStateHandle?.get<String>("payment_start_time") ?: ""
                val startDate = savedStateHandle?.get<String>("payment_start_date") ?: ""
                val endTime = savedStateHandle?.get<String>("payment_end_time") ?: ""
                val endDate = savedStateHandle?.get<String>("payment_end_date") ?: ""
                val tier = savedStateHandle?.get<String>("payment_tier") ?: ""
                val branch = savedStateHandle?.get<String>("payment_branch") ?: ""
                val notes = savedStateHandle?.get<String>("payment_notes") ?: ""
                val duration = savedStateHandle?.get<Int>("payment_duration") ?: 0
                val tierPrice = savedStateHandle?.get<Double>("payment_tier_price") ?: 0.0
                val totalPrice = savedStateHandle?.get<Double>("payment_total_price") ?: 0.0
                val paymentMethod = backStackEntry.savedStateHandle.getStateFlow<String?>("selected_payment_method", null).collectAsState().value

                PaymentScreen(
                    serviceType = serviceType,
                    petNames = petNames,
                    startTime = startTime,
                    startDate = startDate,
                    endTime = endTime,
                    endDate = endDate,
                    tier = tier,
                    branch = branch,
                    durationHours = duration,
                    tierPrice = tierPrice,
                    totalPrice = totalPrice,
                    selectedPaymentMethod = paymentMethod,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToPaymentMethod = {
                        navController.navigate(Screen.PaymentMethodSelection.route)
                    },
                    onConfirmPayment = { viewModel ->
                        // Create order with all the data
                        viewModel.createOrderFromPayment(
                            pets = pets,
                            serviceType = serviceType,
                            startTime = startTime,
                            startDate = startDate,
                            endTime = endTime,
                            endDate = endDate,
                            tier = tier,
                            branch = branch,
                            notes = notes,
                            totalPrice = totalPrice,
                            onSuccess = {
                                navController.popBackStack(Screen.Home.route, inclusive = false)
                            },
                            onError = { error ->
                                // Show error (can be handled with a snackbar or toast)
                                android.util.Log.e("PaymentScreen", "Error creating order: $error")
                            }
                        )
                    }
                )
            }

            // Payment Method Selection Screen
            composable(
                route = Screen.PaymentMethodSelection.route,
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
            ) {
                PaymentMethodSelectionScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPaymentMethodSelected = { method ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_payment_method", method.name)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
