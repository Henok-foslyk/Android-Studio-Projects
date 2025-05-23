package com.example.httpdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.example.httpdemo.navigation.MainScreenRoute
import com.example.httpdemo.navigation.MoneyScreenRoute
import com.example.httpdemo.navigation.NasaScreenRoute
import com.example.httpdemo.ui.screen.MainScreen
import com.example.httpdemo.ui.screen.money.MoneyScreen
import com.example.httpdemo.ui.screen.nasa.NasaMarsScreen
import com.example.httpdemo.ui.theme.HTTPDemoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HTTPDemoTheme {
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
                onMoneyAPISelected = {
                    navController.navigate(MoneyScreenRoute)
                },
                onNasaMarsAPISelected = {
                    navController.navigate(NasaScreenRoute)
                }
            )
        }
        composable<MoneyScreenRoute> {
            MoneyScreen()
        }
        composable<NasaScreenRoute> {
            NasaMarsScreen()
        }
    }
}