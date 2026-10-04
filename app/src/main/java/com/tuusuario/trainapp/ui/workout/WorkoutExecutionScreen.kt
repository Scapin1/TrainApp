package com.tuusuario.trainapp.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import com.tuusuario.trainapp.data.db.entities.BlockType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Remove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun WorkoutExecutionScreen(
    workoutId: Long,
    onFinish: () -> Unit,
    onNavigateUp: () -> Unit
) {
    var currentBlockIndex by remember { mutableStateOf(0) }
    var currentExerciseIndex by remember { mutableStateOf(0) }
    var globalTimerSeconds by remember { mutableStateOf(0L) }
    var exerciseTimerSeconds by remember { mutableStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var isResting by remember { mutableStateOf(false) }
    var restTimerSeconds by remember { mutableStateOf(0) }
    var showSkipConfirm by remember { mutableStateOf<Int?>(null) }
    var showFinishConfirm by remember { mutableStateOf(false) }
    var showAbandonConfirm by remember { mutableStateOf(false) }
    var showSensationDialog by remember { mutableStateOf<Int?>(null) }
    var sensationRating by remember { mutableStateOf(5) }
    var sensationNotes by remember { mutableStateOf("") }

    val blocks = remember { getMockBlocks() }
    val currentBlock = blocks.getOrNull(currentBlockIndex)
    val currentExercise = currentBlock?.exercises?.getOrNull(currentExerciseIndex)
    val nextExercise = currentBlock?.exercises?.getOrNull(
        ((currentExerciseIndex + 1).takeIf { it < (currentBlock.exercises?.size ?: 0) } ?: 0)
    )

    var timerJob by remember { mutableStateOf<Job?>(null) }
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            timerJob = CoroutineScope(Dispatchers.Main).launch {
                while (isTimerRunning) {
                    globalTimerSeconds++
                    exerciseTimerSeconds++
                    if (isResting) restTimerSeconds--
                    try { Thread.sleep(1000) } catch (e: InterruptedException) { break }
                }
            }
        } else {
            timerJob?.cancel()
        }
    }

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = currentBlock?.name ?: "Entrenamiento",
                navigationIcon = { IconButton(onClick = { showAbandonConfirm = true }) { Icon(imageVector = Icons.Filled.Close, contentDescription = "Salir") } },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .width(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                        ) {
                            Text(
                                text = formatTime(globalTimerSeconds),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD600),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (currentExercise != null) {
                BottomActionBar(
                    currentExercise = currentExercise,
                    onCompleteSet = { /* TODO */ },
                    onSkipExercise = { showSkipConfirm = currentExerciseIndex },
                    onSensation = { showSensationDialog = currentExerciseIndex },
                    isResting = isResting,
                    onSkipRest = { isResting = false },
                    restTime = restTimerSeconds
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (blocks.size > 1) {
                BlockStepper(
                    blocks = blocks,
                    currentIndex = currentBlockIndex,
                    onTabClick = { currentBlockIndex = it }
                )
            }

            currentExercise?.let { exercise ->
                CurrentExerciseCard(
                    exercise = exercise,
                    blockType = currentBlock?.type ?: BlockType.SETS_REPS,
                    previousSet = null,
                    timerSeconds = exerciseTimerSeconds,
                    isResting = isResting,
                    restTime = restTimerSeconds,
                    onSetComplete = { weight, reps, rpe -> /* TODO: log set */ },
                    onSensation = { showSensationDialog = currentExerciseIndex }
                )
            }

            nextExercise?.let { exercise ->
                NextExercisePreview(exercise = exercise)
            }

            if (isResting) {
                RestOverlay(
                    timeRemaining = restTimerSeconds,
                    onSkip = { isResting = false }
                )
            }
        }
    }

    if (showSkipConfirm != null) {
        com.tuusuario.trainapp.ui.common.DoubleConfirmDialog(
            title = "Saltar ejercicio",
            message = "¿Seguro que quieres saltar \"${currentExercise?.name}\"? Se registrará como omitido.",
            onConfirm = { /* TODO: skip exercise */ showSkipConfirm = null },
            onDismiss = { showSkipConfirm = null }
        )
    }

    if (showFinishConfirm) {
        com.tuusuario.trainapp.ui.common.ConfirmDialog(
            title = "Finalizar entrenamiento",
            message = "¿Guardar y finalizar el entrenamiento?",
            confirmText = "Finalizar",
            onConfirm = { onFinish() },
            onDismiss = { showFinishConfirm = false }
        )
    }

    if (showAbandonConfirm) {
        com.tuusuario.trainapp.ui.common.ConfirmDialog(
            title = "Abandonar entrenamiento",
            message = "¿Salir sin guardar? Se perderá todo el progreso de esta sesión.",
            confirmText = "Abandonar",
            destructive = true,
            onConfirm = { onFinish() },
            onDismiss = { showAbandonConfirm = false }
        )
    }

    showSensationDialog?.let { index ->
        AlertDialog(
            onDismissRequest = { showSensationDialog = null },
            title = { Text("Sensación: ${blocks[currentBlockIndex]?.exercises?.get(index)?.name}", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("¿Cómo te sentiste? (1-10)", fontSize = 16.sp, color = Color(0xFFFFFFFF))
                    Slider(
                        value = sensationRating.toFloat(),
                        onValueChange = { sensationRating = it.toInt() },
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
                    Text(text = "Rating: $sensationRating", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600), textAlign = TextAlign.Center)
                    TextField(
                        value = sensationNotes,
                        onValueChange = { sensationNotes = it },
                        label = { Text("Notas (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { /* TODO: save sensation */ showSensationDialog = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600),
                        contentColor = Color(0xFF121212)
                    )
                ) { Text("Guardar", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                Button(onClick = { showSensationDialog = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun BlockStepper(
    blocks: List<WorkoutBlock>,
    currentIndex: Int,
    onTabClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        blocks.forEachIndexed { index, block ->
            val isCurrent = index == currentIndex
            val isCompleted = index < currentIndex
            Button(
                onClick = { onTabClick(index) },
                modifier = Modifier.weight(1f, fill = false),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        isCurrent -> Color(0xFFFFD600)
                        isCompleted -> Color(0xFF4CAF50)
                        else -> Color(0xFF2C2C2C)
                    }
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isCompleted) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Color(0xFF121212), modifier = Modifier.size(16.dp))
                    }
                    Text(block.name, color = when {
                        isCurrent -> Color(0xFF121212)
                        isCompleted -> Color(0xFF121212)
                        else -> Color(0xFFFFFFFF)
                    })
                }
            }
        }
    }
}

@Composable
fun CurrentExerciseCard(
    exercise: WorkoutExerciseItem,
    blockType: BlockType,
    previousSet: WorkoutSet?,
    timerSeconds: Long,
    isResting: Boolean,
    restTime: Int,
    onSetComplete: (Double, Int, Int) -> Unit,
    onSensation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = exercise.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    Text(text = exercise.targetDescription, fontSize = 14.sp, color = Color(0xFF888888))
                }
                IconButton(onClick = onSensation) {
                    Icon(imageVector = Icons.Filled.SentimentSatisfied, contentDescription = "Sensación", tint = Color(0xFFFFD600))
                }
            }

            if (blockType != BlockType.SETS_REPS) {
                ExerciseTimer(timerSeconds = timerSeconds, isRunning = true)
            }

            if (blockType == BlockType.SETS_REPS) {
                SetLogger(
                    previousSet = previousSet,
                    onComplete = onSetComplete
                )
            }

            if (blockType == BlockType.CIRCUIT_TIME || blockType == BlockType.EMOM || blockType == BlockType.HIIT) {
                CircuitProgress(
                    currentRound = 1,
                    totalRounds = exercise.targetRounds,
                    exerciseIndex = 0,
                    totalExercises = 1
                )
            }
        }
    }
}

