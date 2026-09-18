package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DhikrSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: DhikrSessionEntity): Long

    @Query("SELECT * FROM dhikr_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<DhikrSessionEntity>>

    @Query("SELECT * FROM dhikr_sessions WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getSessionsSince(sinceTimestamp: Long): Flow<List<DhikrSessionEntity>>

    @Query("SELECT COALESCE(SUM(count), 0) FROM dhikr_sessions")
    fun getTotalDhikrCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(count), 0) FROM dhikr_sessions WHERE timestamp >= :startOfDayTimestamp")
    fun getTodayDhikrCount(startOfDayTimestamp: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM dhikr_sessions WHERE timestamp >= :startOfDayTimestamp")
    fun getTodaySessionsCount(startOfDayTimestamp: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM dhikr_sessions WHERE completed = 1")
    fun getCompletedSessionsCount(): Flow<Int>

    @Query("DELETE FROM dhikr_sessions")
    suspend fun clearAllSessions()
}

@Dao
interface CustomDhikrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomDhikr(customDhikr: CustomDhikrEntity): Long

    @Query("SELECT * FROM custom_dhikr ORDER BY createdAt DESC")
    fun getAllCustomDhikr(): Flow<List<CustomDhikrEntity>>

    @Query("DELETE FROM custom_dhikr WHERE id = :id")
    suspend fun deleteCustomDhikr(id: Long)
}
