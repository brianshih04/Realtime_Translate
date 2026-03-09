package com.example.translateapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TutorialActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        
        val btnGetStarted: Button = findViewById(R.id.btnGetStarted)
        
        btnGetStarted.setOnClickListener {
            // 跳轉到主 Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    
    override fun onBackPressed() {
        // 阻止返回鍵，強制用戶點擊開始按鈕
        super.onBackPressed()
        // 這裡可以選擇不調用 super，這樣返回鍵就不會有作用
    }
}
