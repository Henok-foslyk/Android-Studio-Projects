package com.example.shoppinglist.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object ShoppingScreenRoute

@Serializable
data class SummaryScreenRoute(
    val allItems: Int,
    val FoodItems: Int,
    val BookItems: Int,
    val ElectronicItems: Int
)