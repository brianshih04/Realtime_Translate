package com.example.translateapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationHistoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: TranslationHistory)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(histories: List<TranslationHistory>)
    
    @Delete
    suspend fun delete(history: TranslationHistory)
    
    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM translation_history")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC LIMIT :limit")
    fun getAllHistories(limit: Int = 50): Flow<List<TranslationHistory>>
    
    @Query("SELECT * FROM translation_history WHERE sourceText LIKE :query OR targetText LIKE :query ORDER BY timestamp DESC")
    fun searchHistories(query: String): Flow<List<TranslationHistory>>
    
    @Query("SELECT * FROM translation_history WHERE isFromOffline = :isOffline ORDER BY timestamp DESC")
    fun getHistoriesByType(isOffline: Boolean): Flow<List<TranslationHistory>>
    
    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC LIMIT 1")
    fun getLastHistory(): Flow<TranslationHistory?>
}
