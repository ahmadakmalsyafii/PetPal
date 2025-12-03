package com.example.petpal.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter
import com.example.petpal.data.model.Rating

@Composable
fun RatingsCarousel(
    ratings: List<Rating>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp)
    ) {
        // Title
        Text(
            text = "Reviews & Ratings",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (ratings.isEmpty()) {
            Text(
                "No ratings yet",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(ratings) { rating ->
                    RatingCardWithPhoto(rating = rating)
                }
            }
        }
    }
}


@Composable
fun RatingCardWithPhoto(rating: Rating, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(240.dp)
            .height(240.dp), // fixed height
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White), // force white background
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Photo
            Image(
                painter = rememberAsyncImagePainter(rating.photoUrl),
                contentDescription = rating.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White), // light gray placeholder
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Name
            Text(
                text = rating.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            // Stars
            Spacer(modifier = Modifier.height(4.dp))
            Row (
                modifier = modifier.fillMaxWidth()
            ){
                StarRating(rating = rating.rating)
                Text(
                    text = rating.rating.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            // Review
            Text(
                text = rating.review,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                maxLines = 3
            )
        }
    }
}

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
//    maxRating: Int = 5
) {
    Row(modifier = modifier) {
        for (i in 1..rating) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Star",
                tint = Color(0xFFFFD700), // golden stars
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
