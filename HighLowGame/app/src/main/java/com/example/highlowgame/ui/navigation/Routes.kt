package com.example.highlowgame.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object MainMenuScreen

@Serializable
data class GameScreen(val upperBound: Int)