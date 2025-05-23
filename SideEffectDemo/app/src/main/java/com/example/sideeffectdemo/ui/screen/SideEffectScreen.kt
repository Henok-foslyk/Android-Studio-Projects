package com.example.sideeffectdemo.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun SideEffectScreens() {

    var counter by remember { mutableStateOf(0) }
    LaunchedEffect(key1 = counter) {
        Log.d("TAG_SIDE", "Launched Effect Called")
    }

    SideEffect {
        Log.d("TAG_SIDE", "SideEffect Called")
    }

    DisposableEffect(key1 = counter) {
        Log.d("TAG_SIDE", "Launch in DisposableEffect Called")
        onDispose {
            Log.d("TAG_SIDE", "DisposableEffect onDispose Called")
        }
    }
    Column {
        Text("SIDEEFFECT Demo")
        Button(onClick = {
            counter++
        }){
            Text("Increase - state change")
        }
    }
}

@Composable
fun SideEffectScreen(){
    var counter by remember { mutableStateOf(0) }
    Button(
        onClick = {counter++}
    ) {
        Text("Press")
    }
    Button(
        onClick = {counter++}
    ) {
        Text("Press")
    }
    Row() {
        Button(
            onClick = {counter++}
        ) {
            Text("Press")
        }
        Button(
            onClick = {counter++}
        ) {
            Text("Press")
        }
    }
}