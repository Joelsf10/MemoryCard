package com.uni.memorycard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uni.memorycard.ui.screens.MemoryCardNavigation
import com.uni.memorycard.ui.theme.MemoryCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoryCardTheme {
                MemoryCardNavigation()
            }
        }
    }
}