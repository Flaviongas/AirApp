// Tu archivo MainActivity.kt actualizado
package com.example.airapp

import WeatherScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.airapp.LoginScreen
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
                NavHost(navController=navController, startDestination="Login", builder={
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
