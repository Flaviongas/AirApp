package com.example.airapp

import MainWeatherInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import com.example.airapp.R

// Definir la familia de fuentes Poppins
val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
)

// Colores
val backgroundColor = Color(0xFFF5F5F5)
val cardColor = Color.White.copy(alpha = 0.8f)
val primaryButtonColor = Color(0xFFB8E39B)
val textColor = Color(0xFF1E1B1B)

@Composable
fun Days(navController: NavController) {
    DaysBody(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaysBody(navController: NavController) {
    val scrollState = rememberScrollState()

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
                        .height(250.dp)
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

                    // Overlay oscuro
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
                            "Universidad Autónoma",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = poppinsFamily,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Información principal del clima con texto blanco
                        MainWeatherInfo(reduced = false, useWhiteText = true)
                    }
                }

                // Resto del contenido con padding
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    // Arreglo para mantener registro de acordiones abiertos
                    val states = remember { mutableStateListOf(false, false, false) }

                    // Tarjetas de días con iconos PNG
                    Accordion(
                        day = "Lunes, Mar 22",
                        weather = "Soleado",
                        iconResId = R.drawable.ic_sunny,
                        index = 0,
                        states = states,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Accordion(
                        day = "Martes, Mar 23",
                        weather = "Nublado",
                        iconResId = R.drawable.ic_cloudy,
                        index = 1,
                        states = states,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Accordion(
                        day = "Miércoles, Mar 24",
                        weather = "Lluvia ligera",
                        iconResId = R.drawable.ic_cloudy,
                        index = 2,
                        states = states,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de volver
                    Button(
                        onClick = { navController.navigate("Home") },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryButtonColor,
                            contentColor = textColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
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