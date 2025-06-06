package com.uni.memorycard.ui.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        val PLAYER_NAME = stringPreferencesKey("player_name")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    }

    val playerName: Flow<String> = dataStore.data
        .map { preferences -> preferences[PLAYER_NAME] ?: "" }

    suspend fun updatePlayerName(name: String) {
        dataStore.edit { preferences ->
            preferences[PLAYER_NAME] = name
        }
    }
}