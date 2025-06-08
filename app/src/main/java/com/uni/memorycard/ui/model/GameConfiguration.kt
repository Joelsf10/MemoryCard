package com.uni.memorycard.ui.model

import com.uni.memorycard.ui.data.preferences.GameDifficulty


data class GameConfiguration(
    val playerName: String = "",
    val numCardTypes: Int = 4,
    val difficulty: GameDifficulty = GameDifficulty.VERY_EASY
)