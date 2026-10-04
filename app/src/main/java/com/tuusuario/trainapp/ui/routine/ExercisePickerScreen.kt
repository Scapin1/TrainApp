package com.tuusuario.trainapp.ui.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuusuario.trainapp.ui.common.EmptyState
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import com.tuusuario.trainapp.domain.model.ExerciseCatalog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun ExercisePickerScreen(
    routineId: String,
    dayId: Long,
    blockId: Long,
    onExerciseSelected: (ExerciseCatalog) -> Unit,
    onNavigateUp: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedMuscle by remember { mutableStateOf<String?>(null) }
    var selectedPattern by remember { mutableStateOf<String?>(null) }
    var selectedEquipment by remember { mutableStateOf<String?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var newExerciseName by remember { mutableStateOf("") }

    // Mock data - TODO: Connect to ExerciseRepository
    val categories = listOf("Todos", "Empuje", "Tirón", "Piernas", "Core", "Cardio", "Full Body", "Accesorios", "Personalizados")
    val exercises = remember { mutableStateOf<List<ExerciseCatalog>>(getMockExercises()) }
    val filteredExercises = exercises.value.filter { ex ->
        (searchQuery.isBlank() || ex.name.lowercase().contains(searchQuery.lowercase())) &&
        (selectedCategory == null || selectedCategory == "Todos") && // categoryId not name
        (selectedMuscle == null || ex.primaryMuscle.name == selectedMuscle) &&
        (selectedPattern == null || ex.pattern.name == selectedPattern) &&
        (selectedEquipment == null || ex.equipment.name == selectedEquipment)
    }

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = "Seleccionar ejercicio",
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } },
                actions = {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Crear ejercicio", tint = Color(0xFFFFFFFF))
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
                    placeholder = { Text("Buscar ejercicios…", color = Color(0xFF888888)) },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color(0xFF888888)) },
                    singleLine = true,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Filter chips
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChipRow(
                        title = "Categoría",
                        options = categories,
                        selected = selectedCategory,
                        onSelect = { selectedCategory = if (it == "Todos") null else it }
                    )
                    FilterChipRow(
                        title = "Músculo",
                        options = listOf("Todos", "Pecho", "Espalda", "Hombros", "Tríceps", "Bíceps", "Cuádriceps", "Femoral", "Glúteos", "Pantorrilla", "Core"),
                        selected = selectedMuscle,
                        onSelect = { selectedMuscle = if (it == "Todos") null else it }
                    )
                    FilterChipRow(
                        title = "Patrón",
                        options = listOf("Todos", "Empuje H", "Empuje V", "Tirón H", "Tirón V", "Sentadilla", "Hinge", "Zancada", "Core", "Carry", "Cardio"),
                        selected = selectedPattern,
                        onSelect = { selectedPattern = if (it == "Todos") null else it }
                    )
                    FilterChipRow(
                        title = "Equipo",
                        options = listOf("Todos", "Barra", "Mancuernas", "Kettlebell", "Máquina", "Polea", "Bandas", "TRX", "Peso corporal"),
                        selected = selectedEquipment,
                        onSelect = { selectedEquipment = if (it == "Todos") null else it }
                    )
                }
            }

            // Exercise list
            SectionHeader(title = "Ejercicios (${filteredExercises.size})")

            if (filteredExercises.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.FitnessCenter,
                    title = "Sin resultados",
                    message = "Prueba a cambiar los filtros o busca otro término",
                    modifier = Modifier.padding(padding).fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredExercises) { exercise: ExerciseCatalog ->
                        ExerciseCatalogItem(
                            exercise = exercise,
                            isCustom = exercise.isCustom,
                            onClick = { onExerciseSelected(exercise) }
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Crear ejercicio personalizado", fontWeight = FontWeight.Bold) },
            text = {
                TextField(
                    value = newExerciseName,
                    onValueChange = { newExerciseName = it },
                    label = { Text("Nombre del ejercicio") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        // TODO: Create custom exercise
                        showCreateDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600),
                        contentColor = Color(0xFF121212)
                    ),
                    enabled = newExerciseName.isNotBlank()
                ) { Text("Crear", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                Button(onClick = { showCreateDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun FilterChipRow(
    title: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF888888))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(options) { option ->
                Button(
                    onClick = { onSelect(option) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected == option || (selected == null && option == "Todos")) Color(0xFFFFD600) else Color(0xFF2C2C2C)
                    )
                ) {
                    Text(option, color = if (selected == option || (selected == null && option == "Todos")) Color(0xFF121212) else Color(0xFFFFFFFF))
                }
            }
        }
    }
}

@Composable
fun ExerciseCatalogItem(
    exercise: ExerciseCatalog,
    isCustom: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        onClick = onClick
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
                    Text(text = exercise.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    if (isCustom) {
                        Text(
                            text = "Personalizado",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF121212),
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                .background(Color(0xFFFF9800), RoundedCornerShape(8.dp))
                        )
                    }
                    val unilateral = exercise.unilateral
                    val bilateral = exercise.bilateral
                    if (unilateral || bilateral) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (unilateral) {
                                Text(
                                    text = "Unilateral",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF121212),
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                        .background(Color(0xFF2196F3), RoundedCornerShape(8.dp))
                                )
                            }
                            if (bilateral) {
                                Text(
                                    text = "Bilateral",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF121212),
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                        .background(Color(0xFF4CAF50), RoundedCornerShape(8.dp))
                                )
                            }
                        }
                    }
                }
                Text(text = "${exercise.pattern.name} • ${exercise.primaryMuscle.name} • ${exercise.equipment.name}", fontSize = 12.sp, color = Color(0xFF888888))
            }
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF888888))
        }
    }
}

