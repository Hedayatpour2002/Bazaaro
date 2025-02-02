package com.example.bazaaro.app

import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val items = TopLevelDestinations.entries
        val context = LocalContext.current
        items.forEach {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(it.icon))
            var isPlaying by remember { mutableStateOf(true) }
            val progress by animateLottieCompositionAsState(
                composition = composition, isPlaying = isPlaying
            )

            LaunchedEffect(progress) {
                if (progress == 0f) {
                    isPlaying = true
                }
                if (progress == 1f) {
                    isPlaying = false
                }
            }

            NavigationBarItem(
                icon = {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.size(28.dp)
                    )
                },
                label = { Text(it.getTitle(context = context), fontWeight = FontWeight.ExtraBold) },

                selected = navController.currentBackStackEntryAsState().value?.destination?.route == it.route,
                onClick = {
                    navController.navigate(route = it.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    isPlaying = true
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Gray
                ),
            )
        }
    }
}