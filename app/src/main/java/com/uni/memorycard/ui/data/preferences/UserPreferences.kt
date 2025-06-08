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
        private val PLAYER_NAME_KEY = stringPreferencesKey("player_name")
        private val NUM_CARD_TYPES_KEY = intPreferencesKey("num_card_types")
    }

    val playerName: Flow<String> = dataStore.data
        .map { it[PLAYER_NAME_KEY] ?: "" }

    val numCardTypes: Flow<Int> = dataStore.data
        .map { it[NUM_CARD_TYPES_KEY] ?: 4 }

    suspend fun updatePlayerName(name: String) {
        dataStore.edit { prefs ->
            prefs[PLAYER_NAME_KEY] = name
        }
    }

    suspend fun updateNumCardTypes(num: Int) {
        dataStore.edit { prefs ->
            prefs[NUM_CARD_TYPES_KEY] = num
        }
    }
}
