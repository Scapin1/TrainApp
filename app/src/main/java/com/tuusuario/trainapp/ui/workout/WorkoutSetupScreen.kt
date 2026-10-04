package com.tuusuario.trainapp.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
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
import com.tuusuario.trainapp.ui.common.EmptyState
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import com.tuusuario.trainapp.domain.model.Routine
import com.tuusuario.trainapp.domain.model.RoutineDay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter

@Composable
fun WorkoutSetupScreen(
    onStartWorkout: (Long) -> Unit,
    onNavigateUp: () -> Unit
) {
    val routines by remember { mutableStateOf<List<Routine>>(getMockRoutines()) }
    var selectedRoutine by remember { mutableStateOf<Routine?>(null) }
    var showManualPicker by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf<RoutineDay?>(null) }

    val nextDay = selectedRoutine?.let { routine ->
        routine.dayCount > 0
    } ?: false

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = "Iniciar entrenamiento",
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader(title = "Seleccionar rutina")
            
            if (routines.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.FitnessCenter,
                    title = "Sin rutinas",
                    message = "Crea una rutina primero en la pestaña Rutinas",
                    actionText = "Ir a rutinas",
                    onActionClick = { /* TODO */ },
                    modifier = Modifier.padding(padding).fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize().weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(routines) { routine: Routine ->
                        RoutineSelectorCard(
                            routine = routine,
                            isSelected = selectedRoutine?.id == routine.id,
                            onClick = { selectedRoutine = routine; showManualPicker = false }
                        )
                    }
                }
            }

            if (selectedRoutine != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "Modo de inicio", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        Text(text = selectedRoutine!!.name, fontSize = 14.sp, color = Color(0xFF888888))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { showManualPicker = false },
                                modifier = Modifier.weight(1f, fill = false),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!showManualPicker) Color(0xFFFFD600) else Color(0xFF2C2C2C)
                                )
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null, tint = if (!showManualPicker) Color(0xFF121212) else Color(0xFFFFFFFF))
                                    Text("Continuar (día siguiente)", color = if (!showManualPicker) Color(0xFF121212) else Color(0xFFFFFFFF))
                                }
                            }
                            Button(
                                onClick = { showManualPicker = true },
                                modifier = Modifier.weight(1f, fill = false),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (showManualPicker) Color(0xFFFFD600) else Color(0xFF2C2C2C)
                                )
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null, tint = if (showManualPicker) Color(0xFF121212) else Color(0xFFFFFFFF))
                                    Text("Elegir día", color = if (showManualPicker) Color(0xFF121212) else Color(0xFFFFFFFF))
                                }
                            }
                        }
                    }
                }

                if (!showManualPicker) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Próximo día", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                                Text("Día 1 - Push A", fontSize = 12.sp, color = Color(0xFF888888))
                            }
                            Text("4 bloques • ~12 ejercicios • ~75 min", fontSize = 12.sp, color = Color(0xFF888888))
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Elegir día manual", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                            Text("Selecciona qué día de la rutina hacer hoy", fontSize = 12.sp, color = Color(0xFF888888))
                            
                            LazyColumn(
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items((1..selectedRoutine!!.dayCount).toList()) { dayIndex ->
                                    Button(
                                        onClick = { selectedDay = RoutineDay(routineId = selectedRoutine!!.id, dayIndex = dayIndex - 1, name = "Día $dayIndex") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedDay?.dayIndex == dayIndex - 1) Color(0xFFFFD600) else Color(0xFF2C2C2C)
                                        )
                                    ) {
                                        Text("Día $dayIndex", color = if (selectedDay?.dayIndex == dayIndex - 1) Color(0xFF121212) else Color(0xFFFFFFFF))
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { 
                        val workoutId = System.currentTimeMillis()
                        onStartWorkout(workoutId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD600),
                        contentColor = Color(0xFF121212)
                    ),
                    enabled = selectedRoutine != null && (!showManualPicker || selectedDay != null)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null, tint = Color(0xFF121212))
                        androidx.compose.foundation.layout.Spacer(Modifier.width(8.dp))
                        Text("Comenzar entrenamiento", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun RoutineSelectorCard(
    routine: Routine,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF3D3D00) else Color(0xFF1E1E1E),
            contentColor = Color(0xFFFFFFFF)
        ),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = routine.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                if (isSelected) {
                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null, tint = Color(0xFFFFD600))
                }
            }
            Text(text = "${routine.dayCount} días • Lineal cíclico", fontSize = 12.sp, color = Color(0xFF888888))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                ) {
                    Text("Lineal")
                }
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C))
                ) {
                    Text("Cíclico")
                }
            }
        }
    }
}

private fun getMockRoutines(): List<Routine> {
    return listOf(
        Routine(id = 1, name = "Push/Pull/Legs 6x", description = "Clásico PPL de 6 días", dayCount = 6),
        Routine(id = 2, name = "Upper/Lower 4x", description = "Torso/Pierna 4 días", dayCount = 4),
        Routine(id = 3, name = "Full Body 3x", description = "Cuerpo completo 3 días", dayCount = 3)
    )
}