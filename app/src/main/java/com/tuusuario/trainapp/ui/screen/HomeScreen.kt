package com.tuusuario.trainapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onNavigateToRoutines: () -> Unit,
    onNavigateToWorkout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TrainApp",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD600)
                )
                Text(
                    text = "App de entrenamientos offline\nBase de datos lista ✓",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFBDBDBD)
                )
                Text(
                    text = "Próximamente: Rutinas, Ejecución, Stats, Backup",
                    fontSize = 14.sp,
                    color = Color(0xFF888888)
                )
                Button(
                    onClick = onNavigateToRoutines,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600),
                        contentColor = Color(0xFF121212)
                    )
                ) {
                    Text("Ver rutinas", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onNavigateToWorkout,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2C2C2C),
                        contentColor = Color(0xFFFFFFFF)
                    )
                ) {
                    Text("Entrenar ahora", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}