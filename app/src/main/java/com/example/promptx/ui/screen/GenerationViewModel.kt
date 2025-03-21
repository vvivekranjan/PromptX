package com.example.promptx.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.promptx.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Data class to represent the UI state of the GenerationScreen
data class GenerationUiState(
    val inputText: String = "",
    val generatedPrompt: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPromptCopied: Boolean = false,
    val isInputTextEmpty: Boolean = true,
    val isGenerateButtonEnabled: Boolean = false
)

class GenerationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GenerationUiState())
    val uiState: StateFlow<GenerationUiState> = _uiState.asStateFlow()

    private val config = generationConfig {
        temperature = 0.7f // More creative and varied responses
        maxOutputTokens = 200
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.apiKey,
        generationConfig = config
    )

    // Function to update the input text
    fun updateInputText(newInputText: String) {
        _uiState.update {
            it.copy(
                inputText = newInputText,
                isInputTextEmpty = newInputText.isEmpty(),
                isGenerateButtonEnabled = newInputText.isNotEmpty()
            )
        }
    }

    // Function to generate a prompt using Gemini API
    fun generatePrompt(selectedOptions: Map<String, String> = emptyMap()) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null, isPromptCopied = false) }

            try {
                // Build the enhanced prompt
                val enhancedPrompt = buildPromptGenerationInstructions(_uiState.value.inputText, selectedOptions)

                Log.d("GenerationViewModel", "Sending request with prompt: $enhancedPrompt")

                // Generate content using the enhanced prompt
                val response = generativeModel.generateContent(
                    content {
                        text(enhancedPrompt)
                    }
                ) // Removed `content { text(...) }`

                Log.d("GenerationViewModel", "Response received: ${response.text}")

                val newPrompt = response.text ?: "No response received."

                _uiState.update {
                    it.copy(
                        generatedPrompt = newPrompt,
                        isLoading = false,
                        isPromptCopied = false
                    )
                }
            } catch (e: Exception) {
                Log.e("GenerationViewModel", "Error generating prompt", e)
                _uiState.update { it.copy(error = "Failed to generate prompt: ${e.message}", isLoading = false) }
            }
        }
    }

    fun copyPrompt() {
        _uiState.update { it.copy(isPromptCopied = true) }
    }

    fun clearGeneratedPrompt() {
        _uiState.update { it.copy(generatedPrompt = "", isPromptCopied = false) }
    }

    fun clearInputText() {
        _uiState.update { it.copy(inputText = "", isInputTextEmpty = true, isGenerateButtonEnabled = false) }
    }
}
