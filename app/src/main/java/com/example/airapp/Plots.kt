package com.example.airapp

import MainWeatherInfo
import WeatherMetricCardSimple
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Colores para los iconos
val temperatureIconColor = Color(0xFFFF6B35) // Naranja para temperatura
val co2IconColor = Color(0xFF4CAF50) // Verde para CO2
val warningIconColor = Color(0xFFFF5722) // Rojo-naranja para alertas
val calendarIconColor = Color(0xFF2196F3) // Azul para calendario

@Composable
fun Plots(navController: NavController) {
    NavBar(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navController: NavController) {
    // Definiciones de variables de estilo
    val poppinsFamily = FontFamily.Default
    val textColor = Color(0xFF1E1B1B)
    val scrollState = rememberScrollState()

    val barsValues = remember { mutableStateOf(List(4) { (4..8).random() }) }
    val animatedBars = remember {
        List(4) { index ->
            Animatable(barsValues.value[index].toFloat() / 10)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            barsValues.value = List(4) { (4..8).random() }
        }
    }

    LaunchedEffect(barsValues.value) {
        barsValues.value.forEachIndexed { index, newValue ->
            launch {
                animatedBars[index].animateTo(newValue.toFloat() / 10)
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Sección superior con imagen de fondo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    // Imagen de fondo
                    Image(
                        painter = painterResource(id = R.drawable.weather_background),
                        contentDescription = "Weather Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 24.dp,
                                    bottomEnd = 24.dp
                                )
                            )
                    )

                    // Overlay oscuro para mejorar legibilidad del texto
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 24.dp,
                                    bottomEnd = 24.dp
                                )
                            )
                            .background(Color.Black.copy(alpha = 0.3f))
                    )

                    // Contenido sobre la imagen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        // Header
                        Text(
                            "Gráficos y Análisis",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Información principal del clima con texto blanco
                        MainWeatherInfo(reduced = true, useWhiteText = true)
                    }
                }

                // Resto del contenido con padding
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    // Gráfico de temperatura
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Predicción de temperatura",
                                    tint = calendarIconColor, // Azul para calendario
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = "Predicción de temperatura",
                                    color = textColor,
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            JetpackComposeBasicLineChart()
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gráfico de barras de CO2
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Cloud,
                                    contentDescription = "Niveles de CO2",
                                    tint = co2IconColor, // Verde para CO2
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = "Niveles de CO2 hoy",
                                    color = textColor,
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Barras de CO2
                            PrettyBar(
                                "6 PM",
                                "${(animatedBars[0].value*700).toInt()} ppm",
                                animatedBars[0].value
                            )
                            PrettyBar(
                                "7 PM",
                                "${(animatedBars[1].value*700).toInt()} ppm",
                                animatedBars[1].value
                            )
                            PrettyBar(
                                "8 PM",
                                "${(animatedBars[2].value*700).toInt()} ppm",
                                animatedBars[2].value
                            )
                            PrettyBar(
                                "9 PM",
                                "${(animatedBars[3].value*700).toInt()} ppm",
                                animatedBars[3].value
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Alertas con fondo blanco (modificado)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Alerta CO2
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.8f) // Mismo fondo que los gráficos
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = "Alerta CO2",
                                        tint = warningIconColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Alerta CO2",
                                        color = textColor,
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "653 ppm",
                                    color = textColor,
                                    fontSize = 18.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "4h atrás",
                                    color = textColor.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFamily
                                )
                            }
                        }

                        // Alerta Temperatura
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.8f) // Mismo fondo que los gráficos
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        Icons.Default.Thermostat,
                                        contentDescription = "Alerta Temperatura",
                                        tint = temperatureIconColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Alerta Temp",
                                        color = textColor,
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFamily,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "40 °C",
                                    color = textColor,
                                    fontSize = 18.sp,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "15h atrás",
                                    color = textColor.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFamily
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de volver
                    Button(
                        onClick = { navController.navigate("Home") },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB8E39B),
                            contentColor = textColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            "Volver al Inicio",
                            fontSize = 16.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}