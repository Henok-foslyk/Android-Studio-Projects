package com.example.shoppinglist.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.R
import kotlinx.coroutines.delay

@Composable
fun StartingScreen(onTimeout: () -> Unit) {
    // Simulate some work being done
    LaunchedEffect(key1 = true) {
        delay(3000)
        onTimeout()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(id = R.string.welcome))
    }
}