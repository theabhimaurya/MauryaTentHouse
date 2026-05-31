package com.live.mauryatenthouse.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.live.mauryatenthouse.R
import com.live.mauryatenthouse.domain.model.Invoice
import com.live.mauryatenthouse.domain.model.InvoiceItem
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme
import com.live.mauryatenthouse.ui.viewmodel.InvoiceViewModel
import com.live.mauryatenthouse.utils.showToast
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoicePreviewScreen(
    viewModel: InvoiceViewModel = viewModel(),
    navController: NavController,
    isReadOnly: Boolean = false
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
                        "Invoice Preview / बिल पूर्वावलोकन",
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
            // Paper-like Invoice Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                InvoiceOutPutUI(invoice = invoice)
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
                        val file = createInvoicePdfFromCompose(context, invoice)
                        sharePdf(context, file)
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

            if (!isReadOnly) {
                InvoiceActionCard(
                    title = "Save Invoice",
                    subtitle = "Sync to local database",
                    icon = Icons.Default.ShoppingCart,
                    color = Color(0xFF004D61),
                    onClick = {
                        viewModel.saveInvoice(invoice) {
                            showToast(context, "Invoice Saved Successfully")
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Action Buttons Row 2
            InvoiceActionCard(
                title = "Quick Print",
                subtitle = "Established offline connection",
                icon = Icons.Default.Info,
                color = Maroon,
                onClick = { /* Print action */ }
            )

            Spacer(modifier = Modifier.height(12.dp))

            InvoiceActionCard(
                title = "Save PDF",
                subtitle = "Sync to local database",
                icon = Icons.Default.ShoppingCart,
                color = Color(0xFF004D61),
                onClick = {
                    createInvoicePdfFromCompose(context, invoice)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InvoiceActionCard(
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
fun InvoiceOutPutUI(invoice: Invoice) {
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Maroon
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "मौर्या टेंट हाउस",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Maroon,
                fontFamily = devanagariFont
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "लोहिंदा चौराहा, तेजीबाजार रोड, महराजगंज, जौनपुर उ०प्र०",
                fontSize = 11.sp,
                color = Color.DarkGray,
                fontFamily = devanagariFont
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "प्रो० अमर बहादुर मौर्या", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
                Text(text = "मो० 9415807288", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
            }
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = Maroon, thickness = 1.dp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Row 1
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("CUSTOMER / ग्राहक", fontSize = 10.sp, color = Color.Gray, fontFamily = devanagariFont)
                Text(invoice.customerName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("DATE / दिनांक", fontSize = 10.sp, color = Color.Gray, fontFamily = devanagariFont)
                Text(invoice.date, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Info Row 2
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("ADDRESS / पता", fontSize = 10.sp, color = Color.Gray, fontFamily = devanagariFont)
                Text(invoice.customerAddress, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("EVENT / कार्यक्रम", fontSize = 10.sp, color = Color.Gray, fontFamily = devanagariFont)
                Text(invoice.customerEvent, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Items Table
        Column(modifier = Modifier.border(0.5.dp, Color.LightGray)) {
            Row(
                modifier = Modifier.background(Color(0xFFF5F5F5)).padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("S.No.", modifier = Modifier.weight(0.15f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                VerticalDivider(modifier = Modifier.height(20.dp), color = Color.LightGray)
                Text("Item / सामान", modifier = Modifier.weight(0.55f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = devanagariFont)
                VerticalDivider(modifier = Modifier.height(20.dp), color = Color.LightGray)
                Text("PCS", modifier = Modifier.weight(0.15f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                VerticalDivider(modifier = Modifier.height(20.dp), color = Color.LightGray)
                Text("Total", modifier = Modifier.weight(0.15f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)

            invoice.items.forEachIndexed { index, item ->
                Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("${index + 1}", modifier = Modifier.weight(0.15f), textAlign = TextAlign.Center, fontSize = 12.sp)
                    VerticalDivider(modifier = Modifier.height(20.dp), color = Color.LightGray)
                    Text(item.description, modifier = Modifier.weight(0.55f), textAlign = TextAlign.Start, fontSize = 12.sp, modifier2 = Modifier.padding(start = 8.dp), fontFamily = devanagariFont)
                    VerticalDivider(modifier = Modifier.height(20.dp), color = Color.LightGray)
                    Text("${item.qty}", modifier = Modifier.weight(0.15f), textAlign = TextAlign.Center, fontSize = 12.sp)
                    VerticalDivider(modifier = Modifier.height(20.dp), color = Color.LightGray)
                    Text("₹${item.total.toInt()}", modifier = Modifier.weight(0.15f), textAlign = TextAlign.Center, fontSize = 12.sp)
                }
                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
            }

            // Summary Section
            SummaryRow("मजदूरी राशि खर्च / Labor Wages:", "₹${invoice.laborWages.toInt()}", devanagariFont)
            SummaryRow("गाड़ी भाड़ा खर्च / Transport Freight:", "₹${invoice.transportFreight.toInt()}", devanagariFont)
            SummaryRow("छूट / Discount:", "- ₹${invoice.discount.toInt()}", devanagariFont)
            
            Row(
                modifier = Modifier.background(Color(0xFFFFF3F3)).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "कुल रू० / TOTAL AMOUNT:",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Maroon,
                    fontFamily = devanagariFont
                )
                Text(
                    text = "₹${invoice.grandTotal.toInt()}",
                    modifier = Modifier.width(80.dp),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Maroon
                )
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

@Composable
fun SummaryRow(label: String, value: String, font: FontFamily) {
    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = font)
        Text(text = value, modifier = Modifier.width(80.dp), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
}

fun createInvoicePdfFromCompose(context: Context, invoice: Invoice): File {
    val activity = context as ComponentActivity
    val density = context.resources.displayMetrics.density
    val pageWidthPx = (595 * density).toInt()
    val pageHeightPx = (842 * density).toInt()

    val root = FrameLayout(context)
    activity.addContentView(root, ViewGroup.LayoutParams(0, 0))

    val composeView = ComposeView(context).apply {
        setViewTreeLifecycleOwner(activity)
        setViewTreeViewModelStoreOwner(activity)
        setViewTreeSavedStateRegistryOwner(activity)
        setContent {
            Surface(color = Color.White) {
                InvoiceOutPutUI(invoice = invoice)
            }
        }
    }
    root.addView(composeView)
    composeView.measure(View.MeasureSpec.makeMeasureSpec(pageWidthPx, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(pageHeightPx, View.MeasureSpec.EXACTLY))
    composeView.layout(0, 0, pageWidthPx, pageHeightPx)

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidthPx, pageHeightPx, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    composeView.draw(page.canvas)
    pdfDocument.finishPage(page)

    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Invoice_${invoice.invoiceNo}.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()
    root.removeView(composeView)
    return file
}

fun sharePdf(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share Invoice PDF"))
}

@Composable
private fun Text(text: String, modifier: Modifier, textAlign: TextAlign, fontSize: androidx.compose.ui.unit.TextUnit, modifier2: Modifier, fontFamily: FontFamily) {
    Text(text = text, modifier = modifier.then(modifier2), textAlign = textAlign, fontSize = fontSize, fontFamily = fontFamily)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvoicePreviewScreenPreview() {
    MauryaTentHouseTheme {
        InvoicePreviewScreen(navController = rememberNavController())
    }
}
