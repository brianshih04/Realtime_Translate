# Android 即時翻譯 App

這是一個 Android 即時翻譯應用程式，支援語音輸入和多語言切換功能。

## 功能特色

- 🌐 **多語言支援**：支援中文、英文、日文、韓文、西班牙文、法文、德文、越南文、印尼文、泰文等語言
- 🎤 **語音輸入**：支援語音轉文字功能，直接錄音即可翻譯
- 🔀 **快速切換**：一鍵交換來源與目標語言
- 💾 **快速複製**：一鍵複製翻譯結果到剪貼簿

## 前置作業

### 需求

- Android Studio Electric Eel 或更新版本
- Android SDK 34 (Android 14)
- Kotlin 1.9.24
- Gradle 8.2.0

### Google Cloud Translation API 設定

1. 前往 [Google Cloud Console](https://console.cloud.google.com/)
2. 建立新專案或選擇現有專案
3. 啟用 [Google Cloud Translation API](https://console.cloud.google.com/apis/library/translate.googleapis.com)
4. 建立 API 金鑰
5. 將 API 金鑰新增到 `app/build.gradle`：

```gradle
android {
    // ...
    defaultConfig {
        // ...
        buildConfigField "String", "TRANSLATE_API_KEY", "\"your-api-key-here\""
    }
}
```

## 專案結構

```
AndroidTranslateApp/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/translateapp/
│   │       │   ├── TranslateApplication.kt
│   │       │   ├── LanguageData.kt
│   │       │   ├── MainActivity.kt
│   │       │   ├── TutorialActivity.kt
│   │       │   ├── repository/
│   │       │   │   └── TranslateRepository.kt
│   │       │   └── viewmodel/
│   │       │       └── TranslateViewModel.kt
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   ├── values/
│   │       │   └── xml/
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## 使用說明

1. **啟動應用程式**：首次啟動會顯示新手入門指導
2. **語音輸入**：點擊語音輸入按鈕，允許錄音後開始說話
3. **選擇語言**：從下拉選單選擇來源語言和目標語言
4. **翻譯文字**：點擊「翻譯」按鈕
5. **操作翻譯結果**：可複製結果或交換語言

## 權限

- `RECORD_AUDIO`：語音輸入功能
- `INTERNET`：網路連線以進行翻譯

## 專業建議

1. **API 金鑰安全**：在實際部署時，應使用 Firebase Remote Config 或後端伺服器來保護 API 金鑰
2. **錯誤處理**：應用程式已包含完整的錯誤處理機制
3. **效能優化**：使用 Coroutines 處理非同步操作，避免阻塞主執行緒

## 後續開發方向

- [ ] 添加更多翻譯 API 提供者支援
- [ ] 添加離線翻譯功能
- [ ] 添加翻譯歷史記錄
- [ ] 添加文字朗讀功能
- [ ] 添加分享翻譯結果功能

## 许可证

本專案仅供学习和参考使用。
