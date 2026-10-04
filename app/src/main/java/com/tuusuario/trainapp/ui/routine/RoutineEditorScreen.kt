package com.tuusuario.trainapp.ui.routine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RoutineEditorScreen(
    routineId: String,
    onNavigateToDayEditor: (Long) -> Unit,
    onNavigateUp: () -> Unit
) {
    val routineName by remember { mutableStateOf("") }
}