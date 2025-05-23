package com.example.shoppinglist.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.shoppinglist.ui.navigation.SummaryScreenRoute

class SummaryViewModel(
    savedStateHandle: SavedStateHandle) : ViewModel() {

    var allItems by mutableStateOf(0)
    var FoodItems by mutableStateOf(0)
    var ElectronicItems by mutableStateOf(0)
    var BookItems by mutableStateOf(0)


    init {
        allItems = savedStateHandle.toRoute<SummaryScreenRoute>().allItems
        FoodItems = savedStateHandle.toRoute<SummaryScreenRoute>().FoodItems
        BookItems = savedStateHandle.toRoute<SummaryScreenRoute>().BookItems
        ElectronicItems = savedStateHandle.toRoute<SummaryScreenRoute>().ElectronicItems

    }
}