package com.example.aidemo.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GenAIViewModel: ViewModel() {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = "AIzaSyBy0-P9AHkHOgmmdFQ57kzQKilGXuKDZ1I"
    )

    private val _textGenerationResult = MutableStateFlow<String?>(null)
    val textGenerationResult = _textGenerationResult.asStateFlow()

    fun generateStory(userPrompt: String = "Tell me a joke") {
        _textGenerationResult.value = "Generating..."
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var systemPrompt = "You are a helpful assistant. Who answers always in 1 sentence."

                var finalPrompt = "$systemPrompt" +
                        "Return only one answer, do not generate any other description or text." +
                        "$userPrompt"

                val result = generativeModel.generateContent(
                    finalPrompt)

                _textGenerationResult.value = result.text
            } catch (e: Exception) {
                _textGenerationResult.value = "Error: ${e.message}"
            }
        }
    }
}