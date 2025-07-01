package com.example.airapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    val primaryAccentColor = Color(0xFFB8E39B)
    val textPrimaryColor = Color.Black
    val textSecondaryColor = Color.Gray
    val poppinsFamily = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido de Nuevo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor,
                fontFamily = poppinsFamily,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Inicia sesión para continuar",
                fontSize = 16.sp,
                color = textSecondaryColor,
                fontFamily = poppinsFamily,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Correo Electrónico", fontFamily = poppinsFamily) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = errorMessage != null,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = textPrimaryColor,
                    unfocusedTextColor = textPrimaryColor,
                    cursorColor = primaryAccentColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = primaryAccentColor,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedLabelColor = primaryAccentColor,
                    unfocusedLabelColor = textSecondaryColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Contraseña", fontFamily = poppinsFamily) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = errorMessage != null,
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description, tint = textSecondaryColor)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = textPrimaryColor,
                    unfocusedTextColor = textPrimaryColor,
                    cursorColor = primaryAccentColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = primaryAccentColor,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedLabelColor = primaryAccentColor,
                    unfocusedLabelColor = textSecondaryColor
                )
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "El correo y la contraseña no pueden estar vacíos."
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    auth.signInWithEmailAndPassword(email.trim(), password.trim())
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                                navController.navigate("Home") {
                                    popUpTo("Login") { inclusive = true }
                                }
                            } else {
                                val exception = task.exception
                                errorMessage = exception?.message ?: "Error desconocido en el inicio de sesión."
                            }
                        }
                },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryAccentColor,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Ingresar",
                        fontSize = 18.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "¿No tienes una cuenta? ",
                    color = textSecondaryColor,
                    fontFamily = poppinsFamily
                )
                ClickableText(
                    text = AnnotatedString("Regístrate"),
                    onClick = {
                        navController.navigate("Register")
                    },
                    style = TextStyle(
                        color = primaryAccentColor,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFamily
                    )
                )
            }
        }
    }
}