package com.uni.memorycard.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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


@OptIn(ExperimentalMaterial3Api::class)
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

    val spacing = 20.dp

    if (isExpanded) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            // Formulario Izquierda
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 24.dp),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                Text(
                    text = "Configuración de Partida",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del jugador") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Text(
                    "Número de tipos de carta",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Slider(
                    value = numCartas.toFloat(),
                    onValueChange = { numCartas = it.toInt() },
                    valueRange = 4f..10f,
                    steps = 6,
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        thumbColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    "$numCartas tipos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    "Dificultad",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GameDifficulty.values().forEach { diff ->
                        Row(
                            Modifier
                                .selectable(
                                    selected = diff == dificultad,
                                    onClick = { dificultad = diff }
                                )
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = diff == dificultad,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = diff.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (diff == dificultad)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
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
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(
                        "Jugar",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Vista previa Derecha
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Vista previa",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Jugador:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Tipos de carta:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "$numCartas",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Dificultad:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    dificultad.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    } else {
        // Pantallas pequeñas: columna con botones separados y colores
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            Text(
                text = "Configuración de Partida",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del jugador") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Text(
                "Número de tipos de carta",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = numCartas.toFloat(),
                onValueChange = { numCartas = it.toInt() },
                valueRange = 4f..10f,
                steps = 6,
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    thumbColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "$numCartas tipos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                "Dificultad",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GameDifficulty.values().forEach { diff ->
                    Row(
                        Modifier
                            .selectable(
                                selected = diff == dificultad,
                                onClick = { dificultad = diff }
                            )
                            .padding(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = diff == dificultad,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = diff.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (diff == dificultad)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(
                        "Atrás",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            userPreferences.updatePlayerName(nombre)
                            userPreferences.updateNumCardTypes(numCartas)
                            userPreferences.updateDifficulty(dificultad)
                            onStart(GameConfiguration(nombre, numCartas, dificultad))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(
                        "Jugar",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}



