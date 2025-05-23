package com.example.weatherwiz.data

data class CityItem(
    var name:String,
    val addDate:String,
    val id: Int = generateUniqueId()
) {
    companion object {
        private var nextId = 1
        private fun generateUniqueId(): Int {
            return nextId++
        }
    }
}