package com.example.bazaaro.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bazaaro.app.DETAIL_SCREEN_ROUTE
import com.example.bazaaro.app.ui.components.ErrorView
import com.example.bazaaro.app.ui.components.LoadingView
import com.example.bazaaro.app.ui.components.ProductCard
import com.example.bazaaro.data.model.Product

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {

    val homeState = homeViewModel.homeState.collectAsStateWithLifecycle()

    when (val state = homeState.value) {
        is HomeState.Error -> ErrorView(errorMessage = state.errorMessage, onClick = {
            homeViewModel.getAllProducts()
        })

        HomeState.Loading -> LoadingView()
        is HomeState.Success -> MainView(state.productList, onRefresh = {
            homeViewModel.getAllProducts()

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
    clickHandler: (Int) -> Unit
) {

    if (productList.isEmpty()) {
        Text(text = "Nothing to show")
    } else {

        PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh) {
            LazyColumn {
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
                                clickHandler
                            )
                        }
                    }
                }
            }
        }
    }
}




