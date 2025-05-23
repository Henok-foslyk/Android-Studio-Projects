package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.example.todolist.ui.navigation.SummaryScreenRoute
import com.example.todolist.ui.navigation.TodoScreenRoute
import com.example.todolist.ui.screen.SummaryScreen
import com.example.todolist.ui.screen.TodoScreen
import com.example.todolist.ui.theme.ToDoListTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            ToDoListTheme {
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
    navController: NavHostController = rememberNavController()
)
{
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = TodoScreenRoute
    )
    {
        composable<TodoScreenRoute> {
            TodoScreen(
                onInfoClicked = {
                        allTodo, importantTodo ->
                    navController.navigate(SummaryScreenRoute(
                        allTodo,importantTodo
                    ))
                }
            )
        }
        composable<SummaryScreenRoute> {
            SummaryScreen()
        }
    }
}