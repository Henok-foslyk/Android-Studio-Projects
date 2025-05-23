package com.example.weatherwiz.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.weatherwiz.data.CityItem

class MainViewModel : ViewModel() {
    private var cityList = mutableStateListOf<CityItem>()

    fun getAllCityList(): List<CityItem> {
        return cityList
    }

    fun addCityList(cityItem: CityItem) {
        cityList.add(cityItem)
    }

    fun removeCityItem(cityItem: CityItem) {
        cityList.remove(cityItem)
    }

    fun editCityItem(original: CityItem, edited: CityItem) {
        val index = cityList.indexOfFirst{ it.id == original.id }
        if (index != -1) {
            cityList[index] = edited
        }
    }

    fun clearAllCities() {
        cityList.clear()
    }
}