package com.live.mauryatenthouse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.live.mauryatenthouse.R
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

val Maroon = Color(0xFF800000)
val OffWhite = Color(0xFFFFF9F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinLoginScreen(navController: NavController) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    val devanagariFont = FontFamily(
        Font(resId = R.font.devanagari_regular, weight = FontWeight.Normal)
    )

    LaunchedEffect(pin) {
        if (pin.length == 4) {
            if (pin == "1234") {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.PIN) { inclusive = true }
                }
            } else {
                error = true
                pin = "" // Clear PIN on error
            }
        }
    }

    Scaffold(
        containerColor = OffWhite,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Login / लॉगिन",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Maroon
                        )
                    },
                    actions = {
                        TextButton(onClick = { /* Toggle Language */ }) {
                            Text(
                                "EN/हिं",
                                color = Maroon,
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.5f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = OffWhite
                    )
                )
                HorizontalDivider(thickness = 0.5.dp, color = Maroon.copy(alpha = 0.2f))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                "Maurya Tent House",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Maroon,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "मौर्या टेंट हाउस",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = devanagariFont,
                color = Maroon,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "Enter Security PIN / सुरक्षा पिन दर्ज करें",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = devanagariFont,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // PIN Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    PinDot(isFilled = index < pin.length)
                }
            }

            if (error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Wrong PIN", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Numeric Keypad
            NumericKeypad(
                onNumberClick = { num ->
                    if (pin.length < 4) {
                        pin += num
                        error = false
                    }
                },
                onBackspaceClick = {
                    if (pin.isNotEmpty()) {
                        pin = pin.dropLast(1)
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = { /* Forgot PIN */ }) {
                Text(
                    "Forgot PIN? / पिन भूल गए?",
                    color = Maroon,
                    fontFamily = devanagariFont,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PinLoginScreenPreview() {
    MauryaTentHouseTheme {
        PinLoginScreen(navController = rememberNavController())
    }
}

@Composable
fun PinDot(isFilled: Boolean) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(if (isFilled) Maroon else Color.Transparent)
            .border(1.dp, Maroon, CircleShape)
    )
}

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onBackspaceClick: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "backspace")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        keys.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { key ->
                    KeypadButton(
                        key = key,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            when (key) {
                                "backspace" -> onBackspaceClick()
                                "" -> {}
                                else -> onNumberClick(key)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun KeypadButton(
    key: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        enabled = key.isNotEmpty(),
        modifier = modifier
            .aspectRatio(1.5f)
            .border(0.1.dp, Color.LightGray.copy(alpha = 0.5f)),
        color = Color.White
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (key == "backspace") {
                Text(
                    text = "⌫",
                    fontSize = 24.sp,
                    color = Color.Black
                )
            } else {
                Text(
                    text = key,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
        }
    }
}
