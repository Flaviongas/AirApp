package com.example.airapp

import MainWeatherInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
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

// Colores consistentes
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
    Scaffold(
        modifier = Modifier.background(backgroundColor),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Universidad Autónoma",
                        color = textColor,
                        fontSize = 20.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            MainWeatherInfo(reduced = true)

            Spacer(modifier = Modifier.height(24.dp))

            // Arreglo para mantener registro de acordiones abiertos
            val states = remember { mutableStateListOf(false, false, false) }

            // Tarjetas de días
            Accordion(
                day = "Lunes, Mar 22",
                weather = "Soleado",
                emoji = "☀️",
                index = 0,
                states = states,

            )

            Spacer(modifier = Modifier.height(12.dp))

            Accordion(
                day = "Martes, Mar 23",
                weather = "Nublado",
                emoji = "☁️",
                index = 1,
                states = states,

            )

            Spacer(modifier = Modifier.height(12.dp))

            Accordion(
                day = "Miércoles, Mar 24",
                weather = "Lluvia ligera",
                emoji = "🌧️",
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
                    .height(52.dp)  // Aumentado el tamaño para mejor visualización
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