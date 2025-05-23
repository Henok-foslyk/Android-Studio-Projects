package com.example.todolist.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object TodoScreenRoute

@Serializable
data class SummaryScreenRoute(
    val allTodos: Int,
    val importantTodos: Int
)