package com.uni.memorycard.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameStateFactory(private val initialConfig: GameConfiguration) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameState::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameState(initialConfig) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}