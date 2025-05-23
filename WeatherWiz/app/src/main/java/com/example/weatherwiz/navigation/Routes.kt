package com.example.weatherwiz.navigation

import kotlinx.serialization.Serializable

@Serializable
object MainScreenRoute

@Serializable
data class WeatherScreenRoute(
    val cityName: String
)
