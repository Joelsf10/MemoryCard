package com.uni.memorycard.ui.data.preferences

enum class GameDifficulty(val label: String, val timeLimitSeconds: Int?) {
    VERY_EASY("Muy Fácil", null),  // Sin tiempo
    EASY("Fácil", 90),
    MEDIUM("Media", 60),
    HARD("Difícil", 30);

    override fun toString(): String = label
}
