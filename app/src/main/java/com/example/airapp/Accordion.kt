package com.example.airapp
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
    val expanded = states[index]
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "accordion-arrow"
    )

    Surface(
        color = MaterialTheme.colorScheme.surfaceDim,
        modifier = modifier.padding(vertical = 13.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                for (i in states.indices) states[i] = false
                states[index] = true
            }

    ) {
        Column {
            // accordion header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column {
                Text(day,
                        fontWeight = FontWeight.Bold,
                )
                    Text(weather)
                }
                Spacer(modifier= Modifier.weight(2.8f))
                Text(emoji , fontSize = 30.sp,
                    modifier = Modifier.weight(1f),
                    )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                // animate expand vertically from the top when expanded + fade in
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween()
                ) + fadeIn(),
                // animate shrink vertically to the top when collapsed + fade out
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween()
                ) + fadeOut()
            ) {
Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WeatherMetricCardSimple(
                        title = "Alerta CO2",
                        value = "653 ppm",
                        status = "4h atrás",
                        icon = "\uD83D\uDCA8",
                        modifier = Modifier.weight(1f).padding(start = 15.dp),
                        black = true
                    )

                    WeatherMetricCardSimple(
                        title = "Alerta Tem",
                        value = "40 °C",
                        status = "15h atrás",
                        icon = "\uD83D\uDCA8",
                        modifier = Modifier.weight(1f).padding(end = 15.dp),
                        black = true
                    )
                }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        WeatherMetricCardSimple(
                            title = "Calidad de aire",
                            value = "ica 45",
                            status = "Bueno",
                            icon = "💨",
                            modifier = Modifier.weight(1f).padding(start = 15.dp),
                            black= true
                        )

                        WeatherMetricCardSimple(
                            title = "CO₂",
                            value = "400ppm",
                            status = "Bueno",
                            icon = "🌫️",
                            modifier = Modifier.weight(1f).padding(end = 15.dp),
                            black= true
                        )
                    }
                }
                }
            }
        }
    }
