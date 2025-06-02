package com.example.airapp
import WeatherMetricCardSimple
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp




@Composable
fun Accordion(
    modifier: Modifier = Modifier,
    day: String,
    weather: String,
    emoji: String,
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
        color = MaterialTheme.colorScheme.surfaceDim.copy(alpha=0.6f),
        modifier = modifier.padding(vertical = 13.dp)
            .clip(RoundedCornerShape(16.dp)) // Aumenté el radio de las esquinas
            .clickable {
                // Cerrar los demás acordiones cuando se abre uno
                for (i in states.indices) if(i!=index)states[i] = false
                states[index] = !states[index]
            }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column {
                    Text(
                        text = day,
                        color = textColor,
                        fontSize = 20.sp, // Tamaño aumentado
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = weather,
                        color = textColor.copy(alpha = 0.8f),
                        fontSize = 16.sp, // Tamaño aumentado
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = emoji,
                    fontSize = 36.sp, // Tamaño aumentado
                    modifier = Modifier.padding(end = 8.dp)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp) // Tamaño aumentado
                        .rotate(arrowRotation),
                    tint = textColor
                )
            }

            // Animaciones para abrir y cerrar acordión
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween()
                ) + fadeIn(),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween()
                ) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        WeatherMetricCardSimple(
                            title = "Alerta CO2",
                            value = "653 ppm",
                            status = "Alto",
                            icon = "\uD83D\uDCA8",
                            modifier = Modifier.weight(1f).padding(start = 16.dp),
                            black = true
                        )

                        WeatherMetricCardSimple(
                            title = "Alerta Tem",
                            value = "40 °C",
                            status = "15h atrás",
                            icon = "\uD83D\uDCA8",
                            modifier = Modifier.weight(1f).padding(end = 16.dp),
                            black = true
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        WeatherMetricCardSimple(
                            title = "Calidad de aire",
                            value = "ica 45",
                            status = "Bueno",
                            icon = "💨",
                            modifier = Modifier.weight(1f).padding(start = 16.dp),
                            black = true
                        )

                        WeatherMetricCardSimple(
                            title = "CO₂",
                            value = "400ppm",
                            status = "Bueno",
                            icon = "🌫️",
                            modifier = Modifier.weight(1f).padding(end = 16.dp),
                            black = true
                        )
                    }
                }
            }
        }
    }
}