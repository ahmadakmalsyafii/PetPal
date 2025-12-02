package com.example.petpal.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.petpal.R
import com.example.petpal.data.model.Pet
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.viewmodel.PetViewModel
import com.example.petpal.utils.UiState

@Composable
fun PetSelectionScreen(
    initialSelectedPets: List<Pet> = emptyList(),
    onNavigateBack: () -> Unit,
    onNavigateToAddPet: () -> Unit,
    onPetsSelected: (List<Pet>) -> Unit,
    viewModel: PetViewModel = viewModel()
) {
    val state by viewModel.petsState.collectAsState()
    var selectedPets by remember { mutableStateOf(initialSelectedPets.map { it.id }.toSet()) }

    LaunchedEffect(Unit) {
        viewModel.loadUserPets()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_arrowleft_foreground),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Pilih Hewan Peliharaan-mu",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val result = state) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Error -> {
                    Text(
                        text = result.message,
                        color = BlackText,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                is UiState.Success<*> -> {
                    val pets = (result as UiState.Success<List<Pet>>).data

                    if (pets.isEmpty()) {
                        // No pets - show add pet button
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Belum ada hewan peliharaan",
                                fontSize = 16.sp,
                                color = GrayText,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Button(
                                onClick = onNavigateToAddPet,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GrayText.copy(alpha = 0.2f),
                                    contentColor = BlackText
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_arrowright),
                                    contentDescription = "Add",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Tambah Hewan",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else {
                        // Show pets list
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(pets) { pet ->
                                PetSelectionItem(
                                    pet = pet,
                                    isSelected = selectedPets.contains(pet.id),
                                    onSelectionChanged = { isSelected ->
                                        selectedPets = if (isSelected) {
                                            selectedPets + pet.id
                                        } else {
                                            selectedPets - pet.id
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = {
                    val selected = when (val result = state) {
                        is UiState.Success<*> -> {
                            ((result as UiState.Success<List<Pet>>).data).filter { selectedPets.contains(it.id) }
                        }
                        else -> emptyList()
                    }
                    onPetsSelected(selected)
                },
                enabled = selectedPets.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PetPalDarkGreen,
                    contentColor = White,
                    disabledContainerColor = GrayText.copy(alpha = 0.5f),
                    disabledContentColor = White
                ),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Pilih Hewan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PetSelectionItem(
    pet: Pet,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectionChanged(!isSelected) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pet Image
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(GrayText.copy(alpha = 0.1f))
        ) {
            if (pet.photoUrl.isNotEmpty()) {
                AsyncImage(
                    model = pet.photoUrl,
                    contentDescription = pet.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.icon_petoption_foreground),
                    contentDescription = pet.name,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Pet Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = pet.name.ifEmpty { "Unknown" },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText
            )

            Text(
                text = "${pet.type.ifEmpty { "" }} | ${pet.gender.ifEmpty { "" }}",
                fontSize = 14.sp,
                color = GrayText
            )
        }

        // Checkbox
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(
                    width = 2.dp,
                    color = if (isSelected) PetPalDarkGreen else GrayText.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .background(
                    color = if (isSelected) PetPalDarkGreen else White,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_petoption_foreground),
                    contentDescription = "Selected",
                    tint = White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
