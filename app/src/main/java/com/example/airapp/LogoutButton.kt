package com.example.airapp
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LogoutButton(navController: NavController, auth: FirebaseAuth) {
    IconButton(
        onClick = {
            auth.signOut() // Cierra la sesión del usuario actual en Firebase
            // Navega de vuelta a la pantalla de login
            navController.navigate("Login") {
                // Elimina la pantalla "home" del backstack para que no se pueda regresar
                popUpTo("home") { inclusive = true }
            }},
        modifier = Modifier.background(Color.Transparent)
    ) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = "Ingresa tu nombre",
            tint = Color.White
        )
    }
}