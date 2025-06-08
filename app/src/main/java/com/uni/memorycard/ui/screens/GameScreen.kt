package com.uni.memorycard.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uni.memorycard.R
import com.uni.memorycard.ui.model.GameConfiguration
import com.uni.memorycard.ui.model.GameResult
import com.uni.memorycard.ui.model.GameState
import com.uni.memorycard.ui.model.GameStateFactory
import com.uni.memorycard.ui.utils.LocalDatabase
import com.uni.memorycard.ui.utils.rememberWindowSizeClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    config: GameConfiguration,
    onGameEnd: (GameResult) -> Unit,
    onBack: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val gameState: GameState = viewModel(factory = GameStateFactory(config))
    val database = LocalDatabase.current

    val timeLimit = config.difficulty.timeLimitSeconds
    val hasTimeLimit = timeLimit != null && timeLimit > 0

    var elapsedTime by remember { mutableStateOf(0) }
    var timeRemaining by remember { mutableStateOf(timeLimit ?: 0) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var showResetDialog by remember { mutableStateOf(false) }

    val windowSize = rememberWindowSizeClass()
    val isExpanded = windowSize >= WindowWidthSizeClass.Medium

    LaunchedEffect(Unit) {
        while (isTimerRunning) {
            delay(1000L)

            if (gameState.isGameComplete()) {
                isTimerRunning = false
                val timeUsed = if (hasTimeLimit) (timeLimit!! - timeRemaining) else elapsedTime
                val result = GameResult(
                    playerName = config.playerName,
                    numCardTypes = config.numCardTypes,
                    timeSeconds = timeUsed,
                    errorCount = gameState.errorCount,
                    isWinner = true
                )
                database.gameResultDao().insert(result)
                withContext(Dispatchers.Main) { onGameEnd(result) }
                break
            }

            if (hasTimeLimit) {
                timeRemaining--
                if (timeRemaining <= 0) {
                    isTimerRunning = false
                    val result = GameResult(
                        playerName = config.playerName,
                        numCardTypes = config.numCardTypes,
                        timeSeconds = timeLimit!!,
                        errorCount = gameState.errorCount,
                        isWinner = false
                    )
                    database.gameResultDao().insert(result)
                    withContext(Dispatchers.Main) { onGameEnd(result) }
                    break
                }
            } else elapsedTime++
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(config.playerName, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { showResetDialog = true }) {
                        Text("←", style = MaterialTheme.typography.headlineSmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.surface,
                    titleContentColor = colorScheme.primary
                )
            )
        }
    ) { padding ->
        if (isExpanded) {
            // Bipanel: tablero izquierda, info derecha
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(colorScheme.surfaceVariant)
            ) {
                // Izquierda: tablero
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gameState.gridColumns),
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .widthIn(max = 500.dp)
                ) {
                    itemsIndexed(gameState.cards) { index, card ->
                        Card(
                            modifier = Modifier
                                .aspectRatio(0.75f)
                                .padding(4.dp)
                                .clickable(enabled = gameState.isClickEnabled) { gameState.flipCard(index) },
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (card.isFaceUp || card.isMatched) {
                                    Image(
                                        painter = painterResource(card.imageRes),
                                        contentDescription = "Carta ${index + 1}",
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(R.drawable.back),
                                        contentDescription = "Dorso de carta",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                if (card.isMatched) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.3f))
                                    )
                                }
                            }
                        }
                    }
                }

                // Derecha: info (estadísticas, temporizador, errores...)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val pairsFound = gameState.cards.count { it.isMatched } / 2
                    val totalPairs = config.numCardTypes

                    Text(
                        text = "⏱ Tiempo: ${if (hasTimeLimit) "$timeRemaining s" else "$elapsedTime s"}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "❌ Errores: ${gameState.errorCount}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "✅ Parejas: $pairsFound/$totalPairs",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.errorContainer)
                    ) {
                        Text("Salir", color = colorScheme.onErrorContainer)
                    }
                }
            }
        } else {
            // Pantalla única para pantallas pequeñas: igual que tu versión original
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gameState.gridColumns),
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 500.dp)
                ) {
                    itemsIndexed(gameState.cards) { index, card ->
                        Card(
                            modifier = Modifier
                                .aspectRatio(0.75f)
                                .padding(4.dp)
                                .clickable(enabled = gameState.isClickEnabled) { gameState.flipCard(index) },
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (card.isFaceUp || card.isMatched) {
                                    Image(
                                        painter = painterResource(card.imageRes),
                                        contentDescription = "Carta ${index + 1}",
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(R.drawable.back),
                                        contentDescription = "Dorso de carta",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                if (card.isMatched) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.3f))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                shape = RoundedCornerShape(16.dp),
                title = { Text("¿Salir de la partida?") },
                text = { Text("Perderás tu progreso actual") },
                confirmButton = {
                    Button(
                        onClick = {
                            showResetDialog = false
                            onBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.errorContainer,
                            contentColor = colorScheme.onErrorContainer
                        )
                    ) { Text("Salir") }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showResetDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

