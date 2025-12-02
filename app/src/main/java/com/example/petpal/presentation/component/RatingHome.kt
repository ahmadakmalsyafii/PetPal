package com.example.petpal.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.example.petpal.data.model.Rating

@Composable
fun RatingsCarousel(
    ratings: List<Rating>,
    modifier: Modifier = Modifier
) {
    if (ratings.isEmpty()) {
        Text("No ratings yet", modifier = Modifier.padding(16.dp))
    } else {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
        ) {
            items(ratings) { rating ->
                RatingCardWithPhoto(rating = rating)
            }
        }
    }
}

@Composable
fun RatingCardWithPhoto(rating: Rating, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.width(220.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Photo
            Image(
                painter = rememberAsyncImagePainter(rating.photoUrl),
                contentDescription = rating.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Name
            Text(text = rating.name, style = MaterialTheme.typography.titleMedium)

            // Stars
            StarRating(rating = rating.rating)

            Spacer(modifier = Modifier.height(8.dp))

            // Review
            Text(text = rating.review, style = MaterialTheme.typography.bodyMedium, maxLines = 4)
        }
    }
}

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Row(modifier = modifier) {
        for (i in 1..maxRating) {
            if (i <= rating) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Star",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
