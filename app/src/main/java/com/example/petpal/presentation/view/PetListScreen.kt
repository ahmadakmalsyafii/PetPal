package com.example.petpal.presentation.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petpal.R
import com.example.petpal.presentation.component.PetCard
import com.example.petpal.presentation.component.PetPalPrimaryButton
import com.example.petpal.presentation.viewmodel.PetViewModel
import com.example.petpal.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    viewModel: PetViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddPet: () -> Unit
) {
    val petsState by viewModel.petsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserPets()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Daftar Hewan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAddPet) {
                        Icon(Icons.Default.Add, contentDescription = "Tambah")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when (val state = petsState) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiState.Error -> Text(state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    val pets = state.data

                    if (pets.isEmpty()) {
                        // Tampilan Kosong (Sesuai Daftar Hewan 0.png)
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Ganti dengan icon paw print jika ada di drawable
                            Icon(
                                painter = painterResource(id = R.drawable.logo_petpal),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Belum ada hewan terdaftar\nTambahkan hewan peliharaanmu\nsekarang!",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            PetPalPrimaryButton(
                                text = "Tambah Hewan",
                                onClick = onNavigateToAddPet,
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    } else {
                        // Tampilan List (Sesuai Daftar Hewan.png)
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(pets) { pet ->
                                PetCard(pet = pet)
                            }
                        }
                    }
                }
            }
        }
    }
}