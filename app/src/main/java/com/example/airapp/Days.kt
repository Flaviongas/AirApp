package com.example.airapp

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Days(navController: NavController){
    Column {
        DaysBody(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaysBody(navController: NavController) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                MainWeatherInfo(reduced = true)
                Accordion(
                    day = "Lunes, Mar 22",
                    weather = "Soleado",
                    emoji = "☀\uFE0F",
                )
                Accordion(
                    day = "Martes, Mar 23",
                    weather = "Nublado",
                    emoji = "☁️",
                )

                Accordion(
                    day = "Miércoles, Mar 24",
                    weather = "Lluvia ligera",
                    emoji = "🌧️",
                )


                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(onClick ={navController.navigate("Home")} ) { Text(text = "Volver al Inicio") }
                    }
                }

            }
        }
    }
}
