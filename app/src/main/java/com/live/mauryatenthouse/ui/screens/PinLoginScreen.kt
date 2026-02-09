package com.live.mauryatenthouse.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.live.mauryatenthouse.ui.navigation.Routes

@Composable
fun PinLoginScreen(navController: NavController) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter PIN", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = pin,
            onValueChange = {
                if (it.length <= 4 && it.all { ch -> ch.isDigit() }) {
                    pin = it
                }
            },
            modifier = Modifier.fillMaxWidth(), // important for center alignment
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,   // ✅ center gravity
                fontSize = 18.sp
            ),
            label = {
                Text(
                    text = "PIN",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // optional: center label too
                )
            }
        )



        if (error) {
            Text("Wrong PIN", color = Color.Red)
        }


        Spacer(modifier = Modifier.height(20.dp))


        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (pin == "1234") {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.PIN) { inclusive = true }
                    }
                } else {
                    error = true
                }
            }
        ) {
            Text("Login")
        }
    }
}