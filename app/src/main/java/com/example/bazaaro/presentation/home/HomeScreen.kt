package com.example.bazaaro.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
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
import com.example.bazaaro.data.model.Product

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(), navController: NavHostController
) {

    val homeState = homeViewModel.homeState.collectAsStateWithLifecycle()


    when (val state = homeState.value) {
        is HomeState.Error -> ErrorView(errorMessage = state.errorMessage, onClick = {
            homeViewModel.getAllProducts()
        })

        HomeState.Loading -> LoadingView()
        is HomeState.Success -> MainView(state.productList, onRefresh = {
            homeViewModel.getAllProducts()
        }, goToFavorite = {
            navController.navigate("favorite")
        }, isFavorite = {
            val isFavorite by homeViewModel.isFavorite(it).collectAsStateWithLifecycle(false)

            isFavorite
        }, toggleFavorite = {
            homeViewModel.toggleFavorite(it)
        }) {
            navController.navigate("$DETAIL_SCREEN_ROUTE/$it")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainView(
    productList: List<Product>,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit,
    goToFavorite: () -> Unit,
    isFavorite: @Composable (Int) -> Boolean,
    toggleFavorite: (FavoriteEntity) -> Unit,
    clickHandler: (Int) -> Unit
) {

    if (productList.isEmpty()) {
        Text(text = stringResource(R.string.nothingToShow))
    } else {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.favorite))

        PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh) {
            LazyColumn {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {

                            Text(
                                text = stringResource(R.string.B),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = Color(0xFFDB3022),
                                fontFamily = FontFamily.Serif
                            )
                            Text(text = stringResource(R.string.azaaro), fontFamily = FontFamily.Monospace)

                        }

                        LottieAnimation(composition = composition,
                            isPlaying = true,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    goToFavorite()
                                })
                    }
                }
                items(productList.chunked(2)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        it.forEach { product ->
                            ProductCard(
                                product.id,
                                product.title,
                                product.image,
                                product.rating,
                                product.price,
                                product.category,
                                isFavorite = isFavorite(product.id),
                                toggleFavorite = toggleFavorite,
                                clickHandler
                            )
                        }
                    }
                }
            }
        }
    }
}




