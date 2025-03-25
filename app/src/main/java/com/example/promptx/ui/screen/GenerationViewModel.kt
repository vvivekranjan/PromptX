package com.example.promptx.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.withTimeout

// Data class to represent the UI state of the GenerationScreen
data class GenerationUiState(
    val inputText: String = "",
    val generatedPrompt: String = "",
    val selectedOptions: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPromptCopied: Boolean = false,
    val isInputTextEmpty: Boolean = true,
    val isGenerateButtonEnabled: Boolean = false,
    val isStreaming: Boolean = false,
    val feedbackGiven: Boolean = false
)

class GenerationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GenerationUiState())
    val uiState: StateFlow<GenerationUiState> = _uiState.asStateFlow()

    private val config = generationConfig {
        temperature = 0.75f // More creative and varied responses
        maxOutputTokens =
            2000 // Max token limit per response, increase if needed, but keep within API limits
        topP = 0.95f // Controls randomness (higher = more diverse output)
        topK = 50 // Reduces irrelevant responses
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.apiKey,
        generationConfig = config
    )

    // Function to update the input text
    fun updateInputText(newInputText: String, selectedOptions: Map<String, String>) {
        _uiState.update {
            it.copy(
                inputText = newInputText,
                isInputTextEmpty = newInputText.isEmpty(),
                isGenerateButtonEnabled = newInputText.isNotEmpty(),
                selectedOptions = selectedOptions
            )
        }
    }

    // Function to generate a prompt using Gemini API
    fun generatePrompt(selectedOptions: Map<String, String> = emptyMap()) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    isPromptCopied = false,
                    generatedPrompt = "",
                    isStreaming = true
                )
            }

            var newResponse = ""

            try {
                // Build the enhanced prompt
                val enhancedPrompt =
                    buildPromptGenerationInstructions(_uiState.value.inputText, selectedOptions)

                Log.d("GenerationViewModel", "Prompt Sent: ${enhancedPrompt.take(200)}...")

                // Generate content using the enhanced prompt
                val responseFlow = generativeModel.generateContentStream(
                    content {
                        text(enhancedPrompt)
                    }
                )

                responseFlow.collect { response ->
                    val newToken = response.text
                        ?: "AI couldn't generate a response. Try again with fewer customizations."

                    newResponse += newToken

                }

                _uiState.update {
                    it.copy(
                        generatedPrompt = newResponse,
                        isLoading = false,
                        isPromptCopied = false,
                        isStreaming = false
                    )
                }
            } catch (e: Exception) {
                Log.e("GenerationViewModel", "Error generating prompt", e)
                _uiState.update {
                    it.copy(
                        error = "Failed to generate prompt: ${e.message}",
                        isLoading = false,
                        isStreaming = false
                    )
                }
            }
        }
    }

    fun copyPrompt() {
        _uiState.update { it.copy(isPromptCopied = true) }
    }

    fun sendPositiveFeedback() {
        viewModelScope.launch {
            _uiState.update { it.copy(feedbackGiven = true) }
            Log.d("GenerationViewModel", "User liked the response")
        }
    }

    fun sendNegativeFeedback() {
        viewModelScope.launch {
            _uiState.update { it.copy(feedbackGiven = true, generatedPrompt = "", isLoading = true) }
            Log.d("GenerationViewModel", "User disliked the response, regenerating...")

            generatePrompt(_uiState.value.selectedOptions) // Regenerate response
        }
    }

    fun clearGeneratedPrompt() {
        _uiState.update { it.copy(generatedPrompt = "", isPromptCopied = false) }
    }

    fun clearInputText() {
        _uiState.update {
            it.copy(
                inputText = "",
                isInputTextEmpty = true,
                isGenerateButtonEnabled = false,
                selectedOptions = emptyMap()
            )
        }
    }
}
