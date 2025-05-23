package com.example.httpdemo.ui.screen.nasa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.httpdemo.data.MoneyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import com.example.httpdemo.data.nasa.RoverPhotos
import com.example.httpdemo.network.NasaAPI
import com.example.httpdemo.ui.screen.money.MoneyUiState

sealed interface MarsUiState {
    data class Success(val rowerPhotsResult: RoverPhotos) : MarsUiState
    data class Error(val message: String) : MarsUiState
    object Loading : MarsUiState
}

@HiltViewModel
class NasaMarsViewModel @Inject constructor(
    val nasaAPI: NasaAPI
) : ViewModel() {

    /** The mutable State that stores the status of the most recent request */
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)

    init {
        getRoverPhotos("2015-6-3")
    }

    fun getRoverPhotos(date: String) {
        marsUiState = MarsUiState.Loading
        viewModelScope.launch {
            marsUiState = try {
                val result = nasaAPI.getRoverPhotos(
                    date,"XnhjqoSmjwxIxoxYrzE4xCQ0al7Tds0SCrCkCQ5s"
                )
                MarsUiState.Success(result)
            } catch (e: IOException) {
                MarsUiState.Error(e.message!!)
            } catch (e: HttpException) {
                MarsUiState.Error(e.message!!)
            }
        }
    }
}