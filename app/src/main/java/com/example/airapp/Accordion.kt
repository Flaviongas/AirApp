package com.example.airapp
import WeatherMetricCardSimple
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Accordion(
    modifier: Modifier = Modifier,
    day: String,
    weather: String,
    iconResId: Int, // Cambio de emoji a iconResId
    index: Int,
    states: SnapshotStateList<Boolean>
) {
    // Saber cual acordión se abrió
    val expanded = states[index]

    // Para rotar flecha al abrir acordión
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "accordion-arrow"
    )

    Surface(
        // Nuevo color más moderno y elegante - un azul suave con transparencia
        color = Color(0xFF6B9AE8).copy(alpha = 0.15f),
        modifier = modifier.padding(vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp)) // Esquinas más redondeadas
            .clickable {
                // Cerrar los demás acordiones cuando se abre uno
                for (i in states.indices) if(i!=index)states[i] = false
                states[index] = !states[index]
            }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = day,
                        color = textColor,
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = weather,
                        color = textColor.copy(alpha = 0.7f),
                        fontSize = 15.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Reemplazar emoji con imagen PNG
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Weather icon",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(arrowRotation),
                    tint = textColor.copy(alpha = 0.8f)
                )
            }

            // Animaciones para abrir y cerrar acordión
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        WeatherMetricCardSimple(
                            title = "Alerta CO2",
                            value = "653 ppm",
                            status = "Alto",
                            icon = {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Alerta CO2",
                                    tint = Color(0xFFE57373), // Rojo suave para alertas
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            black = false
                        )

                        WeatherMetricCardSimple(
                            title = "Alerta Tem",
                            value = "40 °C",
                            status = "15h atrás",
                            icon = {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Alerta Temperatura",
                                    tint = Color(0xFFFFB74D), // Naranja suave para alertas
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            black = false
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        WeatherMetricCardSimple(
                            title = "Calidad de aire",
                            value = "ica 45",
                            status = "Bueno",
                            icon = {
                                Icon(
                                    Icons.Default.Air,
                                    contentDescription = "Calidad de aire",
                                    tint = Color(0xFF81C784), // Verde suave para buenas métricas
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            black = false
                        )

                        WeatherMetricCardSimple(
                            title = "CO₂",
                            value = "400ppm",
                            status = "Bueno",
                            icon = {
                                Icon(
                                    Icons.Default.Cloud,
                                    contentDescription = "CO₂",
                                    tint = Color(0xFF64B5F6), // Azul suave
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.weight(1f),
                            black = false
                        )
                    }
                }
            }
        }
    }
}