private fun getMockExercises(): List<ExerciseCatalog> {
    return listOf(
        ExerciseCatalog(id = 1, name = "Press banca", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.PUSH_HORIZ, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.CHEST, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.BARBELL, unilateral = false, bilateral = true, notes = "", aliases = listOf("Banca plana", "Bench press"), categoryId = 1, isCustom = false),
        ExerciseCatalog(id = 2, name = "Sentadilla trasera", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.SQUAT, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.QUADS, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.BARBELL, unilateral = false, bilateral = true, notes = "", aliases = listOf("Back squat", "Squat"), categoryId = 3, isCustom = false),
        ExerciseCatalog(id = 3, name = "Peso muerto", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.HINGE, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.HAMSTRINGS, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.BARBELL, unilateral = false, bilateral = true, notes = "", aliases = listOf("Deadlift"), categoryId = 3, isCustom = false),
        ExerciseCatalog(id = 4, name = "Dominadas", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.PULL_VERT, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.BACK, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.BODYWEIGHT, unilateral = false, bilateral = true, notes = "", aliases = listOf("Pull ups", "Chin ups"), categoryId = 2, isCustom = false),
        ExerciseCatalog(id = 5, name = "Press militar", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.PUSH_VERT, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.SHOULDERS, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.BARBELL, unilateral = false, bilateral = true, notes = "", aliases = listOf("Overhead press", "OHP"), categoryId = 1, isCustom = false),
        ExerciseCatalog(id = 6, name = "Remo con barra", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.PULL_HORIZ, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.BACK, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.BARBELL, unilateral = false, bilateral = true, notes = "", aliases = listOf("Barbell row", "Bent over row"), categoryId = 2, isCustom = false),
        ExerciseCatalog(id = 7, name = "Zancadas", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.LUNGE, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.QUADS, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.DUMBBELL, unilateral = true, bilateral = false, notes = "", aliases = listOf("Lunges"), categoryId = 3, isCustom = false),
        ExerciseCatalog(id = 8, name = "Plancha abdominal", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.CORE, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.CORE, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.BODYWEIGHT, unilateral = false, bilateral = true, notes = "", aliases = listOf("Plank", "Front plank"), categoryId = 4, isCustom = false),
        ExerciseCatalog(id = 9, name = "Swing kettlebell", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.HINGE, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.GLUTES, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.KETTLEBELL, unilateral = false, bilateral = true, notes = "", aliases = listOf("KB swing"), categoryId = 6, isCustom = false),
        ExerciseCatalog(id = 10, name = "Burpees", pattern = com.tuusuario.trainapp.data.db.entities.ExercisePattern.CARDIO, primaryMuscle = com.tuusuario.trainapp.data.db.entities.MuscleGroup.FULL_BODY, equipment = com.tuusuario.trainapp.data.db.entities.Equipment.BODYWEIGHT, unilateral = false, bilateral = true, notes = "", aliases = listOf("Burpee"), categoryId = 5, isCustom = false)
    )
}