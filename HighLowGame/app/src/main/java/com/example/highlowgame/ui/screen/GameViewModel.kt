package com.example.highlowgame.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.highlowgame.ui.navigation.GameScreen

class GameViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var generateNumber = mutableStateOf(0)
    var counter = mutableStateOf(0)
    var upperBound = 0

    init {
        generateRandomNumber(upperBound)
        val upperBound = savedStateHandle.toRoute<GameScreen>().upperBound
    }

    fun increaseCounter(){
        counter.value++
    }

    fun reset(){
        counter.value = 0
        generateRandomNumber(upperBound)
    }

    fun generateRandomNumber(upperBound: Int) {
        generateNumber.value = (0 .. upperBound).random()
    }
}