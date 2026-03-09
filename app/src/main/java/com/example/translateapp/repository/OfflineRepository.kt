package com.example.translateapp.repository

import android.content.Context
import androidx.room.Room
import com.example.translateapp.data.OfflineTranslator
import com.example.translateapp.data.TranslationDatabase
import com.example.translateapp.data.TranslationHistory
import kotlinx.coroutines.flow.Flow

class OfflineRepository(context: Context) {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        TranslationDatabase::class.java,
        TranslationDatabase.DATABASE_NAME
    ).build()
    
    private val offlineTranslator = OfflineTranslator()
    private val dao = database.translationHistoryDao()
    
    // 離線翻譯
    suspend fun translateOffline(text: String, targetLanguage: String): String {
        return offlineTranslator.translateOffline(text, targetLanguage).getOrNull() ?: ""
    }
    
    // 儲存翻譯歷史
    suspend fun saveTranslationHistory(
        sourceText: String,
        targetText: String,
        sourceLanguage: String,
        targetLanguage: String,
        isFromOffline: Boolean = false
    ) {
        val history = TranslationHistory(
            sourceText = sourceText,
            targetText = targetText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            isFromOffline = isFromOffline
        )
        dao.insert(history)
    }
    
    // 獲取翻譯歷史
    fun getTranslationHistory(): Flow<List<TranslationHistory>> {
        return dao.getAllHistories()
    }
    
    // 搜尋歷史記錄
    fun searchHistory(query: String): Flow<List<TranslationHistory>> {
        return dao.searchHistories(query)
    }
    
    // 刪除歷史記錄
    suspend fun deleteHistory(history: TranslationHistory) {
        dao.delete(history)
    }
    
    // 清空歷史記錄
    suspend fun clearHistory() {
        dao.deleteAll()
    }
    
    // 檢查是否支援離線翻譯
    fun isOfflineAvailable(): Boolean {
        return offlineTranslator.isOfflineAvailable()
    }
    
    // 獲取支援的離線語言對
    fun getSupportedLanguagePairs(): List<String> {
        return offlineTranslator.getSupportedLanguagePairs()
    }
}
