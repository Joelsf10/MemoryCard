package com.uni.memorycard.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uni.memorycard.ui.data.preferences.GameDifficulty
import com.uni.memorycard.ui.model.GameConfiguration
import com.uni.memorycard.ui.model.GameResult
import com.uni.memorycard.ui.model.GameViewModel
import com.uni.memorycard.ui.utils.LocalDatabase
import com.uni.memorycard.ui.utils.LocalUserPreferences
import com.uni.memorycard.ui.viewmodel.GameViewModelFactory


@Composable
fun MemoryCardNavigation() {
    val navController = rememberNavController()
    val defaultDifficulty = LocalUserPreferences.current.difficulty.collectAsState(initial = GameDifficulty.VERY_EASY).value
    var currentConfig by remember {
        mutableStateOf(
            GameConfiguration(difficulty = defaultDifficulty)
        )
    }
    var gameResult by remember { mutableStateOf<GameResult?>(null) }
    val userPreferences = LocalUserPreferences.current
    val context = LocalContext.current
    val database = LocalDatabase.current
    val results by database.gameResultDao().getAllResults().collectAsState(initial = emptyList())

    NavHost(navController, startDestination = "main") {
        composable("main") {
            val viewModel: GameViewModel = viewModel(
                factory = GameViewModelFactory(userPreferences)
            )
            val config by viewModel.config.collectAsState()

            LaunchedEffect(config) {
                config?.let {
                    currentConfig = it
                    navController.navigate("game")
                    viewModel.resetConfig()
                }
            }

            MainMenuScreen(
                onPlay = {
                    viewModel.loadConfigAndStartGame()
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
                    navController.navigate("results") {
                        popUpTo("game") { inclusive = true } // limpia el backstack hasta la partida
                    }
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
            HistoryScreen(results = results, onBack = { navController.popBackStack() })
        }
    }
}
