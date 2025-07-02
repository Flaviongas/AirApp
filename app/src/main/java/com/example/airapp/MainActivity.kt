// Tu archivo MainActivity.kt actualizado
package com.example.airapp

import WeatherScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF011A37)
            ) {
                val navController = rememberNavController()
                NavHost(navController=navController, 
startDestination = if (user != null) "home" else "login"
                builder={
                    composable("Login") {
                        LoginScreen(navController, auth)
                    }
                    composable("Register") {
                        RegisterScreen(navController = navController)}
                    composable("Home") {
                        WeatherScreen(navController, auth)
                    }
                    composable("Plots") {
                        Plots(navController)
                    }
                    composable("Days") {
                        Days(navController)
                    }
                })
            }
        }
    }
}
