package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dhikr_sessions")
data class DhikrSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dhikrName: String,
    val dhikrArabic: String,
    val count: Int,
    val target: Int,
    val completed: Boolean,
    val round: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_dhikr")
data class CustomDhikrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val arabic: String,
    val target: Int,
    val createdAt: Long = System.currentTimeMillis()
)
