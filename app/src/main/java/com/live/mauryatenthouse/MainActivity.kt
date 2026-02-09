package com.live.mauryatenthouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.screens.HomeScreen
import com.live.mauryatenthouse.ui.screens.InvoicePreviewScreen
import com.live.mauryatenthouse.ui.screens.InvoiceScreen
import com.live.mauryatenthouse.ui.screens.PinLoginScreen
import com.live.mauryatenthouse.ui.screens.SplashScreen
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MauryaTentHouseTheme {
                AppNavGraph()
            }
        }
    }
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }
        composable(Routes.PIN) {
            PinLoginScreen(navController)
        }
        composable(Routes.HOME) {
//            HomeScreen()
            InvoiceScreen(navController = navController)
        }
        composable(Routes.INVOICE_PREVIEW) {
            InvoicePreviewScreen(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MauryaTentHouseTheme {
        AppNavGraph()
    }
}