package com.example.petpal.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.petpal.data.model.Pet
// Import Warna Langsung
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.PetPalGreenAccent
import com.example.petpal.presentation.theme.GrayText

@Composable
fun PetCard(pet: Pet, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        // Background Putih Langsung
        colors = CardDefaults.cardColors(
            containerColor = White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Hewan
            AsyncImage(
                model = pet.photoUrl.ifEmpty { "https://placehold.co/100x100/png?text=Pet" },
                contentDescription = "Foto ${pet.name}",
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                // Nama Hewan (Hitam Tebal)
                Text(
                    text = pet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackText
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Jenis Hewan (Aksen Hijau)
                Text(
                    text = pet.type,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PetPalGreenAccent
                )

                // Catatan (Abu-abu)
                if (pet.notes.isNotEmpty()) {
                    Text(
                        text = pet.notes,
                        fontSize = 12.sp,
                        color = GrayText,
                        maxLines = 1
                    )
                }
            }
        }
    }
}