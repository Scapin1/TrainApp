package com.tuusuario.trainapp.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.tuusuario.trainapp.ui.common.RoutineCard
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import com.tuusuario.trainapp.domain.model.Routine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow

@Composable
fun RoutineListScreen(
    onCreateRoutine: () -> Unit,
    onEditRoutine: (Long) -> Unit,
    onStartWorkout: () -> Unit,
    viewModel: RoutineListViewModel = viewModel(factory = RoutineListViewModelFactory((androidx.compose.ui.platform.LocalContext.current.applicationContext as com.tuusuario.trainapp.TrainApp).container()))
) {
    val routines by viewModel.routines.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf<Pair<Long, String>?>(null) }

    Scaffold(
        topBar = { TrainTopAppBar(title = "Mis Rutinas") },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateRoutine,
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Crear rutina") },
                text = { Text("Nueva rutina", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                containerColor = Color(0xFFFFD600),
                contentColor = Color(0xFF121212)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isLoading && routines.isNullOrEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFFD600))
                }
            } else if (routines.isNullOrEmpty()) {
                EmptyState(
                    icon = Icons.Filled.FitnessCenter,
                    title = "No hay rutinas aún",
                    message = "Crea tu primera rutina para empezar a entrenar",
                    actionText = "Crear rutina",
                    onActionClick = onCreateRoutine
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(routines) { routine: Routine ->
                        RoutineCard(
                            name = routine.name,
                            dayCount = routine.dayCount,
                            lastExecuted = null, // TODO: get from repository
                            nextDayName = null, // TODO: get from repository
                            onClick = { /* TODO: navigate to detail */ },
                            onEdit = { onEditRoutine(routine.id) },
                            onDelete = { showDeleteConfirm = routine.id to routine.name }
                        )
                    }
                }
            }

            // Quick start workout button
            Button(
                onClick = onStartWorkout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C2C2C),
                    contentColor = Color(0xFFFFFFFF)
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null, tint = Color(0xFFFFD600))
                    Spacer(Modifier.width(8.dp))
                    Text("Iniciar entrenamiento", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteConfirm?.let { (routineId, routineName) ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Eliminar rutina", fontWeight = FontWeight.Bold) },
            text = { Text("¿Eliminar \"$routineName\"? Se perderán todos sus días, bloques y ejercicios.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteRoutine(routineId); showDeleteConfirm = null },
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