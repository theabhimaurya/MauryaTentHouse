package com.live.mauryatenthouse.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.live.mauryatenthouse.R
import com.live.mauryatenthouse.data.database.InvoiceEntity
import com.live.mauryatenthouse.domain.model.Invoice
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme
import com.live.mauryatenthouse.ui.viewmodel.InvoiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: InvoiceViewModel = viewModel(), navController: NavController) {
    val devanagariFont = FontFamily(
        Font(resId = R.font.devanagari_regular, weight = FontWeight.Normal)
    )

    val savedInvoices by viewModel.allInvoices.collectAsState(initial = null)
    var invoiceToDelete by remember { mutableStateOf<InvoiceEntity?>(null) }

    if (invoiceToDelete != null) {
        AlertDialog(
            onDismissRequest = { invoiceToDelete = null },
            title = { Text("Confirm Delete / पुष्टि करें") },
            text = { Text("Are you sure you want to delete this invoice?\nक्या आप इस बिल को हटाना चाहते हैं?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        invoiceToDelete?.let { viewModel.deleteInvoice(it) }
                        invoiceToDelete = null
                    }
                ) {
                    Text("Yes / हाँ", color = Maroon)
                }
            },
            dismissButton = {
                TextButton(onClick = { invoiceToDelete = null }) {
                    Text("No / नहीं", color = Color.Gray)
                }
            },
            containerColor = Color.White,
            titleContentColor = Maroon,
            textContentColor = Color.DarkGray
        )
    }

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
                            text = "मौर्या टेंट हाउस",
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.resetForm()
                    navController.navigate(Routes.INVOICE)
                },
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

            if (savedInvoices == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Maroon)
                }
            } else if (savedInvoices!!.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 80.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_invoice),
                            contentDescription = null,
                            modifier = Modifier.size(240.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "No Invoices Found / कोई बिल नहीं मिला",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontFamily = devanagariFont
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Your saved bookings and history will appear here once you create a new invoice. / जब आप नया बिल बनाएंगे तो आपकी बुकिंग और इतिहास यहाँ दिखाई देगा।",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp),
                            lineHeight = 20.sp,
                            fontFamily = devanagariFont
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(savedInvoices!!) { invoiceEntity ->
                        InvoiceCard(
                            invoice = invoiceEntity,
                            font = devanagariFont,
                            onClick = {
                                val invoice = Invoice(
                                    customerEvent = invoiceEntity.customerEvent,
                                    customerName = invoiceEntity.customerName,
                                    customerAddress = invoiceEntity.customerAddress,
                                    customerPhone = invoiceEntity.customerPhone,
                                    invoiceNo = invoiceEntity.invoiceNo,
                                    date = invoiceEntity.date,
                                    items = invoiceEntity.items,
                                    laborWages = invoiceEntity.laborWages,
                                    transportFreight = invoiceEntity.transportFreight,
                                    discount = invoiceEntity.discount
                                )
                                InvoiceHolder.currentInvoice = invoice
                                navController.navigate(Routes.invoicePreview(true))
                            },
                            onDelete = { invoiceToDelete = invoiceEntity },
                            onEdit = {
                                viewModel.loadInvoiceForEditing(invoiceEntity)
                                navController.navigate(Routes.INVOICE)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InvoiceCard(invoice: InvoiceEntity, font: FontFamily, onClick: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                    Text(text = invoice.customerName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = invoice.customerPhone, fontSize = 14.sp, color = Color.Gray)
                }
                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
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
                color = Color(0xFFFFECB3), // Yellow status fallback
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = invoice.customerEvent,
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
                    Text(text = "Invoice No:", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = invoice.invoiceNo,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Maroon,
                        fontFamily = font
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Total Amount / कुल राशि", fontSize = 12.sp, color = Color.Gray, fontFamily = font)
                    Text(
                        text = "₹${invoice.grandTotal.toInt()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Maroon
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MauryaTentHouseTheme {
        HomeScreen(navController = rememberNavController())
    }
}
