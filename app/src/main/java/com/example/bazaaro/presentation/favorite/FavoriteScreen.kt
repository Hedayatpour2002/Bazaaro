package com.example.bazaaro.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazaaro.R
import com.example.bazaaro.app.DETAIL_SCREEN_ROUTE
import com.example.bazaaro.app.ui.components.ErrorView
import com.example.bazaaro.app.ui.components.LoadingView
import com.example.bazaaro.app.ui.components.ProductCard
import com.example.bazaaro.data.model.FavoriteEntity
import com.example.bazaaro.data.model.Rating


@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val favoriteState = favoriteViewModel.favoriteState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "back"
                )
            }
            Text(text = "Favorites", fontSize = 20.sp, color = Color(0xFF222222))

            IconButton(onClick = {
                navController.navigate("cart")
            }) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
            }
        }


        when (val state = favoriteState.value) {
            FavoriteState.Loading -> LoadingView()
            is FavoriteState.Success -> MainView(favorites = state.favorites, isFavorite = {
                val isFavorite by favoriteViewModel.isFavorite(it)
                    .collectAsStateWithLifecycle(false)
                isFavorite
            }, toggleFavorite = {
                favoriteViewModel.toggleFavorite(it)
            }) { clickedItemId ->
                navController.navigate("$DETAIL_SCREEN_ROUTE/$clickedItemId")
            }

            is FavoriteState.Error -> ErrorView(errorMessage = state.errorMessage) {
                favoriteViewModel.getAllFavorites()
            }

            FavoriteState.EmptyList -> EmptyListView() {
                navController.navigate("home")
            }
        }
    }
}

@Composable
fun MainView(
    favorites: List<FavoriteEntity>,
    toggleFavorite: (FavoriteEntity) -> Unit,
    isFavorite: @Composable (Int) -> Boolean,
    onClickHandler: (Int) -> Unit
) {

    LazyColumn {
        items(favorites.chunked(2)) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()
            ) {
                it.forEach { product ->
                    ProductCard(
                        product.id,
                        product.title,
                        product.image,
                        rating = Rating(
                            count = product.ratingCount, rate = product.ratingRate
                        ),
                        product.price,
                        product.category,
                        isFavorite = isFavorite(product.id),
                        toggleFavorite = {
                            toggleFavorite(it)
                        },
                        onClickHandler
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyListView(goToHome: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
        )

        Text(text = "Favorite list is empty!", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))
        ElevatedButton(
            onClick = goToHome
        ) {
            Text("Go to home")
        }
    }
}