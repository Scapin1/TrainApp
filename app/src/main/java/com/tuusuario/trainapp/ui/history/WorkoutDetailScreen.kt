package com.tuusuario.trainapp.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
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
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload

@Composable
fun WorkoutDetailScreen(
    workoutId: Long,
    onNavigateUp: () -> Unit
) {
    // Mock data
    val workout = getMockWorkoutDetail()
    var showExportDialog by remember { mutableStateOf(false) }
    var exportFormat by remember { mutableStateOf("JSON") }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = workout.routineName,
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(imageVector = Icons.Filled.FileDownload, contentDescription = "Exportar", tint = Color(0xFFFFFFFF))
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF5350))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Workout header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = workout.dayName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Text(text = workout.date, fontSize = 14.sp, color = Color(0xFF888888))
                        Text(text = workout.duration, fontSize = 14.sp, color = Color(0xFF888888))
                        Text(text = "Volumen: ${workout.volume} series", fontSize = 14.sp, color = Color(0xFF888888))
                        workout.rpeAvg?.let { Text(text = "RPE: ${String.format("%.1f", it)}", fontSize = 14.sp, color = Color(0xFF888888)) }
                    }
                }
            }

            // Sensations
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Sensaciones globales", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        sensationChip("Sueño", workout.sensationStart?.sleep ?: 0, Modifier.weight(1f))
                        sensationChip("Fatiga", workout.sensationStart?.fatigue ?: 0, Modifier.weight(1f))
                        sensationChip("Motivación", workout.sensationStart?.motivation ?: 0, Modifier.weight(1f))
                        sensationChip("Dolor", workout.sensationStart?.pain ?: 0, Modifier.weight(1f))
                    }
                }
            }

            // Blocks timeline
            workout.blocks.forEach { block ->
                SectionHeader(title = block.name)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        block.exercises.forEach { exercise ->
                            ExerciseDetailCard(exercise = exercise)
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Exportar entrenamiento", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Formato:", fontSize = 16.sp, color = Color(0xFFFFFFFF))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { exportFormat = "JSON" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (exportFormat == "JSON") Color(0xFFFFD600) else Color(0xFF2C2C2C)
                            )
                        ) { Text("JSON") }
                        Button(
                            onClick = { exportFormat = "CSV" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (exportFormat == "CSV") Color(0xFFFFD600) else Color(0xFF2C2C2C)
                            )
                        ) { Text("CSV") }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showExportDialog = false },
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

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Eliminar entrenamiento", fontWeight = FontWeight.Bold) },
            text = { Text("¿Eliminar este entrenamiento definitivamente?") },
            confirmButton = {
                Button(
                    onClick = { showDeleteConfirm = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350),
                        contentColor = Color(0xFF121212)
                    )
                ) { Text("Eliminar", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirm = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun sensationChip(label: String, value: Int, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column(
            modifier = modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = label, fontSize = 12.sp, color = Color(0xFF888888))
            Text(text = "$value/10", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
        }
    }
}

@Composable
fun ExerciseDetailCard(
    exercise: WorkoutExerciseDetail
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = exercise.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                Text(text = exercise.type, fontSize = 12.sp, color = Color(0xFF888888))
            }

            // Sets table
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("#", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.width(30.dp))
                    Text("Peso", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.weight(1f))
                    Text("Reps", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.weight(1f))
                    Text("RPE", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.weight(1f))
                    Text("Descanso", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.weight(1f))
                    Text("Duración", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.weight(1f))
                }

                exercise.sets.forEachIndexed { index, set ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("${index + 1}", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.width(30.dp))
                        Text("${set.weight} kg", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF), modifier = Modifier.weight(1f))
                        Text("${set.reps}", fontSize = 12.sp, color = Color(0xFFFFFFFF), modifier = Modifier.weight(1f))
                        Text("@ ${set.rpe}", fontSize = 12.sp, color = if (set.rpe >= 9) Color(0xFFEF5350) else Color(0xFF888888), modifier = Modifier.weight(1f))
                        Text("${set.rest}s", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.weight(1f))
                        Text("${set.duration}s", fontSize = 12.sp, color = Color(0xFF888888), modifier = Modifier.weight(1f))
                    }
                }
            }

            // Sensation
            exercise.sensation?.let { s ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Sensación: ${s.rating}/10", fontSize = 14.sp, color = Color(0xFFFFD600))
                        Text(text = s.notes, fontSize = 12.sp, color = Color(0xFF888888))
                    }
                }
            }
        }
    }
}

fun getMockWorkoutDetail(): WorkoutDetail {
    return WorkoutDetail(
        id = 1,
        routineName = "Push/Pull/Legs",
        dayName = "Día 1 - Push A",
        date = "2026-10-01",
        duration = "1h 23m",
        volume = 24,
        rpeAvg = 8.2f,
        sensationStart = Sensation(sleep = 8, fatigue = 3, motivation = 9, pain = 1, notes = "Muy bien"),
        sensationEnd = Sensation(sleep = 7, fatigue = 6, motivation = 8, pain = 2, notes = "Cansado pero contento"),
        blocks = listOf(
            WorkoutBlockDetail(
                name = "Calentamiento",
                exercises = listOf(
                    WorkoutExerciseDetail("Movilidad articular", "CIRCUIT", listOf(), null),
                    WorkoutExerciseDetail("Jumping jacks", "CIRCUIT", listOf(), null)
                )
            ),
            WorkoutBlockDetail(
                name = "Principal",
                exercises = listOf(
                    WorkoutExerciseDetail("Press banca", "SETS_REPS", listOf(
                        WorkoutSetDetail(80.0, 8, 8, 120, 45),
                        WorkoutSetDetail(80.0, 7, 9, 120, 50),
                        WorkoutSetDetail(80.0, 6, 10, 120, 55),
                        WorkoutSetDetail(80.0, 6, 10, 120, 60)
                    ), SensationDetail(rating = 8, notes = "Buena técnica")),
                    WorkoutExerciseDetail("Remo con barra", "SETS_REPS", listOf(
                        WorkoutSetDetail(60.0, 10, 7, 90, 40),
                        WorkoutSetDetail(60.0, 10, 8, 90, 42),
                        WorkoutSetDetail(60.0, 9, 8, 90, 45),
                        WorkoutSetDetail(60.0, 8, 9, 90, 48)
                    ), SensationDetail(rating = 7, notes = "Bien"))
                )
            ),
            WorkoutBlockDetail(
                name = "Calma",
                exercises = listOf(
                    WorkoutExerciseDetail("Estiramiento", "SINGLE_TIMER", listOf(), null)
                )
            )
        )
    )
}

// Mock data classes
data class WorkoutDetail(
    val id: Long,
    val routineName: String,
    val dayName: String,
    val date: String,
    val duration: String,
    val volume: Int,
    val rpeAvg: Float?,
    val sensationStart: Sensation?,
    val sensationEnd: Sensation?,
    val blocks: List<WorkoutBlockDetail>
)

data class Sensation(
    val sleep: Int,
    val fatigue: Int,
    val motivation: Int,
    val pain: Int,
    val notes: String
)

data class WorkoutBlockDetail(
    val name: String,
    val exercises: List<WorkoutExerciseDetail>
)

data class WorkoutExerciseDetail(
    val name: String,
    val type: String,
    val sets: List<WorkoutSetDetail>,
    val sensation: SensationDetail?
)

data class WorkoutSetDetail(
    val weight: Double,
    val reps: Int,
    val rpe: Int,
    val rest: Int,
    val duration: Int
)

data class SensationDetail(
    val rating: Int,
    val notes: String
)