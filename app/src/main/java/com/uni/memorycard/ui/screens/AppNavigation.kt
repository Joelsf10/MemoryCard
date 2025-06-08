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
import com.uni.memorycard.ui.utils.LocalUserPreferences
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull


@Composable
fun MemoryCardNavigation() {
    val navController = rememberNavController()
    var currentConfig by remember { mutableStateOf(GameConfiguration()) }
    var gameResult by remember { mutableStateOf<GameResult?>(null) }
    val userPreferences = LocalUserPreferences.current
    val context = LocalContext.current

    NavHost(navController, startDestination = "main") {
        composable("main") {
            var navigateToGame by remember { mutableStateOf(false) }

            if (navigateToGame) {
                // Efecto para recuperar preferencias y navegar solo cuando se activa
                LaunchedEffect(Unit) {
                    val config = combine(
                        userPreferences.playerName,
                        userPreferences.numCardTypes
                    ) { name, num -> GameConfiguration(name, num) }
                        .firstOrNull()

                    config?.let {
                        currentConfig = it
                        navController.navigate("game")
                        navigateToGame = false // reseteamos el trigger
                    }
                }
            }

            MainMenuScreen(
                onPlay = {
                    navigateToGame = true
                },
                onHelp = { navController.navigate("help") },
                onHistory = { navController.navigate("history") },
                onExit = {
                    if (context is ComponentActivity) {
                        context.finish()
                    }
                },
                onConfig = { navController.navigate("config") }
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
