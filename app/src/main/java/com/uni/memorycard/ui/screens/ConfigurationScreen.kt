package com.uni.memorycard.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uni.memorycard.ui.data.preferences.GameDifficulty
import com.uni.memorycard.ui.model.GameConfiguration
import com.uni.memorycard.ui.utils.LocalUserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Composable
fun ConfigurationScreen(
    onStart: (GameConfiguration) -> Unit,
    onBack: () -> Unit
) {
    val userPreferences = LocalUserPreferences.current
    val coroutineScope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var numCartas by remember { mutableStateOf(4) }
    var dificultad by remember { mutableStateOf(GameDifficulty.VERY_EASY) }

    // Carga inicial desde las preferencias
    LaunchedEffect(Unit) {
        nombre = userPreferences.playerName.first()
        numCartas = userPreferences.numCardTypes.first()
        dificultad = userPreferences.difficulty.first()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Configuración de Partida", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del jugador") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Número de tipos de carta:")
        Slider(
            value = numCartas.toFloat(),
            onValueChange = { numCartas = it.toInt() },
            valueRange = 4f..10f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Seleccionado: $numCartas")

        Text("Dificultad:")
        GameDifficulty.values().forEach { diff ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dificultad = diff }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = dificultad == diff,
                    onClick = { dificultad = diff }
                )
                Text(text = diff.label)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick = onBack) {
                Text("Atrás")
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        // Guardamos la configuración
                        userPreferences.updatePlayerName(nombre)
                        userPreferences.updateNumCardTypes(numCartas)
                        userPreferences.updateDifficulty(dificultad)

                        // Creamos la configuración para la partida
                        val config = GameConfiguration(
                            playerName = nombre,
                            numCardTypes = numCartas,
                            difficulty = dificultad
                        )
                        onStart(config)
                    }
                }
            ) {
                Text("Jugar")
            }
        }
    }
}

