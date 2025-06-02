package com.example.airapp

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrettyBar(hour:String,particles:String,progress:Float) {
    Row(modifier = Modifier.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(hour, color = Color(0xFF1E1B1B), modifier = Modifier.padding(horizontal = 2.dp))

        LinearProgressIndicator(
            progress = { progress },
            // Eliminar separación de barras
            gapSize = -15.dp,
            // Eliminar punto para indicar porcentaje
            drawStopIndicator = {},
            modifier = Modifier
                .height(17.dp)
                .padding(horizontal = 5.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = Color(0xFFB8E39B),  // Set the progress color
            trackColor = Color(0xFFB8E39B).copy(alpha = 0.3f),  // Optional: lighter track
        )
        Text(
            particles,
            color = Color(0xFF1E1B1B),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 1.dp)
        )

    }
}