package com.example.andr_dev.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val playerId: Long,
    val score: Int,
    val difficulty: Int,
    val date: String // Формат "yyyy-MM-dd HH:mm"
)