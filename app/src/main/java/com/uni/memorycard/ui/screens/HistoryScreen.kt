package com.uni.memorycard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.uni.memorycard.ui.model.GameResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    results: List<GameResult>,
    onBack: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedResultIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de partidas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Lista de partidas a la izquierda
            LazyColumn(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(16.dp)
                    .background(colorScheme.surfaceVariant, shape = RoundedCornerShape(size = 12.dp))
            ) {
                itemsIndexed(results) { index, result ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedResultIndex = index },
                        shape = RoundedCornerShape(12.dp),
                        colors = if (index == selectedResultIndex) {
                            CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
                        } else {
                            CardDefaults.cardColors()
                        }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(result.playerName, fontWeight = FontWeight.Bold)
                            Text("Pares: ${result.numCardTypes}")
                            Text("Tiempo: ${formatTime(result.timeSeconds)}")
                        }
                    }
                }
            }

            // Detalles de la partida seleccionada a la derecha
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(16.dp)
                    .background(colorScheme.surface, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                if (selectedResultIndex != null) {
                    val result = results[selectedResultIndex!!]
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Detalles de la partida", style = MaterialTheme.typography.titleLarge)
                        Text("Jugador: ${result.playerName}")
                        Text("Pares distintos: ${result.numCardTypes}")
                        Text("Tiempo: ${formatTime(result.timeSeconds)}")
                        Text("Errores: ${result.errorCount}")
                        Text("Resultado: ${if (result.isWinner) "Ganador" else "Perdedor"}")
                    }
                } else {
                    Text(
                        "Selecciona una partida para ver detalles",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

// Función auxiliar para mostrar el tiempo en formato mm:ss
private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}