package com.example.agent.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun observeMessages(): Flow<List<MessageEntity>>
    @Query("SELECT COUNT(*) FROM messages")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageEntity>)

    @Query("DELETE FROM messages")
    suspend fun clearAll()
}
