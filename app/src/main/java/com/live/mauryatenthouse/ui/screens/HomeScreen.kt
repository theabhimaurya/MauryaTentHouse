package com.live.mauryatenthouse.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.live.mauryatenthouse.R
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme

private val LightMaroon = Color(0xFFFBE9E7)
private val YellowStatus = Color(0xFFFFECB3)
private val PinkStatus = Color(0xFFF8BBD0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val devanagariFont = FontFamily(
        Font(resId = R.font.devanagari_regular, weight = FontWeight.Normal)
    )

    Scaffold(
        containerColor = OffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Maurya Tent House",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Maroon
                        )
                        Text(
                            text = "मौर्य टेंट हाउस",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Maroon,
                            fontFamily = devanagariFont
                        )
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = { /* Change language */ },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("EN/हिं", fontSize = 10.sp, color = Color.DarkGray, lineHeight = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OffWhite)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = OffWhite,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PinkStatus.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "New", tint = Maroon)
                        }
                    },
                    label = { Text("New / नया", color = Maroon, fontSize = 12.sp, fontFamily = devanagariFont) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Info, contentDescription = "History") },
                    label = { Text("History / इतिहास", fontSize = 12.sp, fontFamily = devanagariFont) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings / सेटिंग्स", fontSize = 12.sp, fontFamily = devanagariFont) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.INVOICE) },
                containerColor = Maroon,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Saved Invoices / सुरक्षित किए गए बिल",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = devanagariFont
            )
            Text(
                text = "Recent history of bookings and transactions",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(sampleInvoices) { invoice ->
                    InvoiceCard(invoice, devanagariFont)
                }
            }
        }
    }
}

@Composable
fun InvoiceCard(invoice: InvoiceSummary, font: FontFamily) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(text = invoice.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = invoice.nameHindi, fontSize = 14.sp, color = Color.Gray, fontFamily = font)
                }
                Row {
                    IconButton(onClick = { /* Edit */ }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(onClick = { /* Delete */ }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Maroon, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = invoice.date, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = invoice.tagColor,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "${invoice.tag} / ${invoice.tagHindi}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontFamily = font
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(text = "Status / स्थिति", fontSize = 12.sp, color = Color.Gray, fontFamily = font)
                    Text(
                        text = "${invoice.status} / ${invoice.statusHindi}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = invoice.statusColor,
                        fontFamily = font
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Total Amount / कुल राशि", fontSize = 12.sp, color = Color.Gray, fontFamily = font)
                    Text(
                        text = "₹${invoice.amount}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Maroon
                    )
                }
            }
        }
    }
}

data class InvoiceSummary(
    val name: String,
    val nameHindi: String,
    val date: String,
    val tag: String,
    val tagHindi: String,
    val tagColor: Color,
    val status: String,
    val statusHindi: String,
    val statusColor: Color,
    val amount: String
)

val sampleInvoices = listOf(
    InvoiceSummary(
        "Ram Kumar Yadav", "राम कुमार यादव", "26/05/2026",
        "Wedding \uD83C\uDF89", "विवाह उत्सव", YellowStatus,
        "Pending", "लंबित", Maroon, "850"
    ),
    InvoiceSummary(
        "Shubham Singh", "शुभम सिंह", "12/06/2026",
        "Religious \uD83D\uDE4F", "धार्मिक उत्सव", PinkStatus,
        "Paid", "भुगतान किया", Color(0xFF388E3C), "4,200"
    ),
    InvoiceSummary(
        "Vikas Maurya", "विकास मौर्य", "15/06/2026",
        "Social \uD83D\uDCAA", "सामाजिक उत्सव", LightMaroon,
        "Advance", "अग्रिम", Color.DarkGray, "1,350"
    )
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MauryaTentHouseTheme {
        HomeScreen(rememberNavController())
    }
}
