package com.example.weatherwiz.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.weatherwiz.data.WeatherData
import com.example.weatherwiz.navigation.WeatherScreenRoute
import com.example.weatherwiz.network.WeatherAPI
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val weatherAPI: WeatherAPI,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Init)
    var cityName : String = checkNotNull(savedStateHandle["cityName"])

    init {
        getWeather(cityName)
    }

    fun getWeather(cityName: String) {
        weatherUiState = WeatherUiState.Loading
        viewModelScope.launch {
            weatherUiState = try {
                val result = weatherAPI.getWeather("${cityName}",
                                                   "metric",
                                                   "77233674d06b05e240fd758f8526d850")

                WeatherUiState.Success(result)
            } catch (e: IOException) {
                WeatherUiState.Error(e.message!!)
            } catch (e: HttpException) {
                WeatherUiState.Error(e.message!!)
            } catch (e: Exception) {
                WeatherUiState.Error(e.message!!)
            }
        }
    }
}

sealed interface WeatherUiState {
    object Init : WeatherUiState
    data class Success(val weatherData: WeatherData) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
    object Loading : WeatherUiState
}