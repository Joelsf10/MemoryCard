package com.uni.memorycard.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.uni.memorycard.ui.model.GameConfiguration
import com.uni.memorycard.ui.model.GameResult


@Composable
fun MemoryCardNavigation() {
    val navController = rememberNavController()
    var currentConfig by remember { mutableStateOf(GameConfiguration()) }
    var gameResult by remember { mutableStateOf<GameResult?>(null) }
    val context = LocalContext.current

    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainMenuScreen(
                onPlay = { navController.navigate("config") },
                onHelp = { navController.navigate("help") },
                onHistory = {navController.navigate("history") },
                onExit = {
                    if (context is ComponentActivity){
                        context.finish()
                    }

                }
            )
        }

        composable("config") {
            ConfigurationScreen(
                onStart = { config ->
                    currentConfig = config
                    navController.navigate("game")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("game") {
            GameScreen(
                config = currentConfig,
                onGameEnd = { result ->
                    gameResult = result
                    navController.navigate("results")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("results") {
            gameResult?.let { result ->
                ResultsScreen(
                    result = result,
                    onRestart = { navController.navigate("game") },
                    onExit = { navController.navigate("main") }
                )
            }
        }

        composable("help") {
            HelpScreen { navController.popBackStack() }
        }

        composable("history") {
            HistoryScreen { navController.popBackStack() }
        }
    }
}