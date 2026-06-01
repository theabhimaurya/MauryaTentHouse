package com.live.mauryatenthouse.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import com.live.mauryatenthouse.domain.model.Invoice
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme
import com.live.mauryatenthouse.ui.viewmodel.InvoiceViewModel
import com.live.mauryatenthouse.utils.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPreviewScreen(
    viewModel: InvoiceViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val invoice = InvoiceHolder.currentInvoice ?: return
    val devanagariFont = FontFamily(Font(resId = R.font.devanagari_regular, weight = FontWeight.Normal))

    Scaffold(
        containerColor = OffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Booking Preview / बुकिंग पूर्वावलोकन",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Maroon,
                        fontFamily = devanagariFont
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Maroon)
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Paper-like Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                BookingOutputUI(invoice = invoice)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons Row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.DarkGray)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.DarkGray)
                    Spacer(Modifier.width(8.dp))
                    Text("Modify / सुधारें", color = Color.DarkGray, fontFamily = devanagariFont)
                }

                Button(
                    onClick = {
                        showToast(context, "Sharing Booking...")
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Share / भेजें", fontFamily = devanagariFont)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BookingActionCard(
                title = "Save Booking",
                subtitle = "Sync to local database",
                icon = Icons.Default.ShoppingCart,
                color = Color(0xFF004D61),
                onClick = {
                    viewModel.saveInvoice(invoice) {
                        showToast(context, "Booking Saved Successfully")
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            BookingActionCard(
                title = "Quick Print",
                subtitle = "Established offline connection",
                icon = Icons.Default.Info,
                color = Maroon,
                onClick = { /* Print action */ }
            )

            Spacer(modifier = Modifier.height(12.dp))

            BookingActionCard(
                title = "Save PDF",
                subtitle = "Sync to local database",
                icon = Icons.Default.Info,
                color = Color(0xFF004D61),
                onClick = {
                    showToast(context, "Saving PDF...")
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BookingActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF3F3),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(8.dp),
                color = color
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun BookingOutputUI(invoice: Invoice) {
    val devanagariFont = FontFamily(Font(resId = R.font.devanagari_regular, weight = FontWeight.Normal))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Maurya Tent House",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Maroon
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "मौर्या टेंट हाउस",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Maroon,
                fontFamily = devanagariFont
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "लोहिंदा चौराहा, तेजीबाजार रोड, महराजगंज, जौनपुर उ०प्र०",
                fontSize = 10.sp,
                color = Color.DarkGray,
                fontFamily = devanagariFont,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "प्रो० अमर बहादुर मौर्या", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
                Text(text = "मो० 9415807288", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
            }
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = Maroon, thickness = 1.dp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Rows
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("CUSTOMER / ग्राहक", fontSize = 9.sp, color = Color.Gray, fontFamily = devanagariFont)
                Text(invoice.customerName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("DATE / दिनांक", fontSize = 9.sp, color = Color.Gray, fontFamily = devanagariFont)
                Text(invoice.date, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("ADDRESS / पता", fontSize = 9.sp, color = Color.Gray, fontFamily = devanagariFont)
                Text(invoice.customerAddress, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("EVENT / कार्यक्रम", fontSize = 9.sp, color = Color.Gray, fontFamily = devanagariFont)
                Text(invoice.customerEvent, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Items Table
        Column(modifier = Modifier.border(0.5.dp, Color.LightGray)) {
            Row(
                modifier = Modifier.background(Color(0xFFF9F9F9)).padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("S.No.", modifier = Modifier.weight(0.2f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                VerticalDivider(modifier = Modifier.height(16.dp), color = Color.LightGray)
                Text("Item / सामान", modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = devanagariFont)
                VerticalDivider(modifier = Modifier.height(16.dp), color = Color.LightGray)
                Text("PCS", modifier = Modifier.weight(0.2f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)

            invoice.items.forEachIndexed { index, item ->
                Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("${index + 1}", modifier = Modifier.weight(0.2f), textAlign = TextAlign.Center, fontSize = 12.sp)
                    VerticalDivider(modifier = Modifier.height(16.dp), color = Color.LightGray)
                    Text(item.description, modifier = Modifier.weight(0.6f).padding(start = 8.dp), textAlign = TextAlign.Start, fontSize = 12.sp, fontFamily = devanagariFont)
                    VerticalDivider(modifier = Modifier.height(16.dp), color = Color.LightGray)
                    Text("${item.qty}", modifier = Modifier.weight(0.2f), textAlign = TextAlign.Center, fontSize = 12.sp)
                }
                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Footer Note
        Column {
            Text("नोट (Note):", fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = devanagariFont)
            Text("• हमारे यहाँ शादी व विवाह कभी किसी भी शुभ अवसर पर टेंट सम्बन्धित लिया जाता है।", fontSize = 10.sp, color = Color.DarkGray, fontFamily = devanagariFont)
            Text("• सामान की टूट-फूट या खोने पर ग्राहक उत्तरदायी होगा।", fontSize = 10.sp, color = Color.DarkGray, fontFamily = devanagariFont)
            Text("• किराये का भुगतान कार्यक्रम के तुरंत बाद सुनिश्चित करें।", fontSize = 10.sp, color = Color.DarkGray, fontFamily = devanagariFont)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookingPreviewScreenPreview() {
    MauryaTentHouseTheme {
        BookingPreviewScreen(navController = rememberNavController())
    }
}
