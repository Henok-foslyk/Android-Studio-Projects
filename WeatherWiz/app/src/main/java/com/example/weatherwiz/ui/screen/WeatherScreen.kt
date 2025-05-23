package com.example.weatherwiz.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weatherwiz.data.WeatherData

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()

) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (viewModel.weatherUiState) {
            is WeatherUiState.Init -> {
                Button(
                    onClick = {
                        viewModel.getWeather(viewModel.cityName)
                    }
                ) {
                    Text("Refresh")
                }
                Text("Press refresh to get most recent weather status.")
            }
            is WeatherUiState.Loading -> CircularProgressIndicator()
            is WeatherUiState.Success -> WeatherDataWidget(
                (viewModel.weatherUiState as WeatherUiState.Success).weatherData)
            is WeatherUiState.Error -> Text((viewModel.weatherUiState as WeatherUiState.Error).message)
        }
    }
}


@Composable
fun WeatherDataWidget(weatherData: WeatherData) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${weatherData.name} Weather",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        NetworkImage("https://openweathermap.org/img/wn/${weatherData.weather?.firstOrNull()?.icon}@2x.png")
        weatherData.weather?.firstOrNull()?.let { weather ->
            WeatherDetail(title = "Weather", description = weather.main ?: "")
            WeatherDetail(title = "Description", description = weather.description ?: "")
            WeatherDetail(title = "Coordinates", description = "${weatherData.coord?.lon}, ${weatherData.coord?.lat}")
            WeatherDetail(title = "Temperature", description = "${weatherData.main?.temp}°C")
            WeatherDetail(title = "Humidity", description = "${weatherData.main?.humidity}%")
            WeatherDetail(title = "Minimum Temperature", description = "${weatherData.main?.tempMin}°C")
            WeatherDetail(title = "Maximum Temperature", description = "${weatherData.main?.tempMax}°C")

        }
    }
}

@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = "Image from URL",
        modifier = modifier.size(256.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun WeatherDetail(title: String, description: String) {
    Column(
        modifier = Modifier.padding(top = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}