package com.example.petpal.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.petpal.R
import com.example.petpal.data.model.User
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.viewmodel.AuthViewModel
import com.example.petpal.utils.UiState

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateToEditProfile: () -> Unit,
    onNavigateToPetList: () -> Unit,
    onLogout: () -> Unit
) {
    val userState by viewModel.userData.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "Keluar", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
            text = { Text(text = "Konfirmasi Keluar?", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PetPalDarkGreen),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.width(100.dp)
                ) {
                    Text("Keluar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.width(100.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Batal")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Background abu-abu muda
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header (Title & Edit Icon)
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Profil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = onNavigateToEditProfile,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit Profil", tint = PetPalDarkGreen)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = userState) {
            is UiState.Loading -> CircularProgressIndicator(color = PetPalDarkGreen)
            is UiState.Error -> Text("Gagal memuat data", color = Color.Red)
            is UiState.Success -> {
                val user = state.data ?: User()

                // Foto Profil
                AsyncImage(
                    model = user.photoUrl.ifEmpty { "https://ui-avatars.com/api/?name=${user.name}&background=random" },
                    contentDescription = "Foto Profil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nama Besar
                Text(
                    text = user.name.ifEmpty { "Pengguna" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PetPalDarkGreen
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Info Cards
                ProfileInfoCard(icon = Icons.Default.Person, label = "Nama Lengkap", value = user.name)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoCard(icon = Icons.Default.Email, label = "Email", value = user.email)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoCard(icon = Icons.Default.Phone, label = "Telepon", value = user.phoneNumber)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoCard(icon = Icons.Default.LocationOn, label = "Lokasi", value = user.location)

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Daftar Hewan (Sesuai UI)
                PetPalPrimaryButton(
                    text = "Daftar Hewan",
                    onClick = onNavigateToPetList
                )

                Spacer(modifier = Modifier.weight(1f))

                // Tombol Keluar
                TextButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Keluar", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = PetPalDarkGreen, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = value.ifEmpty { "-" }, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}