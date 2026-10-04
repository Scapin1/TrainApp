package com.tuusuario.trainapp.ui.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuusuario.trainapp.ui.common.EmptyState
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import com.tuusuario.trainapp.domain.model.Block
import com.tuusuario.trainapp.data.db.entities.BlockType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Layers

@Composable
fun DayEditorScreen(
    routineId: String,
    dayId: Long,
    onNavigateToBlockEditor: (Long) -> Unit,
    onNavigateUp: () -> Unit
) {
    // TODO: Connect to DayEditorViewModel
    var blocks by remember { mutableStateOf<List<Block>>(emptyList()) }
    var showAddBlockDialog by remember { mutableStateOf(false) }
    var selectedBlockType by remember { mutableStateOf<BlockType?>(null) }

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = "Editar día",
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddBlockDialog = true },
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir bloque") },
                text = { Text("Añadir bloque", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                containerColor = Color(0xFFFFD600),
                contentColor = Color(0xFF121212)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Predefined blocks info
            SectionHeader(title = "Bloques predefinidos", subtitle = "Mínimo 3: Calentamiento, Principal, Calma")

            if (blocks.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.Layers,
                    title = "Sin bloques",
                    message = "Añade bloques para definir la estructura del día",
                    actionText = "Añadir bloque",
                    onActionClick = { showAddBlockDialog = true }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(blocks) { block: Block ->
                        BlockItem(
                            block = block,
                            onClick = { onNavigateToBlockEditor(block.id) },
                            onMoveUp = { /* TODO */ },
                            onMoveDown = { /* TODO */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BlockItem(
    block: Block,
    onClick: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
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
                    Text(text = block.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    BlockTypeChip(block.type)
                }
                Text(text = "${block.configJson} ejercicios", fontSize = 12.sp, color = Color(0xFF888888))
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onMoveUp) { Icon(imageVector = Icons.Filled.ArrowUpward, contentDescription = "Subir", tint = Color(0xFF888888)) }
                IconButton(onClick = onMoveDown) { Icon(imageVector = Icons.Filled.ArrowDownward, contentDescription = "Bajar", tint = Color(0xFF888888)) }
            }
        }
    }
}

@Composable
fun BlockTypeChip(type: BlockType) {
    val (label, color) = when (type) {
        BlockType.SETS_REPS -> "Fuerza" to Color(0xFF4CAF50)
        BlockType.CIRCUIT_TIME -> "Circuito" to Color(0xFF2196F3)
        BlockType.EMOM -> "EMOM" to Color(0xFFFF9800)
        BlockType.HIIT -> "HIIT" to Color(0xFFEF5350)
        BlockType.SINGLE_TIMER -> "Timer" to Color(0xFF9C27B0)
        BlockType.MIXED -> "Mixto" to Color(0xFF795548)
    }
    Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF121212),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .background(color, RoundedCornerShape(8.dp))
    )
}