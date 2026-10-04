package com.tuusuario.trainapp.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuusuario.trainapp.ui.common.EmptyState
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.SentimentSatisfied

@Composable
fun BodyScreen(
    onNavigateUp: () -> Unit
) {
    val weightEntries by remember { mutableStateOf(getMockWeightEntries()) }
    var showWeightDialog by remember { mutableStateOf(false) }
    val weightInput by remember { mutableStateOf("") }
    val weightDateInput by remember { mutableStateOf("") }
    val weightNotesInput by remember { mutableStateOf("") }
    val showQuizDialog by remember { mutableStateOf(false) }
    val quizType by remember { mutableStateOf<QuizType?>(null) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = "Cuerpo",
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showWeightDialog = true },
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Registrar peso") },
                text = { Text("Registrar peso", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                containerColor = Color(0xFFFFD600),
                contentColor = Color(0xFF121212)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current weight card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Peso actual", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Toggle button instead of Chip
                            Button(
                                onClick = { notificationsEnabled = !notificationsEnabled },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (notificationsEnabled) Color(0xFFFFD600) else Color(0xFF2C2C2C),
                                    contentColor = if (notificationsEnabled) Color(0xFF121212) else Color(0xFFFFFFFF)
                                )
                            ) {
                                Text("Recordatorio viernes")
                            }
                        }
                    }
                    weightEntries.firstOrNull()?.let { entry ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${entry.weight} kg", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = entry.date, fontSize = 14.sp, color = Color(0xFF888888))
                                entry.notes?.let { Text(text = it, fontSize = 12.sp, color = Color(0xFF888888)) }
                            }
                        }
                    }
                }
            }

            // Weight chart placeholder
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(180.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    Text(text = "Gráfica de evolución de peso", fontSize = 14.sp, color = Color(0xFF888888))
                    Text(text = "(Implementación pendiente - Canvas)", fontSize = 12.sp, color = Color(0xFF666666))
                }
            }

            // Sensation quiz buttons
            SectionHeader(title = "Test de sensaciones")
            Row(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* quizType = QuizType.START */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                ) {
                    Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Filled.SentimentSatisfied, contentDescription = null, tint = Color(0xFFFFFFFF))
                    Text("Al iniciar")
                }
                }
                Button(
                    onClick = { /* quizType = QuizType.END */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                ) {
                    Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Filled.SentimentSatisfied, contentDescription = null, tint = Color(0xFFFFFFFF))
                    Text("Al finalizar")
                }
                }
            }

            // Weight history
            SectionHeader(title = "Historial de peso", action = {
                Button(onClick = { /* showWeightDialog = true */ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600), contentColor = Color(0xFF121212))) { Text("Añadir") }
            })

            if (weightEntries.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.MonitorWeight,
                    title = "Sin registros",
                    message = "Registra tu peso semanal para ver la evolución",
                    actionText = "Registrar peso",
                    onActionClick = { /* showWeightDialog = true */ },
                    modifier = Modifier.padding(padding).fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(weightEntries) { entry: WeightEntry ->
                        WeightHistoryCard(entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
fun WeightHistoryCard(entry: WeightEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "${entry.weight} kg", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
                Text(text = entry.date, fontSize = 12.sp, color = Color(0xFF888888))
            }
            entry.notes?.let { Text(text = it, fontSize = 12.sp, color = Color(0xFF888888)) }
        }
    }
}

@Composable
fun SensationQuizDialog(
    quizType: QuizType,
    onComplete: () -> Unit,
    onDismiss: () -> Unit
) {
    val sleep by remember { mutableStateOf(7) }
    val fatigue by remember { mutableStateOf(3) }
    val motivation by remember { mutableStateOf(8) }
    val pain by remember { mutableStateOf(1) }
    val notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("${if (true) "Inicio" else "Fin"} de entrenamiento", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                QuizSlider(label = "Sueño (1-10)", value = 7, onValueChange = { })
                QuizSlider(label = "Fatiga (1-10)", value = 3, onValueChange = { })
                QuizSlider(label = "Motivación (1-10)", value = 8, onValueChange = { })
                QuizSlider(label = "Dolor/Molestias (1-10)", value = 1, onValueChange = { })
                TextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("Notas (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                    // colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                    //     containerColor = Color(0xFF2C2C2C),
                    //     focusedContainerColor = Color(0xFF2C2C2C),
                    //     textColor = Color(0xFFFFFFFF),
                    //     labelColor = Color(0xFF888888),
                    //     cursorColor = Color(0xFFFFD600)
                    // )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onComplete() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD600),
                    contentColor = Color(0xFF121212)
                )
            ) { Text("Guardar", fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun QuizSlider(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, fontSize = 16.sp, color = Color(0xFFFFFFFF))
            Text(text = "$value", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
        }
        androidx.compose.material3.Slider(
            value = value.toFloat(),
            onValueChange = { },
            valueRange = 1f..10f,
            steps = 9,
            colors = SliderDefaults.colors(
                activeTrackColor = Color(0xFFFFD600),
                activeTickColor = Color(0xFFFFD600),
                inactiveTrackColor = Color(0xFF2C2C2C),
                inactiveTickColor = Color(0xFF4A4A4A),
                thumbColor = Color(0xFFFFD600)
            )
        )
    }
}

enum class QuizType { START, END }

data class WeightEntry(
    val date: String,
    val weight: Double,
    val photoUri: String? = null,
    val notes: String? = null
)

fun getMockWeightEntries(): List<WeightEntry> {
    return listOf(
        WeightEntry("2026-10-01", 82.5, notes = "Post-entreno"),
        WeightEntry("2026-09-24", 83.0, notes = "Semana de carga"),
        WeightEntry("2026-09-17", 82.8, notes = "Bien"),
        WeightEntry("2026-09-10", 83.2, notes = "Subida agua"),
        WeightEntry("2026-09-03", 82.5, notes = "Objetivo")
    )
}