package com.example.petpal.presentation.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.petpal.data.model.User
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.component.PetPalTextField
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.viewmodel.AuthViewModel
import com.example.petpal.utils.UiState

@Composable
fun EditProfileScreen(
    viewModel: AuthViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: () -> Unit
) {
    val userState by viewModel.userData.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") } // Email read-only usually
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    // State Foto
    var currentPhotoUrl by remember { mutableStateOf("") } // Foto dari Firestore
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) } // Foto baru dari Galeri

    // Setup Image Picker (Hanya Gambar)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Init Data saat masuk layar
    LaunchedEffect(userState) {
        if (userState is UiState.Success) {
            val user = (userState as UiState.Success).data ?: User()
            name = user.name
            email = user.email
            phone = user.phoneNumber
            location = user.location
            currentPhotoUrl = user.photoUrl
        }
    }

    // Handle Update Success
    LaunchedEffect(updateState) {
        if (updateState is UiState.Success && (updateState as UiState.Success).data) {
            Toast.makeText(context, "Profil Berhasil Diupdate", Toast.LENGTH_SHORT).show()
            viewModel.resetUpdateState()
            onNavigateBack()
        }
        if (updateState is UiState.Error) {
            Toast.makeText(context, (updateState as UiState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Toolbar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                text = "Edit Profil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.width(48.dp)) // Balance the icon button
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logika Menampilkan Foto: Prioritaskan Foto Baru (Uri), kalau null pakai Foto Lama (Url)
        val imageModel = if (selectedImageUri != null) {
            selectedImageUri
        } else {
            currentPhotoUrl.ifEmpty { "https://ui-avatars.com/api/?name=$name" }
        }

        Box(contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = imageModel,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Tombol Ganti Foto
        Button(
            onClick = {
                // Buka Galeri
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = PetPalDarkGreen),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text("Ganti Foto", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Form
        PetPalTextField(label = "Nama Lengkap", value = name, onValueChange = { name = it }, placeholder = "")
        Spacer(modifier = Modifier.height(16.dp))

        // Email biasanya tidak bisa diedit dengan mudah di Firebase tanpa re-auth, jadi kita disable atau read-only visual
        PetPalTextField(label = "Email", value = email, onValueChange = {}, placeholder = "")
        Spacer(modifier = Modifier.height(16.dp))

        PetPalTextField(label = "No. Telepon", value = phone, onValueChange = { phone = it }, placeholder = "", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        Spacer(modifier = Modifier.height(16.dp))

        PetPalTextField(label = "Lokasi", value = location, onValueChange = { location = it }, placeholder = "Masukkan Lokasi")

        Spacer(modifier = Modifier.height(32.dp))

        if (updateState is UiState.Loading) {
            CircularProgressIndicator(color = PetPalDarkGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Mengupload...", fontSize = 12.sp, color = Color.Gray)
        } else {
            PetPalPrimaryButton(
                text = "Simpan",
                onClick = {
                    viewModel.updateProfile(name, phone, location, selectedImageUri, currentPhotoUrl)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ganti Password",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.clickable { onNavigateToChangePassword() }
        )
    }
}