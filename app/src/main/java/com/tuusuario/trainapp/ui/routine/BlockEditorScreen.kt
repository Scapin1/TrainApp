package com.tuusuario.trainapp.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuusuario.trainapp.ui.common.EmptyState
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import com.tuusuario.trainapp.data.db.entities.BlockType
import com.tuusuario.trainapp.domain.model.TemplateExercise
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun BlockEditorScreen(
    routineId: String,
    dayId: Long,
    blockId: Long,
    onNavigateToExercisePicker: () -> Unit,
    onNavigateUp: () -> Unit
) {
    var exercises by remember { mutableStateOf<List<TemplateExercise>>(emptyList()) }
    var blockName by remember { mutableStateOf("Bloque principal") }
    var selectedBlockType by remember { mutableStateOf(BlockType.SETS_REPS) }
    var showConfigDialog by remember { mutableStateOf(false) }

    val blockTypes = listOf(
        BlockType.SETS_REPS,
        BlockType.CIRCUIT_TIME,
        BlockType.EMOM,
        BlockType.HIIT,
        BlockType.SINGLE_TIMER,
        BlockType.MIXED
    )

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = "Editar bloque",
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToExercisePicker,
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir ejercicio") },
                text = { Text("Añadir ejercicio", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                containerColor = Color(0xFFFFD600),
                contentColor = Color(0xFF121212)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Block config
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextField(
                        value = blockName,
                        onValueChange = { blockName = it },
                        label = { Text("Nombre del bloque", color = Color(0xFF888888)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(text = "Tipo de bloque", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    LazyColumn(
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 0.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(blockTypes) { type ->
                            Button(
                                onClick = { selectedBlockType = type },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedBlockType == type) Color(0xFFFFD600) else Color(0xFF2C2C2C)
                                )
                            ) {
                                Text(type.name.replace("_", " "), color = if (selectedBlockType == type) Color(0xFF121212) else Color(0xFFFFFFFF))
                            }
                        }
                    }

                    Button(
                        onClick = { showConfigDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2C2C2C),
                            contentColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Text("Configurar parámetros del bloque (próximamente)")
                    }
                }
            }

            // Exercises list
            SectionHeader(title = "Ejercicios (${exercises.size})", action = {
                Button(onClick = onNavigateToExercisePicker) { Text("Añadir", color = Color(0xFFFFD600)) }
            })

            if (exercises.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.FitnessCenter,
                    title = "Sin ejercicios",
                    message = "Añade ejercicios a este bloque",
                    actionText = "Añadir ejercicio",
                    onActionClick = onNavigateToExercisePicker,
                    modifier = Modifier.padding(padding).fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(exercises) { exercise: TemplateExercise ->
                        ExerciseTemplateItem(
                            exercise = exercise,
                            index = exercises.indexOf(exercise),
                            onMoveUp = { if (exercises.indexOf(exercise) > 0) moveExercise(exercise.id, exercises.indexOf(exercise) - 1) },
                            onMoveDown = { if (exercises.indexOf(exercise) < exercises.size - 1) moveExercise(exercise.id, exercises.indexOf(exercise) + 1) },
                            onDelete = { deleteExercise(exercise.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseTemplateItem(
    exercise: TemplateExercise,
    index: Int,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        onClick = { /* TODO: edit exercise config */ }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "#${index + 1}", fontSize = 14.sp, color = Color(0xFF888888))
                    Text(text = "Ejercicio #${exercise.exerciseId}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                }
                Text(text = "Config: ${exercise.configJson}", fontSize = 12.sp, color = Color(0xFF888888))
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onMoveUp) { Icon(imageVector = Icons.Filled.ArrowUpward, contentDescription = "Subir", tint = Color(0xFF888888)) }
                IconButton(onClick = onMoveDown) { Icon(imageVector = Icons.Filled.ArrowDownward, contentDescription = "Bajar", tint = Color(0xFF888888)) }
                IconButton(onClick = onDelete) { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF5350)) }
            }
        }
    }
}

fun moveExercise(exerciseId: Long, newIndex: Int) { /* TODO */ }
fun deleteExercise(exerciseId: Long) { /* TODO */ }