# Android 即時翻譯 App

這是一個功能完整的 Android 即時翻譯應用程式，支援語音輸入、多語言切換、離線翻譯和翻譯歷史記錄功能。

## 功能特色

- 🌐 **多語言支援**：支援中文、英文、日文、韓文、西班牙文、法文、德文、越南文、印尼文、泰文等語言
- 🎤 **語音輸入**：支援語音轉文字功能，直接錄音即可翻譯
- 🔀 **快速切換**：一鍵交換來源與目標語言
- 💾 **快速複製**：一鍵複製翻譯結果到剪貼簿
- 📶 **離線翻譯**：無網路時仍可進行簡單翻譯
- 📜 **翻譯歷史**：自動記錄所有翻譯內容，方便查閱
- 💾 **本地儲存**：使用 Room Database 儲存翻譯歷史

## 前置作業

### 需求

- Android Studio Electric Eel 或更新版本
- Android SDK 34 (Android 14)
- Kotlin 1.9.24
- Gradle 8.2.0

### Google Cloud Translation API 設定 (選用)

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
│   │       │   ├── data/ (Room Database)
│   │       │   │   ├── TranslationHistory.kt
│   │       │   │   ├── TranslationHistoryDao.kt
│   │       │   │   └── TranslationDatabase.kt
│   │       │   ├── repository/ (資料倉儲)
│   │       │   │   ├── TranslateRepository.kt
│   │       │   │   └── OfflineRepository.kt
│   │       │   ├── viewmodel/
│   │       │   │   └── TranslateViewModel.kt
│   │       │   ├── HistoryAdapter.kt
│   │       │   ├── MainActivity.kt
│   │       │   ├── TranslateApplication.kt
│   │       │   ├── LanguageData.kt
│   │       │   └── TutorialActivity.kt
│   │       ├── res/
│   │       │   ├── layout/ (UI 佈局)
│   │       │   ├── values/ (字串和顏色資源)
│   │       │   └── xml/ (備份規則)
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## 使用說明

### 基本翻譯

1. **啟動應用程式**：首次啟動會顯示新手入門指導
2. **語音輸入**：點擊語音輸入按鈕，允許錄音後開始說話
3. **文字輸入**：直接在文字框中輸入要翻譯的文字
4. **選擇語言**：從下拉選單選擇來源語言和目標語言
5. **翻譯文字**：點擊「翻譯」按鈕
6. **操作翻譯結果**：可複製結果或交換語言

### 離線翻譯

1. 切換「離線模式」開關
2. 輸入簡單的詞彙或短語
3. 點擊「翻譯」按鈕
4. 離線模式下使用詞典進行翻譯

### 翻譯歷史

1. 翻譯後會自動儲存到歷史記錄
2. 滾動到畫面底部查看歷史記錄
3. 點擊歷史記錄可載入到輸入框

## 權限

- `RECORD_AUDIO`：語音輸入功能
- `INTERNET`：網路連線以進行翻譯

## 技術特點

### Room Database
- 使用 Room Database 儲存翻譯歷史
- 支援非同步操作
- 自動管理資料庫連接

### Coroutines
- 使用 Kotlin Coroutines 處理非同步操作
- 避免主執行緒阻塞
- 簡化異步程式碼

### ViewBinding
- 使用 ViewBinding 取代 findViewById
- 編譯時檢查 View 引用
- 更安全的 UI 操作

## 离線翻譯說明

目前離線翻譯使用簡單詞典進行翻譯，支援以下語言對：

- 中文 ↔ 英文
- 日文 ↔ 中文
- 韓文 ↔ 中文

如需更多翻譯結果，請關閉離線模式並使用 Google Cloud Translation API。

## 後續開發方向

- [ ] 添加更多翻譯 API 提供者支援
- [ ] 整合 ML Kit 實現更強大的離線翻譯
- [ ] 添加翻譯歷史記錄管理功能
- [ ] 添加文字朗讀功能
- [ ] 添加分享翻譯結果功能
- [ ] 支援更多語言

## 常見問題

**Q: 離線翻譯結果不準確怎么办？**  
A: 請關閉離線模式，使用線上翻譯功能獲得更準確的翻譯結果。

**Q: 翻譯歷史會儲存在哪裡？**  
A: 翻譯歷史使用 Room Database 儲存在裝置本地，不會上傳到伺服器。

**Q: 如何清除翻譯歷史？**  
A: 目前版本需手動清空，後續版本會新增清除功能。

## 许可证

本專案仅供学习和参考使用。
