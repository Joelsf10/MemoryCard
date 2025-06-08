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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uni.memorycard.ui.data.preferences.GameDifficulty
import com.uni.memorycard.ui.model.GameConfiguration
import com.uni.memorycard.ui.utils.LocalUserPreferences
import com.uni.memorycard.ui.utils.rememberWindowSizeClass
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Composable
fun ConfigurationScreen(
    onStart: (GameConfiguration) -> Unit,
    onBack: () -> Unit
) {
    val userPreferences = LocalUserPreferences.current
    val coroutineScope = rememberCoroutineScope()

    var nombre by rememberSaveable { mutableStateOf("") }
    var numCartas by rememberSaveable { mutableStateOf(4) }
    var dificultad by rememberSaveable { mutableStateOf(GameDifficulty.VERY_EASY) }

    LaunchedEffect(Unit) {
        if (nombre.isEmpty()) {
            nombre = userPreferences.playerName.first()
        }
        if (numCartas == 4) {
            numCartas = userPreferences.numCardTypes.first()
        }
        if (dificultad == GameDifficulty.VERY_EASY) {
            dificultad = userPreferences.difficulty.first()
        }
    }

    val windowSize = rememberWindowSizeClass()
    val isExpanded = windowSize >= WindowWidthSizeClass.Medium

    if (isExpanded) {
        Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            // Izquierda: formulario
            Column(
                modifier = Modifier.weight(1f),
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

                Text("Número de tipos de carta")
                Slider(
                    value = numCartas.toFloat(),
                    onValueChange = { numCartas = it.toInt() },
                    valueRange = 4f..10f,
                    steps = 10
                )
                Text("$numCartas tipos")

                Text("Dificultad")
                Row {
                    GameDifficulty.values().forEach { diff ->
                        Row(
                            Modifier
                                .selectable(
                                    selected = diff == dificultad,
                                    onClick = { dificultad = diff }
                                )
                                .padding(end = 8.dp)
                        ) {
                            RadioButton(selected = diff == dificultad, onClick = null)
                            Text(text = diff.name)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            userPreferences.updatePlayerName(nombre)
                            userPreferences.updateNumCardTypes(numCartas)
                            userPreferences.updateDifficulty(dificultad)
                        }
                        onStart(GameConfiguration(nombre, numCartas, dificultad))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Jugar")
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Derecha: vista previa simple
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Vista previa", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Jugador: $nombre")
                Text("Tipos de carta: $numCartas")
                Text("Dificultad: ${dificultad.name}")
            }
        }
    } else {
        // Para pantallas pequeñas, el formulario en una columna normal
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

            Text("Número de parejas")
            Slider(
                value = numCartas.toFloat(),
                onValueChange = { numCartas = it.toInt() },
                valueRange = 4f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth()
            )
            Text("$numCartas tipos")

            Text("Dificultad")
            Row {
                GameDifficulty.values().forEach { diff ->
                    Row(
                        Modifier
                            .selectable(
                                selected = diff == dificultad,
                                onClick = { dificultad = diff }
                            )
                            .padding(end = 8.dp)
                    ) {
                        RadioButton(selected = diff == dificultad, onClick = null)
                        Text(text = diff.name)
                    }
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
}


