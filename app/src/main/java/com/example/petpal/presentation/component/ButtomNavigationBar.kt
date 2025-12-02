package com.example.petpal.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.petpal.presentation.navigation.Screen
import com.example.petpal.presentation.theme.PetPalDarkGreen

@Composable
fun PetPalBottomBar(navController: NavController) {
    val items = listOf(
        Screen.Home to "Beranda",
        Screen.Pesanan to "Pemesanan",
        Screen.Riwayat to "Riwayat",
        Screen.Profile to "Profil"
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hanya tampilkan navbar di halaman utama
    val showBottomBar = items.any { it.first.route == currentRoute }

    if (showBottomBar) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = PetPalDarkGreen
        ) {
            items.forEach { (screen, label) ->
                val selected = currentRoute == screen.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = when (screen) {
                                Screen.Home -> Icons.Default.Home
                                Screen.Pesanan -> Icons.Default.ShoppingCart
                                Screen.Riwayat -> Icons.Default.History
                                Screen.Profile -> Icons.Default.Person
                                else -> Icons.Default.Home
                            },
                            contentDescription = label
                        )
                    },
                    label = { Text(label) },
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PetPalDarkGreen,
                        selectedTextColor = PetPalDarkGreen,
                        indicatorColor = PetPalDarkGreen.copy(alpha = 0.1f),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}