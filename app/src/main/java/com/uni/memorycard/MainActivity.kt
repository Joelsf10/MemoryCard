package com.uni.memorycard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.uni.memorycard.data.local.AppDatabase
import com.uni.memorycard.ui.data.preferences.UserPreferences
import com.uni.memorycard.ui.screens.MemoryCardNavigation
import com.uni.memorycard.ui.theme.MemoryCardTheme
import com.uni.memorycard.ui.utils.LocalDatabase
import com.uni.memorycard.ui.utils.LocalUserPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val database = remember { AppDatabase.getDatabase(this) }
            val dataStore = remember {
                PreferenceDataStoreFactory.create(
                    produceFile = { preferencesDataStoreFile("user_preferences") }
                )
            }
            val userPreferences = remember { UserPreferences(dataStore) }

            CompositionLocalProvider(
                LocalDatabase provides database,
                LocalUserPreferences provides userPreferences
            ) {
                MemoryCardTheme {
                    MemoryCardNavigation()
                }
            }
        }
    }
}