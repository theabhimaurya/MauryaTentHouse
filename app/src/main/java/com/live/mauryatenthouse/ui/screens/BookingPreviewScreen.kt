package com.live.mauryatenthouse.ui.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.live.mauryatenthouse.R
import com.live.mauryatenthouse.domain.model.Invoice
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme
import com.live.mauryatenthouse.ui.viewmodel.InvoiceViewModel
import com.live.mauryatenthouse.utils.showToast
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPreviewScreen(
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
                        val file = createBookingPdfFromCompose(context, invoice)
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
            }

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
                subtitle = "Save to Downloads folder",
                icon = Icons.Default.Info,
                color = Color(0xFF004D61),
                onClick = {
                    val saved = saveBookingPdfToDownloads(context, invoice)
                    if (saved) showToast(context, "PDF saved to Downloads")
                    else showToast(context, "Failed to save PDF")
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
        Column(modifier = Modifier.fillMaxWidth().border(0.5.dp, Color.LightGray)) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF9F9F9)).padding(vertical = 8.dp),
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
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
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

// A4 dimensions in PDF points (1 pt = 1/72 inch). Never multiply by density.
private const val BOOKING_A4_WIDTH_PT  = 595
private const val BOOKING_A4_HEIGHT_PT = 842
// Blank white space added at the bottom of every page and top of pages 2+
private const val BOOKING_PAGE_MARGIN_PT = 24

private fun createBookingPdfFromCompose(context: Context, invoice: Invoice): File {
    val activity = context as ComponentActivity
    val density  = context.resources.displayMetrics.density

    // ── 1. Render width: A4 point-width × screen density for sharp text ───────
    // e.g. on xxhdpi (density=3): 595 × 3 = 1785 px
    val renderWidth = (BOOKING_A4_WIDTH_PT * density).toInt()

    // ── 2. Attach ComposeView to the real window so fillMaxWidth() resolves ───
    // Using decorView (not addContentView) gives ComposeView a proper window
    // token, which is required for fillMaxWidth() to have a parent constraint.
    val decorView = activity.window.decorView as ViewGroup
    val container = FrameLayout(context)
    decorView.addView(
        container,
        FrameLayout.LayoutParams(renderWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    )

    val composeView = ComposeView(context).apply {
        setViewTreeLifecycleOwner(activity)
        setViewTreeViewModelStoreOwner(activity)
        setViewTreeSavedStateRegistryOwner(activity)
        setContent {
            Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
                BookingOutputUI(invoice = invoice)
            }
        }
    }
    container.addView(
        composeView,
        FrameLayout.LayoutParams(renderWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    )

    // ── 3. Synchronous measure + layout ───────────────────────────────────────
    composeView.measure(
        View.MeasureSpec.makeMeasureSpec(renderWidth, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(0,           View.MeasureSpec.UNSPECIFIED)
    )
    val renderHeight = composeView.measuredHeight
    composeView.layout(0, 0, renderWidth, renderHeight)

    // ── 4. Draw into a Bitmap (synchronous, guaranteed complete) ──────────────
    val bitmap = Bitmap.createBitmap(renderWidth, renderHeight, Bitmap.Config.ARGB_8888)
    val bitmapCanvas = Canvas(bitmap)
    bitmapCanvas.drawColor(android.graphics.Color.WHITE)
    composeView.draw(bitmapCanvas)

    val scale       = BOOKING_A4_WIDTH_PT.toFloat() / renderWidth.toFloat()
    val scaledTotal = (renderHeight * scale).toInt()

    val pdfDocument = PdfDocument()
    val paint   = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    val bgPaint = Paint().apply { color = android.graphics.Color.WHITE }

    var bitmapYPt  = 0   // PDF points of bitmap consumed by previous pages
    var pageNumber = 1

    while (bitmapYPt < scaledTotal) {
        val isFirstPage  = pageNumber == 1
        val topMarginPt  = if (isFirstPage) 0 else BOOKING_PAGE_MARGIN_PT
        // Usable content rows per page after reserving margins
        val contentSlice = BOOKING_A4_HEIGHT_PT - topMarginPt - BOOKING_PAGE_MARGIN_PT

        val remaining    = scaledTotal - bitmapYPt
        val drawnContent = minOf(contentSlice, remaining)
        val pageHeight   = topMarginPt + drawnContent + BOOKING_PAGE_MARGIN_PT

        val pageInfo = PdfDocument.PageInfo.Builder(BOOKING_A4_WIDTH_PT, pageHeight, pageNumber).create()
        val page     = pdfDocument.startPage(pageInfo)
        val canvas   = page.canvas

        // Fill whole page white so margin bands are clean
        canvas.drawRect(0f, 0f, BOOKING_A4_WIDTH_PT.toFloat(), pageHeight.toFloat(), bgPaint)

        // src: exact pixel strip of the bitmap for this page (integer Rect)
        val srcTop    = (bitmapYPt / scale).toInt()
        val srcBottom = ((bitmapYPt + drawnContent) / scale).toInt()
        val src = android.graphics.Rect(0, srcTop, renderWidth, srcBottom)

        // dst: where to place the strip on the PDF canvas (integer Rect)
        val dst = android.graphics.Rect(
            0, topMarginPt,
            BOOKING_A4_WIDTH_PT, topMarginPt + drawnContent
        )

        // Single drawBitmap with src+dst — no stacked transforms, no repeat bug
        canvas.drawBitmap(bitmap, src, dst, paint)

        pdfDocument.finishPage(page)

        bitmapYPt  += drawnContent
        pageNumber++
    }

    // ── 6. Write PDF to disk ──────────────────────────────────────────────────
    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
        "Booking_${invoice.invoiceNo}.pdf"
    )
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()
    bitmap.recycle()

    // ── 7. Clean up off-screen views ─────────────────────────────────────────
    container.removeView(composeView)
    decorView.removeView(container)

    return file
}

// ─── SAVE TO DOWNLOADS ────────────────────────────────────────────────────────

/**
 * Saves the booking PDF to the public Downloads folder so it is visible
 * in the phone's Files / Downloads app.
 *
 * Android 10+ (API 29+): uses MediaStore — no WRITE_EXTERNAL_STORAGE permission needed.
 * Android 9  (API 28-): writes directly to Environment.DIRECTORY_DOWNLOADS.
 *
 * Returns true on success, false on any error.
 */
fun saveBookingPdfToDownloads(context: Context, invoice: Invoice): Boolean {
    return try {
        val fileName = "Booking_${invoice.invoiceNo}.pdf"
        // Generate PDF into app's private cache first
        val tempFile = createBookingPdfFromCompose(context, invoice)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // ── Android 10+ : MediaStore API ─────────────────────────────────
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: return false

            resolver.openOutputStream(uri)?.use { out ->
                tempFile.inputStream().use { it.copyTo(out) }
            }

            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        } else {
            // ── Android 9 and below : direct file write ───────────────────────
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloadsDir.mkdirs()
            val destFile = File(downloadsDir, fileName)
            tempFile.inputStream().use { input ->
                destFile.outputStream().use { input.copyTo(it) }
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookingPreviewScreenPreview() {
    MauryaTentHouseTheme {
        BookingPreviewScreen(navController = rememberNavController())
    }
}