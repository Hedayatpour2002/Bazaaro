package com.example.bazaaro.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.bazaaro.app.ui.components.ErrorView
import com.example.bazaaro.app.ui.components.LoadingView
import com.example.bazaaro.data.model.Product
import com.example.bazaaro.data.model.Rating
import kotlin.math.roundToInt

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    navController: NavHostController,
    productId: Int
) {
    val detailState = detailViewModel.detailState.collectAsStateWithLifecycle()

    when (val state = detailState.value) {
        is DetailState.Error -> ErrorView(state.errorMessage) {
            detailViewModel.getSingleProduct()
        }

        DetailState.Loading -> LoadingView()
        is DetailState.Success -> MainView(state.product, onAddToCartHandler = {
            detailViewModel.addToCart(state.product!!)
        }, backClickHandler = {
            navController.navigateUp()
        }) {
            detailViewModel.getSingleProduct()

        }
    }
}

@Composable
private fun MainView(
    product: Product?,
    onAddToCartHandler: () -> Unit,
    backClickHandler: () -> Unit,
    errorHandler: () -> Unit
) {
    if (product == null) {
        ErrorView(errorMessage = null) {
            errorHandler()
        }
    } else {
        DetailView(product, onAddToCartHandler, backClickHandler)
    }
}

@Composable
private fun DetailView(
    product: Product, onAddToCartHandler: () -> Unit, backClickHandler: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var expandedColor by remember { mutableStateOf(false) }

    var selectedSize by remember { mutableStateOf("Size") }
    var selectedColor by remember { mutableStateOf("Color") }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = backClickHandler) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
                Text(text = "Product Detail", fontSize = 20.sp, color = Color(0xFF222222))
                IconButton(onClick = { isLiked = !isLiked }) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "like",
                        tint = if (isLiked) Color(0xFFDB3022) else Color.Gray
                    )
                }
            }
            AsyncImage(
                model = product.image,
                contentDescription = product.description,
                modifier = Modifier
                    .height(400.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.FillHeight
            )
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = product.title,
                    color = Color(0xFF1B1B1B),
                    fontSize = 20.sp,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = product.category, fontSize = 12.sp, color = Color(0xFF9B9B9B))
                        RatingStars(rating = product.rating)
                    }
                    Text(
                        text = "$${product.price}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFDB3022)
                    )
                }
                Text(text = product.description, fontSize = 12.sp, color = Color(0xFF838383))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box() {
                        TextButton(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                            shape = RoundedCornerShape(100),
                            border = BorderStroke(1.dp, Color(0xFFE6E6E6)),
                            onClick = { expanded = !expanded }) {
                            Text(text = selectedSize)
                            Spacer(modifier = Modifier.width(74.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = {
                                Text(text = "12")
                            }, onClick = { selectedSize = "12"; expanded = false })

                            DropdownMenuItem(text = {
                                Text(text = "18")
                            }, onClick = { selectedSize = "18"; expanded = false })

                            DropdownMenuItem(text = {
                                Text(text = "20")
                            }, onClick = { selectedSize = "20"; expanded = false })
                        }
                    }
                    Box() {
                        TextButton(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                            shape = RoundedCornerShape(100),
                            border = BorderStroke(1.dp, Color(0xFFE6E6E6)),
                            onClick = { expandedColor = !expandedColor }) {
                            Text(text = selectedColor)
                            Spacer(modifier = Modifier.width(74.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(expanded = expandedColor,
                            onDismissRequest = { expandedColor = false }) {
                            DropdownMenuItem(text = {
                                Text(text = "Red")
                            }, onClick = { selectedColor = "Red"; expandedColor = false })

                            DropdownMenuItem(text = {
                                Text(text = "Black")
                            }, onClick = { selectedColor = "Black"; expandedColor = false })

                            DropdownMenuItem(text = {
                                Text(text = "White")
                            }, onClick = { selectedColor = "White"; expandedColor = false })
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Shipping & Returns")

                    Text(
                        text = "Free standard shipping and free 60-day returns",
                        color = Color(0xFF838383)
                    )
                }
            }

        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDB3022), contentColor = Color.White
            ),
            onClick = onAddToCartHandler
        ) {
            Text(text = "Add to Cart")
        }
    }

}

@Composable
fun RatingStars(rating: Rating) {
    val roundedRating = rating.rate.roundToInt()

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val isFilled = index < roundedRating
            Star(filled = isFilled)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "(${rating.count})", fontSize = 10.sp, color = Color(0xFF9B9B9B))
    }
}

@Composable
private fun Star(filled: Boolean) {
    Icon(
        imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
        contentDescription = null,
        tint = if (filled) Color(0xFFFFBA49) else Color.Gray,
        modifier = Modifier.size(24.dp)
    )
}