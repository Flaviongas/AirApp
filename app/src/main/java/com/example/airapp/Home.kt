package com.example.airapp

import android.util.Log
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Definir la familia de fuentes Poppins
val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal) // peso 400
)

// Color constante para todos los textos
val textColor = Color(0xFF1E1B1B)

// "Objeto" para mantener estructura
data class WeatherData(
    val Aire: String,
    val Dioxide: String,
    val Humedad: String,
    val Temperatura: String,
    val Aire_Outlook: String,
    val Dioxide_Outlook: String,
    val Humedad_Change: String,
    val Temperatura_Change: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
    val selectedTab = remember { mutableIntStateOf(0) }
    val showDialog = remember { mutableStateOf(false) }

    if(showDialog.value) {
        CustomDialog(value = "", setShowDialog = {
            showDialog.value = it
        }) {
            Log.i("HomePage", "HomePage: $it")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Universidad Autónoma",
                        color = textColor,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val context = LocalContext.current
                val userPreferences = remember { UserData(context) }
                var savedName by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    userPreferences.getName.collect { name ->
                        savedName = name
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Bienvenid@, $savedName",
                        color = textColor,
                        fontSize = 20.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Normal
                    )
                    TransparentEditButton(onClick = { showDialog.value = true })
                }

                MainWeatherInfo(reduced = false)

                Spacer(modifier = Modifier.height(16.dp))

                DaySelector(selectedTab.value) { selectedTab.value = it }

                Spacer(modifier = Modifier.height(16.dp))

                WeatherMetrics(selectedTab.value)

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
        Column {
            Text(
                text = "3°",
                color = textColor,
                fontSize = 80.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Light
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Sensación",
                    color = textColor,
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = " -2°",
                    color = textColor,
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if(!reduced) {
                Text(
                    text = "Marzo 28, 17:08",
                    color = textColor,
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )
            }
        }

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

                Box(
                    modifier = Modifier
                        .offset(y = (-20).dp)
                        .size(40.dp)
                ) {
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

                    Box(
                        modifier = Modifier
                            .offset(y = 6.dp)
                            .size(34.dp)
                            .clip(RoundedCornerShape(topStart = 50f, topEnd = 50f))
                            .background(Color.Black)
                    ) {
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
                color = textColor,
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal
            )

            if(!reduced) {
                Text(
                    text = "Max 3° Min -1°",
                    color = textColor,
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun DaySelector(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Hoy", "Mañana", "10 Días")

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
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (selectedTab == index) Color.Black else textColor,
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    fontFamily = poppinsFamily
                )
            }
        }
    }
}

@Composable
fun WeatherMetrics(selectedTab: Int) {
    val weatherDays = listOf(
        WeatherData(
            "ica 22", "412 ppm", "715 hpa", "18°C",
            "Moderado", "Regular", "↓ 15 hpa", "↓ 1.2°C"
        ),
        WeatherData(
            "ica 15", "388 ppm", "735 hpa", "5°C",
            "Excelente", "Bueno", "↑ 8 hpa", "↑ 2.5°C"
        ),
        WeatherData(
            "ica 08", "405 ppm", "710 hpa", "-2°C",
            "Bueno", "Moderado", "↓ 25 hpa", "↓ 3.1°C"
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherMetricCardSimple(
                title = "Calidad de aire",
                value = weatherDays[selectedTab].Aire,
                status = weatherDays[selectedTab].Aire_Outlook,
                icon = "💨",
                modifier = Modifier.weight(1f)
            )

            WeatherMetricCardSimple(
                title = "CO₂",
                value = weatherDays[selectedTab].Dioxide,
                status = weatherDays[selectedTab].Dioxide_Outlook,
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
                value = weatherDays[selectedTab].Humedad,
                status = weatherDays[selectedTab].Humedad_Change,
                icon = "💧",
                modifier = Modifier.weight(1f)
            )

            WeatherMetricCardSimple(
                title = "Temperatura",
                value = weatherDays[selectedTab].Temperatura,
                status = weatherDays[selectedTab].Temperatura_Change,
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
    modifier: Modifier = Modifier,
    black: Boolean = false
) {
    Card(
        modifier = modifier.height(90.dp),
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
                    color = if(black) Color.Black else textColor,
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
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
                    color = if(black) Color.Black else textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFamily
                )

                Text(
                    text = status,
                    color = if (status.startsWith("↑")) Color.Red else if(black) Color.Black else textColor,
                    fontSize = 14.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
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
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
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
            color = textColor,
            fontSize = 14.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Normal
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
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily
        )
    }
}

@Composable
fun NavButtons(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { navController.navigate("Plots") },
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB8E39B),
                contentColor = Color.Black
            ),
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                "Ver gráficos",
                color = textColor,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal
            )
        }

        Button(
            onClick = { navController.navigate("Days") },
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB8E39B),
                contentColor = Color.Black
            ),
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                "Análisis por día",
                color = textColor,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal
            )
        }
    }
}