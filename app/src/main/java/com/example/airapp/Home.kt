import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.airapp.CustomDialog
import com.example.airapp.R
import com.example.airapp.TransparentEditButton
import com.example.airapp.UserData
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon

val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal)
)

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

// Colores fijos para los iconos como en Days.kt
val airQualityIconColor = Color(0xFF4CAF50) // Verde para calidad de aire
val co2IconColor = Color(0xFF2196F3) // Azul para CO₂
val humidityIconColor = Color(0xFF03A9F4) // Azul claro para humedad
val temperatureIconColor = Color(0xFFFF5722) // Rojo naranja para temperatura

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
    val selectedTab = remember { mutableIntStateOf(0) }
    val showDialog = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if(showDialog.value) {
        CustomDialog(value = "", setShowDialog = {
            showDialog.value = it
        }) {
            Log.i("HomePage", "HomePage: $it")
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
                        .height(260.dp) // Ajusta la altura según necesites
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

                    // Overlay oscuro opcional para mejorar legibilidad del texto
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
                        val context = LocalContext.current
                        val userPreferences = remember { UserData(context) }
                        var savedName by remember { mutableStateOf("") }

                        LaunchedEffect(Unit) {
                            userPreferences.getName.collect { name ->
                                savedName = name
                            }
                        }

                        // Header con nombre de usuario
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text(
                                "Bienvenid@, $savedName",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium
                            )
                            TransparentEditButton(onClick = { showDialog.value = true })
                        }

                        // Información principal del clima
                        MainWeatherInfo(reduced = false, useWhiteText = true)
                    }
                }

                // Resto del contenido con padding
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    DaySelector(selectedTab.value) { selectedTab.value = it }

                    Spacer(modifier = Modifier.height(24.dp))

                    WeatherMetrics(selectedTab.value)

                    Spacer(modifier = Modifier.height(24.dp))

                    HourlyForecast()

                    Spacer(modifier = Modifier.height(5.dp))

                    NavButtons(navController)

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun MainWeatherInfo(reduced: Boolean, useWhiteText: Boolean = false) {
    val displayTextColor = if (useWhiteText) Color.White else textColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "3°",
                color = displayTextColor,
                fontSize = 72.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Light
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Sensación",
                    color = displayTextColor,
                    fontSize = 18.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = " -2°",
                    color = displayTextColor,
                    fontSize = 18.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if(!reduced) {
                Text(
                    text = "Marzo 28, 17:08",
                    color = displayTextColor,
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
                        .size(80.dp)
                        .background(
                            color = Color(0xFFFFB300),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .offset(x = 6.dp, y = 6.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    )
                }


            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Soleado",
                color = displayTextColor,
                fontSize = 18.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal
            )

            if(!reduced) {
                Text(
                    text = "Max 3° Min -1°",
                    color = displayTextColor,
                    fontSize = 16.sp,
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
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.8f)), // Cambio: fondo más blanco
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedTab == index) Color(0xFFB8E39B) else Color.White.copy(alpha = 0.8f)) // Cambio: fondo blanco para no seleccionados
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = if (selectedTab == index) Color.Black else textColor,
                    fontSize = 16.sp,
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
                icon = {
                    Icon(
                        Icons.Default.Air,
                        contentDescription = "Calidad de aire",
                        tint = airQualityIconColor,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f),
                useWhiteBackground = true // Cambio: usar fondo blanco
            )

            WeatherMetricCardSimple(
                title = "CO₂",
                value = weatherDays[selectedTab].Dioxide,
                status = weatherDays[selectedTab].Dioxide_Outlook,
                icon = {
                    Icon(
                        Icons.Default.Cloud,
                        contentDescription = "CO₂",
                        tint = co2IconColor,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f),
                useWhiteBackground = true // Cambio: usar fondo blanco
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
                icon = {
                    Icon(
                        Icons.Default.WaterDrop,
                        contentDescription = "Humedad",
                        tint = humidityIconColor,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f),
                useWhiteBackground = true // Cambio: usar fondo blanco
            )

            WeatherMetricCardSimple(
                title = "Temperatura",
                value = weatherDays[selectedTab].Temperatura,
                status = weatherDays[selectedTab].Temperatura_Change,
                icon = {
                    Icon(
                        Icons.Default.Thermostat,
                        contentDescription = "Temperatura",
                        tint = temperatureIconColor,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f),
                useWhiteBackground = true // Cambio: usar fondo blanco
            )
        }
    }
}

@Composable
fun WeatherMetricCardSimple(
    title: String,
    value: String,
    status: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    black: Boolean = false,
    useBlueBackground: Boolean = false,
    useWhiteBackground: Boolean = false // Nuevo parámetro
) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                useWhiteBackground -> Color.White.copy(alpha = 0.8f) // Cambio: fondo blanco
                useBlueBackground -> Color(0xFF6B9AE8).copy(alpha = 0.15f)
                else -> Color.White.copy(alpha = 0.2f)
            }
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
                icon()

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    color = if(black) Color.Black else textColor,
                    fontSize = 16.sp,
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


data class HourlyWeather(
    val hour: String,
    val temp: Int,
    val iconResId: Int
)

@Composable
fun HourlyForecast() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Tiempo de hoy",
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val hourlyData = listOf(
            HourlyWeather("Ahora", 10, R.drawable.ic_cloudy),
            HourlyWeather("10AM", 8, R.drawable.ic_cloudy),
            HourlyWeather("11AM", 5, R.drawable.ic_sunny),
            HourlyWeather("12PM", 12, R.drawable.ic_cloudy),
            HourlyWeather("1PM", 9, R.drawable.ic_sunny),
            HourlyWeather("2PM", 12, R.drawable.ic_cloudy)
        )

        Surface(
            color = Color.White.copy(alpha = 0.8f), // Cambio: fondo más blanco
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                items(hourlyData) { hourData ->
                    HourlyWeatherItem(hourData)
                }
            }
        }
    }
}

@Composable
fun HourlyWeatherItem(data: HourlyWeather) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(60.dp)
    ) {
        Text(
            text = data.hour,
            color = textColor,
            fontSize = 16.sp,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = data.iconResId),
            contentDescription = "Weather icon",
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${data.temp}°",
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFamily
        )
    }
}

@Composable
fun NavButtons(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { navController.navigate("Plots") },
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB8E39B),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(4.dp)
                .height(48.dp)
                .width(160.dp)
        ) {
            Text(
                "Ver gráficos",
                color = textColor,
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal
            )
        }

        Button(
            onClick = { navController.navigate("Days") },
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB8E39B),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(4.dp)
                .height(48.dp)
                .width(160.dp)
        ) {
            Text(
                "Análisis diario",
                color = textColor,
                fontSize = 16.sp,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal
            )
        }
    }
}