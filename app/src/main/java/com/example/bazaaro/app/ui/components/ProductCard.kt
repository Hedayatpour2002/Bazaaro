package com.example.bazaaro.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bazaaro.data.model.FavoriteEntity
import com.example.bazaaro.data.model.Rating


@Composable
fun ProductCard(
    id: Int,
    title: String,
    image: String,
    rating: Rating,
    price: Double,
    category: String,
    isFavorite: Boolean,
    toggleFavorite: (FavoriteEntity) -> Unit,
    clickHandler: (Int) -> Unit
) {
    Column(modifier = Modifier
        .width(180.dp)
        .fillMaxHeight()
        .clickable {
            clickHandler(id)
        }) {

        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .width(160.dp)
                    .height(170.dp)
                    .align(Alignment.Center)
            )

            IconButton(
                onClick = {
                    toggleFavorite(
                        FavoriteEntity(
                            id = id,
                            image = image,
                            price = price,
                            title = title,
                            ratingCount = rating.count,
                            ratingRate = rating.rate,
                            category = category,
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp)
                    .background(color = Color(0xFF292526), shape = CircleShape)
                    .width(36.dp)
                    .height(36.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "heart",
                    modifier = Modifier.size(24.dp),
                    tint = if (isFavorite) Color(0xFFDB3022) else Color.White
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        Text(
            text = title, color = Color(0xFF121111), fontSize = 14.sp, fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))

        Text(text = category, color = Color(0xFF787676), fontSize = 12.sp)
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = price.toString(),
                color = Color(0xFF292526),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "star",
                    tint = Color(0xFFFFD33C)
                )
                Spacer(Modifier.width(4.dp))

                Text(text = rating.rate.toString(), fontSize = 12.sp)
            }
        }
    }
}
