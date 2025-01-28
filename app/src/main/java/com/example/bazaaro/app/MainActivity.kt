package com.example.bazaaro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
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

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomNavigationBar(navController = navController)
                }) { innerPadding ->
                    NavigationHost(
                        navController = navController, modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
