package com.example.airapp
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TransparentEditButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.background(Color.Transparent)
    ) {
        Icon(
            imageVector = Icons.Default.Edit, // Ícono de lápiz
            contentDescription = "Ingresa tu nombre",
            tint = Color.Gray 
        )
    }
}