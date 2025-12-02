package com.example.petpal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.petpal.data.model.Pet
import com.example.petpal.presentation.theme.BlackText

@Composable
fun PetTiles(
    pets: List<Pet>,
    onPetClick: (Pet) -> Unit,
    onAllPetsClick: () -> Unit
) {
    // ambil max 3 item dan tambahkan tile terakhir untuk "All Pets"
    val displayList: List<Pet?> = pets.take(3) + listOf(null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Your Pets",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = BlackText,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Grid 2 kolom stabil
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            displayList.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { item ->
                        if (item == null) {
                            AllPetsCard(
                                modifier = Modifier.weight(1f),
                                onClick = onAllPetsClick
                            )
                        } else {
                            PetCard(
                                pet = item,
                                modifier = Modifier.weight(1f),
                                onClick = { onPetClick(item) }
                            )
                        }
                    }

                    // Jika row cuma 1, tambahkan tile kosong biar grid stabil
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun PetCard(
    pet: Pet,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = rememberAsyncImagePainter(pet.photoUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x99000000))
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = pet.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun AllPetsCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8E8E8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "All Pets",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = BlackText,
            )
        }
    }
}
