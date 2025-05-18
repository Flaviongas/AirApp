package com.example.airapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun Plots(navController: NavController){
    navBar(navController)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun navBar(navController: NavController) {
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
            Text("hello")
            JetpackComposeBasicLineChart()
            Button(onClick ={navController.navigate("Home")} ) { Text(text = "Volver al Inicio") }
        }}}
