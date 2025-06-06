package com.uni.memorycard.ui.utils

import androidx.compose.runtime.compositionLocalOf
import com.uni.memorycard.ui.data.local.AppDatabase
import com.uni.memorycard.ui.data.preferences.UserPreferences

val LocalDatabase = compositionLocalOf<AppDatabase> {
    error("No database provided")
}

val LocalUserPreferences = compositionLocalOf<UserPreferences> {
    error("No user preferences provided")
}