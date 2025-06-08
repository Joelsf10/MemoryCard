package com.uni.memorycard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuScreen(
    onPlay: () -> Unit,
    onHelp: () -> Unit,
    onHistory: () -> Unit,
    onExit: () -> Unit,
    onConfig: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val buttonShape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Icono de configuración arriba a la derecha
        IconButton(
            onClick = onConfig,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Configuración",
                tint = colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Memory Card",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Button(
                onClick = onPlay,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = buttonShape
            ) {
                Text("Nueva Partida")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onHelp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = buttonShape
            ) {
                Text("Cómo Jugar")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = buttonShape
            ) {
                Text("Ver Historial")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onExit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = buttonShape
            ) {
                Text("Salir")
            }
        }
    }
}
