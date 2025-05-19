package com.example.airapp

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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Plots(navController: NavController){
    NavBar(navController)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navController: NavController) {
    val barsValues = remember { mutableStateOf(List(4) { (4..8).random() }) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            barsValues.value = List(4) { (4..8).random() }
        }
    }
    val animatedBars = remember {
        List(4) { index ->
            Animatable(barsValues.value[index].toFloat() / 10)
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
                            text = "Predicción de temperatura",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                        )
                    }
                    JetpackComposeBasicLineChart()
                }

                Card(
                    modifier = Modifier
                        .height(190.dp).padding(vertical =10.dp),
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
                                    text = "☁\uFE0F",
                                    fontSize = 16.sp,
                                            modifier = Modifier.padding(bottom = 10.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Porcentaje de C02 hoy",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }
                                    PrettyBar("6 PM","${(animatedBars[0].value*700).toInt()} ppm",animatedBars[0].value)
                            PrettyBar("7 PM","${(animatedBars[1].value*700).toInt()} ppm",animatedBars[1].value)
                            PrettyBar("8 PM","${(animatedBars[2].value*700).toInt()} ppm",animatedBars[2].value)
                            PrettyBar("9 PM","${(animatedBars[3].value*700).toInt()} ppm",animatedBars[3].value)

                            }
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { navController.navigate("Home") },
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB8E39B),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Volver al Inicio")
                        }
                    }
                }

            }
        }}
}


