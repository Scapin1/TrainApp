package com.tuusuario.trainapp.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search

@Composable
fun HistoryScreen(
    onWorkoutClick: (Long) -> Unit,
    onNavigateUp: () -> Unit
) {
    val workouts by remember { mutableStateOf(getMockWorkouts()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedRoutineFilter by remember { mutableStateOf<String?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }
    var exportFormat by remember { mutableStateOf("JSON") }
    var showDeleteConfirm by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = "Historial",
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(imageVector = Icons.Filled.FileDownload, contentDescription = "Exportar", tint = Color(0xFFFFFFFF))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar entrenamientos…", color = Color(0xFF888888)) },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color(0xFF888888)) },
                    singleLine = true,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Filter chips - using Button instead of Chip
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterButtonRow(
                        title = "Rutina",
                        options = listOf("Todas", "Push/Pull/Legs", "Upper/Lower", "Full Body"),
                        selected = selectedRoutineFilter,
                        onSelect = { selectedRoutineFilter = if (it == "Todas") null else it }
                    )
                }
            }

            // Workout list
            val filteredWorkouts = workouts.filter { w ->
                (searchQuery.isBlank() || w.routineName.lowercase().contains(searchQuery.lowercase())) &&
                (selectedRoutineFilter == null || w.routineName == selectedRoutineFilter)
            }

            if (filteredWorkouts.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.History,
                    title = "Sin entrenamientos",
                    message = searchQuery.ifBlank { "No hay entrenamientos registrados aún" } ?: "No coinciden con los filtros",
                    actionText = if (searchQuery.isBlank()) "Iniciar entrenamiento" else null,
                    onActionClick = { /* TODO */ },
                    modifier = Modifier.padding(padding).fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredWorkouts) { workout ->
                        WorkoutHistoryCard(
                            date = workout.date,
                            routineName = workout.routineName,
                            dayName = workout.dayName,
                            duration = workout.duration,
                            volume = workout.volume,
                            rpeAvg = workout.rpeAvg,
                            onClick = { onWorkoutClick(workout.id) },
                            onExport = { /* TODO */ },
                            onDelete = { showDeleteConfirm = workout.id }
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Exportar historial", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Formato de exportación:", fontSize = 16.sp, color = Color(0xFFFFFFFF))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { exportFormat = "JSON" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (exportFormat == "JSON") Color(0xFFFFD600) else Color(0xFF2C2C2C)
                            )
                        ) { Text("JSON (completo)") }
                        Button(
                            onClick = { exportFormat = "CSV" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (exportFormat == "CSV") Color(0xFFFFD600) else Color(0xFF2C2C2C)
                            )
                        ) { Text("CSV (entrenamientos)") }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { /* TODO: export */ showExportDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600),
                        contentColor = Color(0xFF121212)
                    )
                ) { Text("Exportar", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                Button(onClick = { showExportDialog = false }) { Text("Cancelar") }
            }
        )
    }

    showDeleteConfirm?.let { workoutId ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Eliminar entrenamiento", fontWeight = FontWeight.Bold) },
            text = { Text("¿Eliminar este entrenamiento? No se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = { /* TODO: delete */ showDeleteConfirm = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350),
                        contentColor = Color(0xFF121212)
                    )
                ) { Text("Eliminar", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirm = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun WorkoutHistoryCard(
    date: String,
    routineName: String,
    dayName: String,
    duration: String,
    volume: Int,
    rpeAvg: Float?,
    onClick: () -> Unit,
    onExport: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color(0xFFFFFFFF)
        ),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = routineName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                Text(text = date, fontSize = 12.sp, color = Color(0xFF888888))
            }
            Text(text = dayName, fontSize = 12.sp, color = Color(0xFF888888))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(top = 8.dp)) {
                statChip("Duración", duration)
                statChip("Volumen", "$volume series")
                rpeAvg?.let { statChip("RPE prom.", String.format("%.1f", it)) }
            }
        }
    }
}

@Composable
private fun statChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
        Text(text = label, fontSize = 10.sp, color = Color(0xFF888888))
    }
}

@Composable
fun FilterButtonRow(
    title: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF888888))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                Button(
                    onClick = { onSelect(option) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected == option || (selected == null && option == "Todas")) Color(0xFFFFD600) else Color(0xFF2C2C2C)
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(option, color = if (selected == option || (selected == null && option == "Todas")) Color(0xFF121212) else Color(0xFFFFFFFF))
                }
            }
        }
    }
}

data class MockWorkout(
    val id: Long,
    val routineName: String,
    val dayName: String,
    val date: String,
    val duration: String,
    val volume: Int,
    val rpeAvg: Float?
)

fun getMockWorkouts(): List<MockWorkout> {
    return listOf(
        MockWorkout(id = 1, routineName = "Push/Pull/Legs", dayName = "Día 1 - Push A", date = "2026-10-01", duration = "1h 23m", volume = 24, rpeAvg = 8.2f),
        MockWorkout(id = 2, routineName = "Push/Pull/Legs", dayName = "Día 2 - Pull A", date = "2026-09-29", duration = "1h 15m", volume = 22, rpeAvg = 7.8f),
        MockWorkout(id = 3, routineName = "Upper/Lower", dayName = "Día 1 - Upper", date = "2026-09-27", duration = "1h 05m", volume = 18, rpeAvg = 8.0f),
        MockWorkout(id = 4, routineName = "Full Body", dayName = "Día 1", date = "2026-09-25", duration = "55m", volume = 15, rpeAvg = 7.5f)
    )
}