@Composable
fun SetLogger(
    previousSet: WorkoutSet?,
    onComplete: (Double, Int, Int) -> Unit
) {
    var weight by remember { mutableStateOf(previousSet?.weight ?: 0.0) }
    var reps by remember { mutableStateOf(previousSet?.reps ?: 0) }
    var rpe by remember { mutableStateOf(previousSet?.rpe ?: 8) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        previousSet?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Serie anterior", fontSize = 12.sp, color = Color(0xFF888888))
                    Text("${it.weight} kg × ${it.reps} @ RPE ${it.rpe}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Peso (kg)", fontSize = 16.sp, color = Color(0xFFBDBDBD), modifier = Modifier.width(80.dp))
            TextField(
                value = weight.toString(),
                onValueChange = { weight = it.toDoubleOrNull() ?: 0.0 },
                
                singleLine = true,
                modifier = Modifier.weight(1f, fill = false)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Reps", fontSize = 16.sp, color = Color(0xFFBDBDBD), modifier = Modifier.width(80.dp))
            TextField(
                value = reps.toString(),
                onValueChange = { reps = it.toIntOrNull() ?: 0 },
                
                singleLine = true,
                modifier = Modifier.weight(1f, fill = false)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "RPE: $rpe", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                Text(text = rpeDescription(rpe), fontSize = 12.sp, color = Color(0xFF888888))
            }
            Slider(
                value = rpe.toFloat(),
                onValueChange = { rpe = it.toInt() },
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

        Button(
            onClick = { onComplete(weight, reps, rpe) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD600),
                contentColor = Color(0xFF121212)
            ),
            enabled = weight > 0 && reps > 0
        ) {
            Text("Completar serie", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun ExerciseTimer(
    timerSeconds: Long,
    isRunning: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val progress = (timerSeconds % 300) / 300f
            drawArc(
                color = Color(0xFF2C2C2C),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx())
            )
            drawArc(
                color = Color(0xFFFFD600),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = formatTime(timerSeconds), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
            Text(text = "Tiempo ejercicio", fontSize = 12.sp, color = Color(0xFF888888))
        }
    }
}

@Composable
fun CircuitProgress(
    currentRound: Int,
    totalRounds: Int,
    exerciseIndex: Int,
    totalExercises: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Ronda $currentRound de $totalRounds", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                Text("Ejercicio ${exerciseIndex + 1} de $totalExercises", fontSize = 14.sp, color = Color(0xFF888888))
            }
            CustomProgressBar(
                progress = (currentRound - 1 + exerciseIndex.toFloat() / totalExercises) / totalRounds,
                color = Color(0xFFFFD600),
                trackColor = Color(0xFF2C2C2C),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NextExercisePreview(exercise: WorkoutExerciseItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.SkipNext, contentDescription = null, tint = Color(0xFF888888))
                Column {
                    Text("Siguiente", fontSize = 12.sp, color = Color(0xFF888888))
                    Text(exercise.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                }
            }
            Text(exercise.targetDescription, fontSize = 12.sp, color = Color(0xFF888888))
        }
    }
}

@Composable
fun RestOverlay(
    timeRemaining: Int,
    onSkip: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000))
        )
        
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("DESCANSO", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
                Text(formatTime(timeRemaining.toLong()), fontSize = 64.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                Text("Toca para saltar", fontSize = 14.sp, color = Color(0xFF888888))
                Button(
                    onClick = onSkip,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600),
                        contentColor = Color(0xFF121212)
                    )
                ) {
                    Text("Saltar descanso", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BottomActionBar(
    currentExercise: WorkoutExerciseItem,
    onCompleteSet: () -> Unit,
    onSkipExercise: () -> Unit,
    onSensation: () -> Unit,
    isResting: Boolean,
    onSkipRest: () -> Unit,
    restTime: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (isResting) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Descanso: ${formatTime(restTime.toLong())}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
                    Button(
                        onClick = onSkipRest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD600),
                            contentColor = Color(0xFF121212)
                        )
                    ) {
                        Text("Saltar", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onCompleteSet,
                        modifier = Modifier.weight(1f, fill = false),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD600),
                            contentColor = Color(0xFF121212)
                        )
                    ) {
                        Text("Completar serie", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onSensation,
                        modifier = Modifier.width(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2C2C2C),
                            contentColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.SentimentSatisfied, contentDescription = "Sensación")
                    }
                    IconButton(onClick = onSkipExercise) {
                        Icon(imageVector = Icons.Filled.SkipNext, contentDescription = "Saltar ejercicio", tint = Color(0xFFEF5350))
                    }
                }
            }
        }
    }
}

