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
fun Plots(navController: NavController){
    NavBar(navController)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navController: NavController) {
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

                Card(
                    modifier = Modifier
                        .height(180.dp).padding(vertical =10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\uD83D\uDCC5",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Historial de tiempo",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                        )
                    }
                    JetpackComposeBasicLineChart()
                }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                    WeatherMetricCardSimple(
                        title = "Alerta CO2",
                        value = "653 ppm",
                        status = "4h atrás",
                        icon = "\uD83D\uDCA8",
                        modifier = Modifier.weight(1f)
                    )

                    WeatherMetricCardSimple(
                        title = "Alerta Tem",
                        value = "40 °C",
                        status = "15h atrás",
                        icon = "\uD83D\uDCA8",
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(onClick = { navController.navigate("Home") }) { Text(text = "Volver al Inicio") }
            }
        }}}


