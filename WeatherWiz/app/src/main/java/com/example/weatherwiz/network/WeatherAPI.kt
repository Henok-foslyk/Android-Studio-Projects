package com.example.weatherwiz.network

import com.example.weatherwiz.data.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.openweathermap.org/data/2.5/weather?q=Budapest,hu&units=metric&appid=f3d694bc3e1d44c1ed5a97bd1120e8fe

// HOST: https://api.openweathermap.org/
// PATH: data/2.5/weather
// QUERY params: ?q=Budapest&units=metric&appid=77233674d06b05e240fd758f8526d850

interface WeatherAPI {

    @GET("data/2.5/weather")
    suspend fun getWeather(@Query("q") city: String,
                           @Query("units") units: String,
                           @Query("appid") apiKey: String): WeatherData
}