package com.example.highlowgame

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
import androidx.navigation.navOptions
import com.example.highlowgame.ui.navigation.GameScreen
import com.example.highlowgame.ui.navigation.MainMenuScreen
import com.example.highlowgame.ui.screen.DemoTime
import com.example.highlowgame.ui.theme.HighLowGameTheme
import com.example.highlowgame.ui.screen.GameScreen
import com.example.highlowgame.ui.screen.MainMenuScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HighLowGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    DemoTime()
                }
            }
        }
    }
}


@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainMenuScreen
    ) {
        composable<MainMenuScreen> {
            MainMenuScreen(
                onStartClick = {
                    navController.navigate(GameScreen(100))
                }
            )
        }
        composable<GameScreen> {
            GameScreen()
        }
    }
}