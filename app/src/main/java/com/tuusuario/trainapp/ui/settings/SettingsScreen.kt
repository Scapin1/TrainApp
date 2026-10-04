package com.tuusuario.trainapp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShowChart

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit
) {
    // Settings state
    var calc1RM by remember { mutableStateOf(true) }
    var calcVolume by remember { mutableStateOf(true) }
    var calcTonnage by remember { mutableStateOf(true) }
    var calcWeightSuggestion by remember { mutableStateOf(true) }
    var calcRPE by remember { mutableStateOf(true) }
    var calcMuscleVolume by remember { mutableStateOf(true) }

    var chartVolume by remember { mutableStateOf(true) }
    var chartHeatmap by remember { mutableStateOf(true) }
    var chart1RM by remember { mutableStateOf(true) }
    var chartTonnage by remember { mutableStateOf(true) }
    var chartRPE by remember { mutableStateOf(true) }
    var chartTime by remember { mutableStateOf(true) }

    var backupAuto by remember { mutableStateOf(true) }
    var lastBackup by remember { mutableStateOf("2026-10-01 03:00") }

    var notifWeighin by remember { mutableStateOf(true) }
    var notifInactivity by remember { mutableStateOf(true) }
    var inactivityDays by remember { mutableStateOf(3) }

    var accentColor by remember { mutableStateOf(0xFFFFD600) }
    var animationIntensity by remember { mutableStateOf(1) } // 0=low, 1=medium, 2=high

    var showClearConfirm by remember { mutableStateOf(false) }
    var clearConfirmText by remember { mutableStateOf("") }
    var showExportDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = "Configuración",
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cálculos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = Color(0xFFFFD600))
                            Text(text = "Cálculos automáticos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        }
                    }
                    Text(text = "Activa/desactiva qué métricas se calculan automáticamente. Las fórmulas se explican al tocar el icono de info.", fontSize = 12.sp, color = Color(0xFF888888))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingToggle(title = "1RM Estimado", subtitle = "Epley: peso × (1 + reps/30) | Brzycki: peso × 36/(37 - reps)", checked = calc1RM, onCheckedChange = { calc1RM = it }, showInfo = true)
                        SettingToggle(title = "Volumen", subtitle = "Series completadas × reps por grupo muscular", checked = calcVolume, onCheckedChange = { calcVolume = it }, showInfo = true)
                        SettingToggle(title = "Tonelaje", subtitle = "Σ (peso × reps × series completadas) por ejercicio", checked = calcTonnage, onCheckedChange = { calcTonnage = it }, showInfo = true)
                        SettingToggle(title = "Sugerencia peso siguiente", subtitle = "RPE < 8: +2.5kg | RPE > 9: -2.5kg | Objetivo RPE 8", checked = calcWeightSuggestion, onCheckedChange = { calcWeightSuggestion = it }, showInfo = true)
                        SettingToggle(title = "Tendencia RPE", subtitle = "Promedio RPE semanal para detectar fatiga", checked = calcRPE, onCheckedChange = { calcRPE = it }, showInfo = true)
                        SettingToggle(title = "Volumen por músculo", subtitle = "Distribución de series por grupo muscular (empuje/tirón/piernas/core)", checked = calcMuscleVolume, onCheckedChange = { calcMuscleVolume = it }, showInfo = true)
                    }
                }
            }

            // Gráficos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.ShowChart, contentDescription = null, tint = Color(0xFFFFD600))
                            Text(text = "Gráficos visibles", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        }
                    }
                    Text(text = "Muestra u oculta cada gráfico en la pantalla de Estadísticas", fontSize = 12.sp, color = Color(0xFF888888))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingToggle(title = "Volumen semanal (barras apiladas)", subtitle = "Empuje/Tirón/Piernas/Core por semana", checked = chartVolume, onCheckedChange = { chartVolume = it })
                        SettingToggle(title = "Heatmap frecuencia (GitHub)", subtitle = "Mapa de calor 52 semanas × 7 días", checked = chartHeatmap, onCheckedChange = { chartHeatmap = it })
                        SettingToggle(title = "1RM Timeline", subtitle = "Líneas por ejercicio principal (Epley/Brzycki)", checked = chart1RM, onCheckedChange = { chart1RM = it })
                        SettingToggle(title = "Tonelaje por músculo", subtitle = "Evolución semanal peso × reps × series", checked = chartTonnage, onCheckedChange = { chartTonnage = it })
                        SettingToggle(title = "Tendencia RPE", subtitle = "Línea de RPE promedio semanal", checked = chartRPE, onCheckedChange = { chartRPE = it })
                        SettingToggle(title = "Tiempo efectivo", subtitle = "Entrenando vs Descanso vs Total por sesión", checked = chartTime, onCheckedChange = { chartTime = it })
                    }
                }
            }

            // Backup
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Storage, contentDescription = null, tint = Color(0xFFFFD600))
                            Text(text = "Backup y datos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        }
                    }
                    Text(text = "Exporta/importa tus datos. El backup automático diario guarda los últimos 30 días.", fontSize = 12.sp, color = Color(0xFF888888))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingAction(title = "Exportar backup completo (JSON)", subtitle = "Rutinas, entrenamientos, peso, settings, catálogo", icon = Icons.Filled.FileDownload, onClick = { /* TODO */ })
                        SettingAction(title = "Exportar entrenamientos (CSV)", subtitle = "Plano para Excel/Sheets: fecha, rutina, ejercicio, serie, peso, reps, RPE", icon = Icons.Filled.FileDownload, onClick = { /* TODO */ })
                        SettingAction(title = "Importar backup", subtitle = "Restaura desde archivo JSON", icon = Icons.Filled.UploadFile, onClick = { /* TODO */ })
                        SettingToggle(title = "Backup automático diario", subtitle = "Se ejecuta a las 03:00. Conserva últimos 30 archivos.", checked = backupAuto, onCheckedChange = { backupAuto = it })
                        SettingInfo(title = "Último backup automático", value = lastBackup)
                    }
                }
            }

            // Notificaciones
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Notifications, contentDescription = null, tint = Color(0xFFFFD600))
                            Text(text = "Notificaciones", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        }
                    }
                    Text(text = "Configura cuándo quieres recibir avisos", fontSize = 12.sp, color = Color(0xFF888888))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingToggle(title = "Recordatorio pesaje (viernes 10:00)", subtitle = "Avisa si no has registrado peso esta semana", checked = notifWeighin, onCheckedChange = { notifWeighin = it })
                        SettingToggle(title = "Inactividad en entrenamiento", subtitle = "Avisa si pasan X días sin entrenar", checked = notifInactivity, onCheckedChange = { notifInactivity = it })
                        if (notifInactivity) {
                            SettingNumberInput(title = "Días de inactividad", subtitle = "Después de cuántos días sin entrenar notificar", value = inactivityDays, onValueChange = { inactivityDays = it.coerceIn(1, 30) })
                        }
                        SettingAction(title = "Probar notificación", subtitle = "Envía una notificación de prueba ahora", icon = Icons.Filled.Notifications, onClick = { /* TODO */ })
                    }
                }
            }

            // Apariencia
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Palette, contentDescription = null, tint = Color(0xFFFFD600))
                            Text(text = "Apariencia", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        }
                    }
                    Text(text = "Personaliza el aspecto de la app", fontSize = 12.sp, color = Color(0xFF888888))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingInfo(title = "Tema", value = "Oscuro (forzado)")
                        SettingInfo(title = "Color de acento", value = "#${String.format("%06X", accentColor)}")
                        SettingOptionSelector(title = "Intensidad de animaciones", options = listOf("Baja" to 0, "Media" to 1, "Alta" to 2), selected = animationIntensity, onSelect = { animationIntensity = it })
                    }
                }
            }

            // Datos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D1B1B))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.DeleteForever, contentDescription = null, tint = Color(0xFFEF5350))
                            Text(text = "Datos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        }
                    }
                    Text(text = "Gestión de datos de la app", fontSize = 12.sp, color = Color(0xFF888888))
                    SettingAction(title = "Borrar todos los datos", subtitle = "Elimina rutinas, entrenamientos, peso, catálogo personalizado, settings. Irreversible.", icon = Icons.Filled.DeleteForever, onClick = { showClearConfirm = true }, destructive = true)
                }
            }

            // Acerca de
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = Color(0xFFFFD600))
                            Text(text = "Acerca de", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                        }
                    }
                    Text(text = "Información de la aplicación", fontSize = 12.sp, color = Color(0xFF888888))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SettingInfo(title = "Versión", value = "1.0.0")
                        SettingInfo(title = "Compilación", value = "Debug")
                        SettingInfo(title = "Min SDK", value = "24 (Android 7.0)")
                        SettingInfo(title = "Target SDK", value = "35 (Android 16)")
                        SettingAction(title = "Licencias open source", subtitle = "Ver licencias de librerías usadas", icon = Icons.Filled.Info, onClick = { /* TODO */ })
                    }
                }
            }
        }
    }

    // Clear all data confirmation
    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("⚠️ Borrar TODOS los datos", fontWeight = FontWeight.Bold, color = Color(0xFFEF5350)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Esta acción eliminará PERMANENTEMENTE:", color = Color(0xFFFFFFFF))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("• Todas las rutinas, días, bloques y ejercicios", color = Color(0xFFBDBDBD))
                        Text("• Todo el historial de entrenamientos y series", color = Color(0xFFBDBDBD))
                        Text("• Registros de peso corporal y fotos", color = Color(0xFFBDBDBD))
                        Text("• Sensaciones globales y sensaciones por ejercicio", color = Color(0xFFBDBDBD))
                        Text("• Catálogo de ejercicios personalizados", color = Color(0xFFBDBDBD))
                        Text("• Todas las configuraciones y preferencias", color = Color(0xFFBDBDBD))
                    }
                    Text("Escribe \"BORRAR\" para confirmar:", fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    TextField(
                        value = clearConfirmText,
                        onValueChange = { clearConfirmText = it },
                        label = { Text("Escribe BORRAR aquí") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { /* TODO: clear all data */ showClearConfirm = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350),
                        contentColor = Color(0xFF121212)
                    ),
                    enabled = clearConfirmText == "BORRAR"
                ) { Text("BORRAR TODO", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                Button(onClick = { showClearConfirm = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun SettingToggle(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showInfo: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, fontSize = 14.sp, color = Color(0xFFFFFFFF))
            Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF888888))
        }
        if (showInfo) {
            IconButton(onClick = { /* TODO: show formula dialog */ }) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "Fórmula", tint = Color(0xFF888888))
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFFFD600),
                checkedTrackColor = Color(0xFFFFD600),
                uncheckedThumbColor = Color(0xFFBDBDBD),
                uncheckedTrackColor = Color(0xFF4A4A4A)
            )
        )
    }
}

