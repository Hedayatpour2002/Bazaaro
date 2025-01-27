package com.example.bazaaro.app

import androidx.annotation.RawRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bazaaro.R
import com.example.bazaaro.presentation.cart.CartScreen
import com.example.bazaaro.presentation.category.CategoryScreen
import com.example.bazaaro.presentation.home.HomeScreen
import com.example.bazaaro.presentation.profile.ProfileScreen

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = TopLevelDestinations.HOME.route,
        modifier = modifier
    ) {
        composable(TopLevelDestinations.HOME.route) { HomeScreen(navController = navController) }
        composable(TopLevelDestinations.CATEGORY.route) { CategoryScreen(navController = navController) }
        composable(TopLevelDestinations.CART.route) { CartScreen(navController = navController) }
        composable(TopLevelDestinations.PROFILE.route) { ProfileScreen(navController = navController) }
    }
}

enum class TopLevelDestinations(val route: String, val title: String, @RawRes val icon: Int) {
    HOME(route = "home", title = "Home", icon = R.raw.ic_home), CATEGORY(
        route = "category", title = "Category", icon = R.raw.ic_category
    ),
    CART(
        route = "cart", title = "Cart", icon = R.raw.ic_cart
    ),
    PROFILE(
        route = "profile", title = "Profile", icon = R.raw.ic_profile
    )
}