@Composable
fun CustomProgressBar(
    progress: Float,
    color: Color,
    trackColor: Color,
    modifier: Modifier = Modifier
) {
    val progressValue = progress.coerceIn(0f, 1f)
    Row(
        modifier = modifier.fillMaxWidth().height(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(progressValue, fill = false)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        if (progressValue < 1f) {
            Box(
                modifier = Modifier
                    .weight(1f - progressValue, fill = false)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(trackColor)
            )
        }
    }
}

fun formatTime(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) "%02d:%02d:%02d".format(h, m, s) else "%02d:%02d".format(m, s)
}

fun rpeDescription(rpe: Int): String {
    return when (rpe) {
        1 -> "Muy fácil"
        2 -> "Fácil"
        3 -> "Ligeramente fácil"
        4 -> "Moderado-fácil"
        5 -> "Moderado"
        6 -> "Moderado-duro"
        7 -> "Duro"
        8 -> "Muy duro (2 reps en reserva)"
        9 -> "Casi fallo (1 rep en reserva)"
        10 -> "Fallo muscular"
        else -> ""
    }
}

data class WorkoutBlock(
    val name: String,
    val type: BlockType,
    val exercises: List<WorkoutExerciseItem>? = null
)

data class WorkoutExerciseItem(
    val name: String,
    val targetDescription: String,
    val targetRounds: Int = 1
)

data class WorkoutSet(
    val weight: Double,
    val reps: Int,
    val rpe: Int
)

fun getMockBlocks(): List<WorkoutBlock> {
    return listOf(
        WorkoutBlock("Calentamiento", BlockType.CIRCUIT_TIME, listOf(
            WorkoutExerciseItem("Movilidad articular", "3 min", 1),
            WorkoutExerciseItem("Jumping jacks", "50 reps", 1),
            WorkoutExerciseItem("Sentadillas con salto", "20 reps", 1)
        )),
        WorkoutBlock("Principal", BlockType.SETS_REPS, listOf(
            WorkoutExerciseItem("Press banca", "4 series × 6-8 reps @ 80kg"),
            WorkoutExerciseItem("Remo con barra", "4 series × 8-10 reps @ 60kg"),
            WorkoutExerciseItem("Sentadilla", "4 series × 8-10 reps @ 100kg"),
            WorkoutExerciseItem("Press militar", "3 series × 8-10 reps @ 40kg"),
            WorkoutExerciseItem("Peso muerto", "3 series × 6-8 reps @ 120kg")
        )),
        WorkoutBlock("Calma", BlockType.SINGLE_TIMER, listOf(
            WorkoutExerciseItem("Estiramiento completo", "5 min")
        ))
    )
}