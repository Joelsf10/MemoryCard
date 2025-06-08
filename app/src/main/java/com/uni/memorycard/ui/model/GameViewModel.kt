package com.uni.memorycard.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uni.memorycard.ui.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class GameViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _config = MutableStateFlow<GameConfiguration?>(null)
    val config: StateFlow<GameConfiguration?> = _config.asStateFlow()

    fun loadConfigAndStartGame() {
        viewModelScope.launch {
            combine(
                userPreferences.playerName,
                userPreferences.numCardTypes,
                userPreferences.difficulty
            ) { name, num, difficulty ->
                GameConfiguration(name, num, difficulty)
            }.firstOrNull()?.let {
                _config.value = it
            }
        }
    }

    fun resetConfig() {
        _config.value = null
    }
}
