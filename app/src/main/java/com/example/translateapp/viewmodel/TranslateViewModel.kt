package com.example.translateapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translateapp.repository.TranslateRepository
import com.example.translateapp.repository.OfflineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TranslateState(
    val inputText: String = "",
    val resultText: String = "",
    val sourceLanguage: String = "",
    val targetLanguage: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isTranslationSuccessful: Boolean = false,
    val historyList: List<com.example.translateapp.data.TranslationHistory> = emptyList(),
    val isOfflineMode: Boolean = false
)

class TranslateViewModel(
    private val offlineRepository: OfflineRepository? = null
) : ViewModel() {
    private val repository = TranslateRepository()
    
    private val _state = MutableStateFlow(TranslateState())
    val state: StateFlow<TranslateState> = _state

    fun setInputText(text: String) {
        _state.value = _state.value.copy(inputText = text)
    }

    fun setResultText(text: String) {
        _state.value = _state.value.copy(resultText = text)
    }

    fun setSourceLanguage(language: String) {
        _state.value = _state.value.copy(sourceLanguage = language)
    }

    fun setTargetLanguage(language: String) {
        _state.value = _state.value.copy(targetLanguage = language)
    }

    fun setIsOfflineMode(isOffline: Boolean) {
        _state.value = _state.value.copy(isOfflineMode = isOffline)
    }

    fun clear() {
        _state.value = TranslateState()
    }

    fun translate() {
        val currentState = _state.value
        val text = currentState.inputText
        val targetLanguage = currentState.targetLanguage
        
        if (text.isEmpty()) {
            _state.value = currentState.copy(errorMessage = "請輸入要翻譯的文字")
            return
        }
        
        if (targetLanguage.isEmpty()) {
            _state.value = currentState.copy(errorMessage = "請選擇目標語言")
            return
        }
        
        _state.value = currentState.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            try {
                val result = if (currentState.isOfflineMode && offlineRepository != null) {
                    // 離線翻譯
                    val offlineResult = offlineRepository.translateOffline(text, targetLanguage)
                    // 儲存到歷史記錄
                    offlineRepository.saveTranslationHistory(
                        text,
                        result,
                        currentState.sourceLanguage,
                        targetLanguage,
                        isFromOffline = true
                    )
                    offlineResult
                } else {
                    // 線上翻譯
                    val result = repository.translateText(
                        text,
                        targetLanguage,
                        if (currentState.sourceLanguage.isEmpty()) null else currentState.sourceLanguage
                    )
                    // 儲存到歷史記錄
                    offlineRepository?.saveTranslationHistory(
                        text,
                        result,
                        currentState.sourceLanguage,
                        targetLanguage,
                        isFromOffline = false
                    )
                    result
                }
                
                _state.value = currentState.copy(
                    resultText = result,
                    isLoading = false,
                    isTranslationSuccessful = true,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "翻譯失敗"
                )
            }
        }
    }

    fun translateAndSave() {
        val currentState = _state.value
        val text = currentState.inputText
        val result = currentState.resultText
        val sourceLanguage = currentState.sourceLanguage
        val targetLanguage = currentState.targetLanguage
        
        if (text.isEmpty() || result.isEmpty()) {
            return
        }
        
        viewModelScope.launch {
            offlineRepository?.saveTranslationHistory(
                text,
                result,
                sourceLanguage,
                targetLanguage,
                currentState.isOfflineMode
            )
        }
    }

    fun swapLanguages() {
        val currentState = _state.value
        _state.value = currentState.copy(
            sourceLanguage = currentState.targetLanguage,
            targetLanguage = currentState.sourceLanguage
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }

    fun loadHistory() {
        viewModelScope.launch {
            offlineRepository?.getTranslationHistory()?.collect { histories ->
                _state.value = _state.value.copy(historyList = histories)
            }
        }
    }

    fun deleteHistoryItem(history: com.example.translateapp.data.TranslationHistory) {
        viewModelScope.launch {
            offlineRepository?.deleteHistory(history)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            offlineRepository?.clearHistory()
        }
    }
}
