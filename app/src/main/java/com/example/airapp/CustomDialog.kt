package com.example.airapp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.colorResource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

data class CurrentUser (
    val email: String = "",
    var username: String = ""
)

@Composable
fun CustomDialog(
    value: String,
    setShowDialog: (Boolean) -> Unit,
    current_user: CurrentUser?,
    onUsernameChanged: (String) -> Unit
) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }
    val scope = rememberCoroutineScope()
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ingresa tu nuevo nombre de usuario",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = { Text(text = "Flaviopal") },
                        value = txtField.value,
                        onValueChange = {
                            // Toma los 10 primeros carácteres
                            txtField.value = it.take(10)
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                // Para evitar valores nulos
                                if (txtField.value.isEmpty()) {
                                    txtFieldError.value = "Ingrese un nombre"
                                    return@Button
                                }
                                scope.launch {
                                    // Guarda el nombre en userPreferences
                                    changeUsername(current_user,txtField.value){
                                        onUsernameChanged(txtField.value)
                                    }
                                }
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Listo")
                        }
                    }
                }
            }
        }
    }
}

fun addUsername(user: CurrentUser?){
    val db = FirebaseFirestore.getInstance()
    if (user != null) {
        db.collection("users")
            .add(user)
    }
}
fun changeUsername(user: CurrentUser?, value: String,onSuccess: () -> Unit = {} ) {
    val db = FirebaseFirestore.getInstance()
    if (user != null) {
        db.collection("users").
        whereEqualTo("email", user.email)
            // Obtener documento del usuario actual, buscando por email
            .get()
            .addOnSuccessListener { querySnapshot ->
                val document = querySnapshot.documents.first()
                // Actualizar username con el nuevo valor
                document.reference.update("username", value)
                    .addOnSuccessListener {
                        user.username = value
                        onSuccess()
                    }

            }
    }
}
