package com.uni.memorycard.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.uni.memorycard.ui.model.GameConfiguration
import com.uni.memorycard.ui.utils.LocalUserPreferences
import kotlinx.coroutines.launch
@Composable
fun ConfigurationScreen(
    onStart: (GameConfiguration) -> Unit,
    onBack: () -> Unit
) {
    val userPreferences = LocalUserPreferences.current
    val playerName by userPreferences.playerName.collectAsState(initial = "")
    val savedCardTypes by userPreferences.numCardTypes.collectAsState(initial = 4)

    var numCardTypes by remember { mutableStateOf(savedCardTypes) }
    val coroutineScope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = playerName,
            onValueChange = {
                coroutineScope.launch {
                    userPreferences.updatePlayerName(it)
                }
            },
            label = { Text("Tu nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        Text("Número de pares distintos: $numCardTypes")
        Slider(
            value = numCardTypes.toFloat(),
            onValueChange = {
                numCardTypes = it.toInt()
                coroutineScope.launch {
                    userPreferences.updateNumCardTypes(numCardTypes)
                }
            },
            valueRange = 4f..10f,
            steps = 6
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            val boardSize = if (numCardTypes <= 6) "4x4 (16 cartas)" else "5x4 (20 cartas)"
            Text(
                text = "Tamaño del tablero: $boardSize",
                modifier = Modifier.padding(16.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Atrás")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    onStart(GameConfiguration(playerName, numCardTypes))
                },
                enabled = playerName.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Iniciar partida")
            }
        }
    }
}
