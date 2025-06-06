package com.uni.memorycard.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playerName: String,
    val numCardTypes: Int,
    val timeSeconds: Int,
    val errorCount: Int,
    val isWinner: Boolean,
    val date: Date = Date()
)