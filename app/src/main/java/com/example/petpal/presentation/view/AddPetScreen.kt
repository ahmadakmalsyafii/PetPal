package com.example.petpal.presentation.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.component.PetPalTextField
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.viewmodel.PetViewModel
import com.example.petpal.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    viewModel: PetViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val addState by viewModel.addPetState.collectAsState()

    // Form State
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image Picker
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> imageUri = uri }

    // Handle Success
    LaunchedEffect(addState) {
        if (addState is UiState.Success && (addState as UiState.Success).data) {
            Toast.makeText(context, "Berhasil Menambah Hewan", Toast.LENGTH_SHORT).show()
            viewModel.resetAddState()
            onNavigateBack()
        }
        if (addState is UiState.Error) {
            Toast.makeText(context, (addState as UiState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Hewan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // SCROLLABLE FORM
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto Picker
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF2F2F2)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.petpal.R.drawable.ic_launcher_foreground), // Ganti placeholder user
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { pickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                colors = ButtonDefaults.buttonColors(containerColor = PetPalDarkGreen),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Tambah Foto", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            PetPalTextField(label = "Nama Hewan", value = name, onValueChange = { name = it }, placeholder = "Nama Hewan")
            Spacer(modifier = Modifier.height(16.dp))

            PetPalTextField(label = "Jenis Hewan", value = type, onValueChange = { type = it }, placeholder = "Jenis Hewan (Cth: Kucing)")
            Spacer(modifier = Modifier.height(16.dp))

            PetPalTextField(label = "Umur", value = age, onValueChange = { age = it }, placeholder = "Umur", keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number))
            Spacer(modifier = Modifier.height(16.dp))

            PetPalTextField(label = "Notes", value = notes, onValueChange = { notes = it }, placeholder = "Notes Tambahan")
            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox Gender
            Text("Jenis Kelamin", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.align(Alignment.Start))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = gender == "Jantan", onCheckedChange = { if (it) gender = "Jantan" })
                Text("Jantan")
                Spacer(modifier = Modifier.width(16.dp))
                Checkbox(checked = gender == "Betina", onCheckedChange = { if (it) gender = "Betina" })
                Text("Betina")
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (addState is UiState.Loading) {
                CircularProgressIndicator(color = PetPalDarkGreen)
            } else {
                PetPalPrimaryButton(
                    text = "Simpan",
                    onClick = {
                        if (name.isNotEmpty() && type.isNotEmpty()) {
                            viewModel.addPet(name, type, age, notes, gender, imageUri)
                        } else {
                            Toast.makeText(context, "Nama dan Jenis wajib diisi", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}