@Composable
fun SettingAction(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    destructive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (destructive) Color(0xFF2D1B1B) else Color(0xFF2C2C2C)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = if (destructive) Color(0xFFEF5350) else Color(0xFFFFD600))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = title, fontSize = 14.sp, color = if (destructive) Color(0xFFEF5350) else Color(0xFFFFFFFF))
                    Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF888888))
                }
            }
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF888888))
        }
    }
}

@Composable
fun SettingInfo(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 14.sp, color = Color(0xFFFFFFFF))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF888888))
    }
}

@Composable
fun SettingNumberInput(
    title: String,
    subtitle: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, fontSize = 14.sp, color = Color(0xFFFFFFFF))
            Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF888888))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onValueChange(value - 1) }) { Icon(imageVector = Icons.Filled.Remove, contentDescription = "Disminuir") }
            Text(text = value.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600), modifier = Modifier.width(48.dp).padding(horizontal = 16.dp))
            Button(onClick = { onValueChange(value + 1) }) { Icon(imageVector = Icons.Filled.Add, contentDescription = "Aumentar") }
        }
    }
}

@Composable
fun SettingOptionSelector(
    title: String,
    options: List<Pair<String, Int>>,
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, fontSize = 14.sp, color = Color(0xFFFFFFFF))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { (label, value) ->
                Button(
                    onClick = { onSelect(value) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected == value) Color(0xFFFFD600) else Color(0xFF2C2C2C)
                    )
                ) {
                    Text(label, color = if (selected == value) Color(0xFF121212) else Color(0xFFFFFFFF))
                }
            }
        }
    }
}