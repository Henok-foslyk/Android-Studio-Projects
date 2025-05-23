package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.example.shoppinglist.ui.navigation.SummaryScreenRoute
import com.example.shoppinglist.ui.navigation.ShoppingScreenRoute
import com.example.shoppinglist.ui.screen.SummaryScreen
import com.example.shoppinglist.ui.screen.ShoppingScreen
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shoppinglist.ui.screen.StartingScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var showStartingScreen by remember { mutableStateOf(true) }
                    if (showStartingScreen) {
                        StartingScreen(onTimeout = { showStartingScreen = false })
                    } else {
                        MainNavigation(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
            keepSplashScreen = false
        }
    }


}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = ShoppingScreenRoute
    )
    {
        composable<ShoppingScreenRoute> {
            ShoppingScreen(
                onInfoClicked = { allItems, foods, books, electronics ->
                    navController.navigate(
                        SummaryScreenRoute(
                            allItems, foods, books, electronics
                        )
                    )
                }
            )
        }
        composable<SummaryScreenRoute> {
            SummaryScreen(
                onBackClicked = {
                    navController.navigate(ShoppingScreenRoute)
                }
            )
        }
    }
}