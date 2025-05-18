package com.example.airapp

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Days(navController: NavController){
    Text(text="Days")
    Button(onClick ={navController.navigate("Home")} ) { Text(text = "Volver al Inicio") }
}
