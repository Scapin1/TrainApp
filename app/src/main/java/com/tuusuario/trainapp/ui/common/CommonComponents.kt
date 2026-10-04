package com.tuusuario.trainapp.ui.common

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.em
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.IconButton

@Composable
fun TrainTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: androidx.compose.material3.TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color(0xFF1E1E1E),
        titleContentColor = Color(0xFFFFD600),
        navigationIconContentColor = Color(0xFFFFFFFF),
        actionIconContentColor = Color(0xFFFFFFFF)
    )
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600)) },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors
    )
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF4A4A4A),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFBDBDBD)
            )
            Text(
                text = message,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF888888),
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            actionText?.let { text ->
                Button(onClick = onActionClick!!, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD600),
                    contentColor = Color(0xFF121212)
                )) {
                    Text(text = text, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    action: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
            subtitle?.let { Text(text = it, fontSize = 12.sp, color = Color(0xFF888888)) }
        }
        action?.invoke()
    }
}

@Composable
fun RoutineCard(
    name: String,
    dayCount: Int,
    lastExecuted: String?,
    nextDayName: String?,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E1E)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color(0xFFFFFFFF)
        ),
        onClick = onClick
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        onEdit?.let { IconButton(onClick = it) { Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar", tint = Color(0xFF888888)) } }
                        onDelete?.let { IconButton(onClick = it) { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF5350)) } }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "$dayCount días", fontSize = 12.sp, color = Color(0xFF888888))
                    lastExecuted?.let { Text(text = "Último: $it", fontSize = 12.sp, color = Color(0xFF888888)) }
                }
                nextDayName?.let {
                    Row {
                        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(16.dp))
                        Text(text = "Próximo: $it", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFFFFD600))
                    }
                }
            }
        }
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
    onExport: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
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
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "Confirmar",
    dismissText: String = "Cancelar",
    destructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF)) },
        text = { Text(text = message, color = Color(0xFFBDBDBD)) },
        confirmButton = {
            Button(
                onClick = { onConfirm(); onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (destructive) Color(0xFFEF5350) else Color(0xFFFFD600),
                    contentColor = Color(0xFF121212)
                )
            ) {
                Text(text = confirmText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text(text = dismissText, color = Color(0xFFBDBDBD)) }
        }
    )
}

@Composable
fun DoubleConfirmDialog(
    title: String,
    message: String,
    firstConfirmText: String = "Confirmar",
    secondConfirmText: String = "Confirmo",
    dismissText: String = "Cancelar",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var firstConfirmed by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF)) },
        text = { 
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = message, color = Color(0xFFBDBDBD))
                if (firstConfirmed) {
                    Text(text = "⚠️ Confirma de nuevo para proceder", fontSize = 12.sp, color = Color(0xFFFFD600))
                }
            }
        },
confirmButton = {
            Button(
                onClick = {
                    if (firstConfirmed) {
                        onConfirm()
                        onDismiss()
                    } else {
                        firstConfirmed = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (firstConfirmed) Color(0xFFEF5350) else Color(0xFFFFD600),
                    contentColor = Color(0xFF121212)
                )
            ) {
                Text(text = if (firstConfirmed) secondConfirmText else firstConfirmText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text(text = dismissText, color = Color(0xFFBDBDBD)) }
        }
    )
}

@Composable
fun TextInputDialog(
    title: String,
    hint: String,
    initialText: String = "",
    confirmText: String = "Guardar",
    dismissText: String = "Cancelar",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initialText) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF)) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = hint, color = Color(0xFF888888)) },
                singleLine = true,
                // colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                //     containerColor = Color(0xFF2C2C2C),
                //     focusedContainerColor = Color(0xFF2C2C2C),
                //     textColor = Color(0xFFFFFFFF),
                //     labelColor = Color(0xFF888888),
                //     cursorColor = Color(0xFFFFD600)
                // ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text); onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD600),
                    contentColor = Color(0xFF121212)
                ),
                enabled = text.isNotBlank()
            ) {
                Text(text = confirmText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text(text = dismissText, color = Color(0xFFBDBDBD)) }
        }
    )
}