package com.example.airapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Universidad, Autónoma",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFF2B5D70) // Color azul oscuro como fondo
    ) { paddingValues ->
        // Overlay para mejorar la legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black.copy(alpha = 0.4f))
        ) {
            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                MainWeatherInfo(reduced=false)

                Spacer(modifier = Modifier.height(16.dp))

                DaySelector()

                Spacer(modifier = Modifier.height(16.dp))

                WeatherMetrics()

                Spacer(modifier = Modifier.height(16.dp))

                HourlyForecast()

                NavButtons(navController)
            }
        }
    }
}

@Composable
fun MainWeatherInfo(reduced: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Temperatura principal y sensación térmica
        Column {
            Text(
                text = "3°",
                color = Color.White,
                fontSize = 80.sp,
                fontWeight = FontWeight.Light
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Sensación",
                    color = Color.White,
                    fontSize = 16.sp
                )

                Text(
                    text = " -2°",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            if(!reduced){

            Text(
                text = "Marzo 28, 17:08",
                color = Color.White,
                fontSize = 16.sp
            )
            }
        }

        // Estado del tiempo con gato asomándose
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.TopCenter) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = Color(0xFFFFB300),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Icono de nube parcialmente cubriendo el sol
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .offset(x = 4.dp, y = 4.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    )
                }

                // Gato asomándose
                Box(
                    modifier = Modifier
                        .offset(y = (-20).dp)
                        .size(40.dp)
                ) {
                    // Orejas del gato
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .offset(x = (-5).dp)
                                .clip(RoundedCornerShape(topStart = 50f, topEnd = 50f))
                                .background(Color.Black)
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .offset(x = 5.dp)
                                .clip(RoundedCornerShape(topStart = 50f, topEnd = 50f))
                                .background(Color.Black)
                        )
                    }

                    // Cara del gato
                    Box(
                        modifier = Modifier
                            .offset(y = 6.dp)
                            .size(34.dp)
                            .clip(RoundedCornerShape(topStart = 50f, topEnd = 50f))
                            .background(Color.Black)
                    ) {
                        // Ojos del gato
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .offset(x = (-5).dp)
                                    .background(Color.Yellow, CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .offset(x = 5.dp)
                                    .background(Color.Yellow, CircleShape)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Nublado",
                color = Color.White,
                fontSize = 16.sp
            )
            if(!reduced){

            Text(
                text = "Max 3° Min -1°",
                color = Color.White,
                fontSize = 14.sp
            )
            }
        }
    }
}

@Composable
fun DaySelector() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Hoy", "Mañana", "10 Días")

    // Tab custom personalizado con estilo de la UI mostrada
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.2f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selectedTab == index) Color(0xFFB8E39B) else Color.Transparent)
                    .clickable { selectedTab = index },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (selectedTab == index) Color.Black else Color.White,
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun WeatherMetrics() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherMetricCardSimple(
                title = "Calidad de aire",
                value = "ica 45",
                status = "Bueno",
                icon = "💨",
                modifier = Modifier.weight(1f)
            )

            WeatherMetricCardSimple(
                title = "CO₂",
                value = "400ppm",
                status = "Bueno",
                icon = "🌫️",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherMetricCardSimple(
                title = "Humedad",
                value = "720 hpa",
                status = "↑ 32 hpa",
                icon = "💧",
                modifier = Modifier.weight(1f)
            )

            WeatherMetricCardSimple(
                title = "Temperatura",
                value = "3 °C",
                status = "↑ 0.3",
                icon = "🌡️",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun WeatherMetricCardSimple(
    title: String,
    value: String,
    status: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = icon,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = status,
                    color = if (status.startsWith("↑")) Color.Red else Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun HourlyForecast() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Tiempo de hoy",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val hourlyData = listOf(
            HourlyWeather("Ahora", 10, "☀️"),
            HourlyWeather("10AM", 8, "☀️"),
            HourlyWeather("11AM", 5, "☀️"),
            HourlyWeather("12PM", 12, "☀️"),
            HourlyWeather("1PM", 9, "☀️"),
            HourlyWeather("2PM", 12, "☀️")
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(hourlyData) { hourData ->
                HourlyWeatherItem(hourData)
            }
        }
    }
}

data class HourlyWeather(
    val hour: String,
    val temp: Int,
    val icon: String
)

@Composable
fun HourlyWeatherItem(data: HourlyWeather) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data.hour,
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xFFFFB300),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = data.icon,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${data.temp}°",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NavButtons(navController: NavController) {
    //TODO: Fix colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        Button(onClick ={navController.navigate("Plots")} ) { Text(text = "Ver gráficos") }
        Button(onClick ={} ) { Text(text = "Análisis por día") }
    }
}
