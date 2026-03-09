package com.example.translateapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TranslationHistory::class],
    version = 1,
    exportSchema = false
)
abstract class TranslationDatabase : RoomDatabase() {
    abstract fun translationHistoryDao(): TranslationHistoryDao
    
    companion object {
        const val DATABASE_NAME = "translation_database"
    }
}
