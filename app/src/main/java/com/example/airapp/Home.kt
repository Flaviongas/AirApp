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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.airapp.CustomDialog
import com.example.airapp.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import com.example.airapp.CurrentUser
import com.example.airapp.EditUsernameButton
import com.example.airapp.LogoutButton
import com.example.airapp.addUsername
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal)
)

val textColor = Color(0xFF1E1B1B)

// "Objeto" para mantener estructura
data class WeatherData(
    val Aire: String = "",
    val Dioxide: String = "",
    val Humedad: String = "",
    val Temperatura: String = "",
    val Aire_Outlook: String = "",
    val Dioxide_Outlook: String = "",
    val Humedad_Change: String = "",
    val Temperatura_Change: String = ""
) {
    constructor() : this("", "", "", "", "", "", "", "") // Required for Firestore
}

// Colores fijos para los iconos como en Days.kt
val airQualityIconColor = Color(0xFF4CAF50) // Verde para calidad de aire
val co2IconColor = Color(0xFF2196F3) // Azul para CO₂
val humidityIconColor = Color(0xFF03A9F4) // Azul claro para humedad
val temperatureIconColor = Color(0xFFFF5722) // Rojo naranja para temperatura

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController, auth: FirebaseAuth) {
    val db = FirebaseFirestore.getInstance()
    val weather_data_db = remember { mutableStateListOf<WeatherData>() }
    val selectedTab = remember { mutableIntStateOf(0) }
    val showDialog = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    // Iniciamos un state que contendrá al usuario
    var current_user by remember { mutableStateOf<CurrentUser?>(null) }


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

                        LaunchedEffect(Unit) {
                            try {
                                // Obtenemos datos del tiempo de la db
                                db.collection("weather_metrics").get()
                                    .addOnSuccessListener { result ->
                                        weather_data_db.clear()
                                        for (document in result) {
                                            try {
                                                val day = document.toObject(WeatherData::class.java)
                                                weather_data_db.add(day)
                                                Log.i("DATA",weather_data_db.toString())
                                            } catch (e: Exception) {
                                                Log.e("Firestore", "Error parsing document", e)
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        Log.e("Firestore", "Error al obtener datos", it)
                                    }

                                auth.currentUser?.email?.let { email ->
                                    // Vemos si el usuario tiene un nombre de usuario registrado
                                    db.collection("users")
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnSuccessListener { result ->
                                            if (result.isEmpty) {
                                                // Si no tiene username, creamos uno con el correo
                                                val newUser = CurrentUser(
                                                    email = email,
                                                    username = email.split('@')[0] // Default username
                                                )
                                                addUsername(newUser)
                                                current_user = newUser
                                            } else {
                                                current_user = result.documents.first().toObject(
                                                    CurrentUser::class.java)
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("Firestore", "Error fetching user", e)
                                        }
                                }

                            } catch (e: Exception) {
                                Log.e("Firestore", "Initialization error", e)
                            }
                        }

                        // Header con nombre de usuario
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text(
                                // Si aun no se obtiene el usuario de la db, se muestra el texto "Cargando..."
                                text = current_user?.let { "Bienvenido ${it.username}" } ?: "Cargando...",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontFamily = poppinsFamily,
                                fontWeight = FontWeight.Medium
                            )
                            if(auth.currentUser!=null){
                            EditUsernameButton(onClick = {showDialog.value= true})
                                Spacer(modifier = Modifier.weight(1f))
                                LogoutButton(navController = navController, auth = auth)
                            }
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

                    WeatherMetrics(
                        selectedTab = selectedTab.value,
                        // Lista vacía si aun no se obtienen los datos
                        weatherDays = if (weather_data_db.isNotEmpty()) weather_data_db.sortedWith(compareBy({it.Aire})).reversed() else emptyList()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HourlyForecast()

                    Spacer(modifier = Modifier.height(5.dp))

                    NavButtons(navController)

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if(showDialog.value) {
        CustomDialog(value = "", setShowDialog = { showDialog.value = it } ,
            current_user = current_user,
            onUsernameChanged = { newUsername ->
                    val temp = CurrentUser(current_user?.email ?: "", newUsername)
                    current_user = null  // Eliminamos el nombre de usuario anterior
                    current_user = temp  // Forzamos a que se actualize el estado

            }
        )

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
            .background(Color.White.copy(alpha = 0.8f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedTab == index) Color(0xFFB8E39B) else Color.White.copy(alpha = 0.8f))
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
fun WeatherMetrics(selectedTab: Int,weatherDays : List<WeatherData>) {
    if (weatherDays.isEmpty()) {
        // Si aun no se obtienen los datos, mostrar animación de carga
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

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
                useWhiteBackground = true
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
                useWhiteBackground = true
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
                useWhiteBackground = true
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
                useWhiteBackground = true
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
    useWhiteBackground: Boolean = false
) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                useWhiteBackground -> Color.White.copy(alpha = 0.8f)
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
            color = Color.White.copy(alpha = 0.8f),
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