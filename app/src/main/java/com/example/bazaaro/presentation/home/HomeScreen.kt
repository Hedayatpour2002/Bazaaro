package com.example.bazaaro.presentation.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bazaaro.app.ui.components.ErrorView
import com.example.bazaaro.app.ui.components.LoadingView
import com.example.bazaaro.data.model.Product

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel(), navController: NavHostController) {

    val homeState = homeViewModel.homeState.collectAsStateWithLifecycle()
    when (val state = homeState.value) {
        is HomeState.Error -> ErrorView(errorMessage = state.errorMessage, onClick = {
            homeViewModel.getAllProducts()
        })

        HomeState.Loading -> LoadingView()
        is HomeState.Success -> MainView(state.productList)
    }
}

@Composable
private fun MainView(productList: List<Product>) {
    Text("home")
}




