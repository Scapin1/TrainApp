package com.tuusuario.trainapp.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuusuario.trainapp.ui.common.SectionHeader
import com.tuusuario.trainapp.ui.common.TrainTopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun StatsScreen(
    onNavigateUp: () -> Unit
) {
    var chartVisibility by remember { mutableStateOf(mapOf(
        "volume" to true,
        "heatmap" to true,
        "1rm" to true,
        "tonnage" to true,
        "rpe" to true,
        "time" to true
    )) }

    Scaffold(
        topBar = {
            TrainTopAppBar(
                title = "Estadísticas",
                navigationIcon = { IconButton(onClick = onNavigateUp) { Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Chart toggles
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Gráficos visibles", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    LazyColumn(
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf(
                            "volume" to "Volumen semanal (barras apiladas)",
                            "heatmap" to "Heatmap frecuencia (estilo GitHub)",
                            "1rm" to "1RM timeline (líneas por ejercicio)",
                            "tonnage" to "Tonelaje por grupo muscular",
                            "rpe" to "Tendencia RPE",
                            "time" to "Tiempo efectivo (entrenando vs descanso)"
                        )) { (key, label) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = label, fontSize = 14.sp, color = Color(0xFFFFFFFF))
                                Switch(
                                    checked = chartVisibility[key] ?: false,
                                    onCheckedChange = { chartVisibility = chartVisibility + (key to it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFFFFD600),
                                        checkedTrackColor = Color(0xFFFFD600),
                                        uncheckedThumbColor = Color(0xFFBDBDBD),
                                        uncheckedTrackColor = Color(0xFF4A4A4A)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Charts
            if (chartVisibility["volume"] == true) {
                ChartCard(title = "Volumen semanal por grupo muscular", subtitle = "Barras apiladas - Últimas 12 semanas") {
                    VolumeChart()
                }
            }

            if (chartVisibility["heatmap"] == true) {
                ChartCard(title = "Frecuencia de entrenamiento", subtitle = "Heatmap estilo GitHub - Último año") {
                    GitHubHeatmapChart()
                }
            }

            if (chartVisibility["1rm"] == true) {
                ChartCard(title = "1RM Estimado", subtitle = "Evolución Epley/Brzycki - Ejercicios principales") {
                    OneRMChart()
                }
            }

            if (chartVisibility["tonnage"] == true) {
                ChartCard(title = "Tonelaje por grupo muscular", subtitle = "Peso × Reps × Series - Evolución semanal") {
                    TonnageChart()
                }
            }

            if (chartVisibility["rpe"] == true) {
                ChartCard(title = "Tendencia RPE", subtitle = "Fatiga percibida promedio por semana") {
                    RPEChart()
                }
            }

            if (chartVisibility["time"] == true) {
                ChartCard(title = "Tiempo efectivo", subtitle = "Entrenando vs Descanso vs Total por sesión") {
                    TimeChart()
                }
            }

            // Summary stats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Resumen rápido", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        StatCard("Entrenamientos", "47", "Este mes", Modifier.weight(1f, fill = false))
                        StatCard("Horas totales", "52h", "Entrenando", Modifier.weight(1f, fill = false))
                        StatCard("Series totales", "1,234", "Este mes", Modifier.weight(1f, fill = false))
                        StatCard("Racha actual", "12 días", "Sin fallar", Modifier.weight(1f, fill = false))
                    }
                }
            }
        }
    }
}

@Composable
fun ChartCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                    Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF888888))
                }
                Icon(imageVector = Icons.Filled.ShowChart, contentDescription = null, tint = Color(0xFF888888))
            }
            content()
        }
    }
}

@Composable
fun VolumeChart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("Volumen semanal por grupo muscular", fontSize = 14.sp, color = Color(0xFF888888))
            Text("(Implementar gráfico de barras apiladas)", fontSize = 12.sp, color = Color(0xFF666666))
        }
    }
}

@Composable
fun GitHubHeatmapChart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("Frecuencia de entrenamiento", fontSize = 14.sp, color = Color(0xFF888888))
            Text("(Implementar heatmap estilo GitHub)", fontSize = 12.sp, color = Color(0xFF666666))
        }
    }
}

@Composable
fun OneRMChart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("1RM Estimado", fontSize = 14.sp, color = Color(0xFF888888))
            Text("(Implementar timeline Epley/Brzycki)", fontSize = 12.sp, color = Color(0xFF666666))
        }
    }
}

@Composable
fun TonnageChart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("Tonelaje por grupo muscular", fontSize = 14.sp, color = Color(0xFF888888))
            Text("(Implementar evolución semanal)", fontSize = 12.sp, color = Color(0xFF666666))
        }
    }
}

@Composable
fun RPEChart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("Tendencia RPE", fontSize = 14.sp, color = Color(0xFF888888))
            Text("(Implementar fatiga percibida)", fontSize = 12.sp, color = Color(0xFF666666))
        }
    }
}

@Composable
fun TimeChart() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2C2C2C))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("Tiempo efectivo", fontSize = 14.sp, color = Color(0xFF888888))
            Text("(Implementar entrenando vs descanso)", fontSize = 12.sp, color = Color(0xFF666666))
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
            Text(text = label, fontSize = 14.sp, color = Color(0xFFFFFFFF))
            Text(text = subtitle, fontSize = 10.sp, color = Color(0xFF888888))
        }
    }
}