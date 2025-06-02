package com.example.airapp

import MainWeatherInfo
import WeatherMetricCardSimple
import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Plots(navController: NavController) {
    NavBar(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navController: NavController) {
    // Definiciones de variables de estilo (ajusta según tus necesidades)
    val poppinsFamily = FontFamily.Default
    val textColor = Color.Black

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

    Scaffold(
        modifier = Modifier.background(Color(0xFFF5F5F5))
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            MainWeatherInfo(reduced = true)

            Spacer(modifier = Modifier.height(24.dp))

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
                        Text(
                            text = "📅",
                            fontSize = 20.sp,
                            fontFamily = poppinsFamily
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
                        Text(
                            text = "☁️",
                            fontSize = 20.sp,
                            fontFamily = poppinsFamily
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

            // Alertas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                WeatherMetricCardSimple(
                    title = "Alerta CO2",
                    value = "653 ppm",
                    status = "4h atrás",
                    icon = "💨",
                    modifier = Modifier.weight(1f),
                    black = true
                )

                WeatherMetricCardSimple(
                    title = "Alerta Temp",
                    value = "40 °C",
                    status = "15h atrás",
                    icon = "🌡️",
                    modifier = Modifier.weight(1f),
                    black = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de volver
            Button(
                onClick = { navController.navigate("Home") },
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB8E39B),
                    contentColor = Color.Black
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
        }
    }
}