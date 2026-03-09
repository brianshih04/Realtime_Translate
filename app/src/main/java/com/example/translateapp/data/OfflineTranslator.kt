package com.example.translateapp.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 離線翻譯器 - 使用簡單的詞典匹配和規則進行離線翻譯
 * 在實際應用中，可以整合 ML Kit 或其他離線翻譯引擎
 */
class OfflineTranslator {
    private val TAG = "OfflineTranslator"
    
    // 簡單的詞典示例
    private val dictionary = mapOf(
        // 中文到英文
        "你好" to "Hello",
        "謝謝" to "Thank you",
        "早安" to "Good morning",
        "晚安" to "Good night",
        "吃飯了嗎" to "Have you eaten?",
        "我愛你" to "I love you",
        "請" to "Please",
        "不好意思" to "Excuse me",
        "再見" to "Goodbye",
        "多少" to "How much",
        "熱" to "Hot",
        "冷" to "Cold",
        
        // 英文到中文
        "Hello" to "你好",
        "Thank you" to "謝謝",
        "Good morning" to "早安",
        "Good night" to "晚安",
        "Have you eaten?" to "吃飯了嗎",
        "I love you" to "我愛你",
        "Please" to "請",
        "Excuse me" to "不好意思",
        "Goodbye" to "再見",
        "How much" to "多少",
        "Hot" to "熱",
        "Cold" to "冷",
        
        // 日文到中文
        "こんにちは" to "你好",
        "ありがとう" to "謝謝",
        "おはよう" to "早安",
        "こんばんは" to "晚安",
        "お腹 empty" to "吃飯了嗎",
        "大丈夫" to "沒事",
        
        // 韓文到中文
        "안녕하세요" to "你好",
        "감사합니다" to "謝謝",
        "좋은 아침" to "早安",
        "잘 자" to "晚安",
        "배고파요" to "吃飯了嗎",
        "괜찮아요" to "沒事"
    )
    
    /**
     * 離線翻譯文字
     */
    suspend fun translateOffline(
        text: String,
        targetLanguage: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                try {
                    Log.d(TAG, "離線翻譯: $text, 目標語言: $targetLanguage")
                    
                    if (text.isEmpty()) {
                        continuation.resume("")
                        return@suspendCoroutine
                    }
                    
                    // 簡單的離線翻譯實作
                    // 1. 檢查詞典中是否有直接翻譯
                    val lowerText = text.lowercase()
                    val directTranslation = dictionary[text] ?: dictionary[lowerText]
                    
                    if (directTranslation != null) {
                        continuation.resume(directTranslation)
                        return@suspendCoroutine
                    }
                    
                    // 2. 如果沒有直接翻譯，返回標記表示使用線上翻譯
                    Log.w(TAG, "詞典中未找到翻譯，請使用線上翻譯")
                    continuation.resume("請使用線上翻譯功能獲得更準確的翻譯結果")
                    
                } catch (e: Exception) {
                    Log.e(TAG, "離線翻譯異常: ${e.message}", e)
                    continuation.resumeWithException(e)
                }
            }
        }
    }
    
    /**
     * 檢查是否支援離線翻譯
     */
    fun isOfflineAvailable(): Boolean {
        // 檢查是否有足夠的詞典數據
        return dictionary.isNotEmpty()
    }
    
    /**
     * 獲取支援的離線語言對
     */
    fun getSupportedLanguagePairs(): List<String> {
        return listOf(
            "中文 - 英文",
            "英文 - 中文",
            "日文 - 中文",
            "韓文 - 中文"
        )
    }
}
