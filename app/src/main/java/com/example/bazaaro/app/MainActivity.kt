package com.example.bazaaro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bazaaro.ui.theme.BazaaroTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BazaaroTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val showBottomBar = TopLevelDestinations.entries.map { it.route }
                    .contains(navBackStackEntry?.destination?.route)
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    AnimatedVisibility(
                        visible = showBottomBar, enter = slideInVertically(
                            initialOffsetY = { it },
                        ) + fadeIn(), exit = slideOutVertically(
                            targetOffsetY = { it },
                        ) + fadeOut()
                    ) {
                        BottomNavigationBar(navController = navController)
                    }
                }) { innerPadding ->
                    NavigationHost(
                        navController = navController, modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
