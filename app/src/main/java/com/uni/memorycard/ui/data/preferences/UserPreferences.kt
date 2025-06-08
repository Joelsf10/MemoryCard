package com.uni.memorycard.ui.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        val PLAYER_NAME = stringPreferencesKey("player_name")
        val NUM_CARD_TYPES = intPreferencesKey("num_card_types")
        val DIFFICULTY = stringPreferencesKey("difficulty")
    }

    val playerName: Flow<String> = dataStore.data
        .map { preferences -> preferences[PLAYER_NAME] ?: "Jugador" }

    val numCardTypes: Flow<Int> = dataStore.data
        .map { preferences -> preferences[NUM_CARD_TYPES] ?: 6 }

    val difficulty: Flow<GameDifficulty> = dataStore.data
        .map { preferences ->
            val name = preferences[DIFFICULTY] ?: GameDifficulty.VERY_EASY.name
            GameDifficulty.valueOf(name)
        }

    suspend fun updatePlayerName(name: String) {
        dataStore.edit { prefs ->
            prefs[PLAYER_NAME] = name
        }
    }

    suspend fun updateNumCardTypes(num: Int) {
        dataStore.edit { prefs ->
            prefs[NUM_CARD_TYPES] = num
        }
    }

    suspend fun updateDifficulty(difficulty: GameDifficulty) {
        dataStore.edit { prefs ->
            prefs[DIFFICULTY] = difficulty.name
        }
    }

}
