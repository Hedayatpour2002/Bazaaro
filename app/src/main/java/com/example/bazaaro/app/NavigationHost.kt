package com.example.bazaaro.app

import android.content.Context
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bazaaro.R
import com.example.bazaaro.presentation.address.AddressScreen
import com.example.bazaaro.presentation.cart.CartScreen
import com.example.bazaaro.presentation.category.CategoryScreen
import com.example.bazaaro.presentation.detail.DetailScreen
import com.example.bazaaro.presentation.favorite.FavoriteScreen
import com.example.bazaaro.presentation.home.HomeScreen
import com.example.bazaaro.presentation.order.OrderScreen
import com.example.bazaaro.presentation.personalInformation.PersonalInformationScreen
import com.example.bazaaro.presentation.profile.ProfileScreen

const val DETAIL_SCREEN_ROUTE = "detail"

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
        composable(
            route = "$DETAIL_SCREEN_ROUTE/{productId}",
            arguments = listOf(navArgument(name = "productId") {
                type = NavType.IntType
            })
        ) {
            val productId = it.arguments?.getInt("productId") ?: 0
            DetailScreen(navController = navController, productId = productId)
        }

        composable(route = "personal-information") { PersonalInformationScreen(navController = navController) }
        composable(route = "order") { OrderScreen(navController = navController) }
        composable(route = "favorite") { FavoriteScreen(navController = navController) }
        composable(route = "address") { AddressScreen(navController = navController) }
    }
}

enum class TopLevelDestinations(val route: String, @RawRes val icon: Int) {
    HOME(route = "home", icon = R.raw.ic_home),
    CATEGORY(route = "category", icon = R.raw.ic_category),
    CART(route = "cart", icon = R.raw.ic_cart),
    PROFILE(route = "profile", icon = R.raw.ic_profile);

    fun getTitle(context: Context): String {
        return when (this) {
            HOME -> context.getString(R.string.home)
            CATEGORY -> context.getString(R.string.category)
            CART -> context.getString(R.string.cart)
            PROFILE -> context.getString(R.string.profile)
        }
    }
}
