package com.example.petpal.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

// Import warna sesuai style PetPal
import com.example.petpal.presentation.theme.White
import com.example.petpal.presentation.theme.PetPalGreenAccent
import com.example.petpal.presentation.theme.GrayText

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetPalCarousel(
    imageUrls: List<String> = emptyList(),
    imageDrawables: List<Int> = emptyList(), // drawable resource IDs
    modifier: Modifier = Modifier
) {
    val imagesCount = if (imageUrls.isNotEmpty()) imageUrls.size else imageDrawables.size
    if (imagesCount == 0) return

    val pagerState = rememberPagerState(pageCount = { imagesCount })

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                if (imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrls[page],
                        contentDescription = "Carousel image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else if (imageDrawables.isNotEmpty()) {
                    Image(
                        painter = painterResource(id = imageDrawables[page]),
                        contentDescription = "Carousel image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(imagesCount) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) PetPalGreenAccent else GrayText.copy(alpha = 0.3f)
                        )
                )
            }
        }
    }
}
