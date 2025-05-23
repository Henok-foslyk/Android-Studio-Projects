package com.example.weatherwiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherwiz.navigation.MainScreenRoute
import com.example.weatherwiz.navigation.WeatherScreenRoute
import com.example.weatherwiz.ui.screen.MainScreen
import com.example.weatherwiz.ui.screen.WeatherScreen
import com.example.weatherwiz.ui.theme.WeatherWizTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherWizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainScreenRoute
    )
    {
        composable<MainScreenRoute> {
            MainScreen(
                onAPISelected = { cityName ->
                    navController.navigate(WeatherScreenRoute(cityName))
                }
            )
        }
        composable<WeatherScreenRoute> {
            WeatherScreen()
        }

    }
}