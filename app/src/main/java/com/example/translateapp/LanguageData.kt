package com.example.translateapp

data class Language(
    val name: String,
    val code: String
)

object LanguageData {
    val languages = listOf(
        Language("自動偵測", ""),
        Language("中文 (繁體)", "zh-TW"),
        Language("英文", "en"),
        Language("日文", "ja"),
        Language("韓文", "ko"),
        Language("西班牙文", "es"),
        Language("法文", "fr"),
        Language("德文", "de"),
        Language("越南文", "vi"),
        Language("印尼文", "id"),
        Language("泰文", "th"),
        Language("俄羅斯語", "ru")
    )

    fun getLanguageCode(name: String): String {
        return languages.find { it.name == name }?.code ?: ""
    }

    fun getLanguageName(code: String): String {
        return languages.find { it.code == code }?.name ?: code
    }

    fun getLanguageIndex(code: String): Int {
        return languages.indexOfFirst { it.code == code }
    }

    fun getDefaultSourceIndex(): Int {
        return 0 // 自動偵測
    }

    fun getDefaultTargetIndex(): Int {
        return 1 // 英文
    }
}
