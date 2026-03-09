package com.example.translateapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.translateapp.viewmodel.TranslateViewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {
    
    private lateinit var viewModel: TranslateViewModel
    private lateinit var speechRecognizer: SpeechRecognizer
    
    // 語音識別結果回調
    private val speechLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val matches = result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.isNotEmpty()) {
                val text = matches[0]
                viewModel.setInputText(text)
            }
        }
    }
    
    // 權限申請回調
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            startSpeechRecognition()
        } else {
            Toast.makeText(this, R.string.error_permission, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 初始化 ViewModel
        viewModel = ViewModelProvider(this)[TranslateViewModel::class.java]
        
        // 初始化語音識別器
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        
        // 設定 UI 元件
        setupUI()
        
        // 初始化語言選擇器
        setupLanguageSpinners()
        
        // 監聽ViewModel狀態變化
        setupViewModelListener()
        
        // 檢查是否顯示入門指導
        checkShowTutorial()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
    
    private fun setupUI() {
        val btnSpeech: Button = findViewById(R.id.btnSpeech)
        val btnTranslate: Button = findViewById(R.id.btnTranslate)
        val btnClear: Button = findViewById(R.id.btnClear)
        val btnCopy: Button = findViewById(R.id.btnCopy)
        val btnSwap: Button = findViewById(R.id.btnSwap)
        val etInputText: EditText = findViewById(R.id.etInputText)
        val tvResult: TextView = findViewById(R.id.tvResult)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        
        // 語音輸入按鈕點擊事件
        btnSpeech.setOnClickListener {
            checkSpeechPermission()
        }
        
        // 翻譯按鈕點擊事件
        btnTranslate.setOnClickListener {
            viewModel.translate()
        }
        
        // 清除按鈕點擊事件
        btnClear.setOnClickListener {
            viewModel.clear()
            etInputText.text.clear()
            tvResult.text = ""
        }
        
        // 複製按鈕點擊事件
        btnCopy.setOnClickListener {
            copyToClipboard(tvResult.text.toString())
        }
        
        // 交換語言按鈕點擊事件
        btnSwap.setOnClickListener {
            viewModel.swapLanguages()
        }
    }
    
    private fun setupLanguageSpinners() {
        val spSourceLanguage: Spinner = findViewById(R.id.spSourceLanguage)
        val spTargetLanguage: Spinner = findViewById(R.id.spTargetLanguage)
        
        // 建立語言列表
        val languageNames = LanguageData.languages.map { it.name }
        val sourceAdapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languageNames
        )
        val targetAdapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languageNames
        )
        
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        spSourceLanguage.adapter = sourceAdapter
        spTargetLanguage.adapter = targetAdapter
        
        // 設定預設選項
        spSourceLanguage.setSelection(LanguageData.getDefaultSourceIndex())
        spTargetLanguage.setSelection(LanguageData.getDefaultTargetIndex())
        
        // 語言選擇監聽
        spSourceLanguage.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = LanguageData.languages[position].name
                viewModel.setSourceLanguage(LanguageData.getLanguageCode(selectedLanguage))
            }
            
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        })
        
        spTargetLanguage.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = LanguageData.languages[position].name
                viewModel.setTargetLanguage(LanguageData.getLanguageCode(selectedLanguage))
            }
            
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        })
    }
    
    private fun setupViewModelListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                // 顯示或隱藏加載進度條
                findViewById<ProgressBar>(R.id.progressBar).visibility = 
                    if (state.isLoading) View.VISIBLE else View.GONE
                
                // 更新翻譯結果
                if (state.isTranslationSuccessful) {
                    findViewById<TextView>(R.id.tvResult).text = state.resultText
                }
                
                // 顯示錯誤訊息
                state.errorMessage?.let { message ->
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }
    }
    
    private fun checkSpeechPermission() {
        val permission = Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            startSpeechRecognition()
        } else {
            permissionLauncher.launch(arrayOf(permission))
        }
    }
    
    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.TAIWAN)
            putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.hint_input_text)
        }
        
        try {
            speechLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "語音識別功能不可用", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun copyToClipboard(text: String) {
        if (text.isEmpty()) {
            Toast.makeText(this, "沒有可複製的內容", Toast.LENGTH_SHORT).show()
            return
        }
        
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("翻譯結果", text)
        clipboard.setPrimaryClip(clip)
        
        Toast.makeText(this, R.string.success_copy, Toast.LENGTH_SHORT).show()
    }
    
    private fun checkShowTutorial() {
        val prefs = getSharedPreferences("tutorial", MODE_PRIVATE)
        val showTutorial = prefs.getBoolean("show_tutorial", true)
        
        if (showTutorial) {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
            prefs.edit().putBoolean("show_tutorial", false).apply()
        }
    }
}
