package com.live.mauryatenthouse.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.navigation.NavController
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.live.mauryatenthouse.domain.model.Invoice
import java.io.File
import java.io.FileOutputStream


@Composable
fun InvoicePreviewScreen(navController: NavController) {

    val context = LocalContext.current
    val invoice = InvoiceHolder.currentInvoice ?: return

    Scaffold(
        topBar = {
            InvoicePreviewTopBar(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        createInvoicePdfFromCompose(context, invoice)
                    }
                ) {
                    Text("Save PDF")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val file = createInvoicePdfFromCompose(context, invoice)

                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )

                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/pdf"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        context.startActivity(
                            Intent.createChooser(intent, "Share Invoice PDF")
                        )
                    }
                ) {
                    Text("Share PDF")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            InvoiceOutPutUI(invoice = invoice)
        }
    }
}


@Composable
fun InvoicePreviewTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🔵 Circular Back Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = CircleShape
                )
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Invoice Preview",
            fontWeight = FontWeight.Bold
        )
    }
}


fun createInvoicePdfFromCompose(
    context: Context,
    invoice: Invoice
): File {

    val activity = context as ComponentActivity
    val density = context.resources.displayMetrics.density

    // A4 size in dp
    val pageWidthDp = 595f
    val pageHeightDp = 842f

    // ✅ Convert dp → px
    val pageWidthPx = (pageWidthDp * density).toInt()
    val pageHeightPx = (pageHeightDp * density).toInt()

    val root = FrameLayout(context)
    activity.addContentView(
        root,
        ViewGroup.LayoutParams(0, 0)
    )

    val composeView = ComposeView(context).apply {
        setViewTreeLifecycleOwner(activity)
        setViewTreeViewModelStoreOwner(activity)
        setViewTreeSavedStateRegistryOwner(activity)

        setContent {
            InvoiceOutPutUI(invoice = invoice)
        }
    }

    root.addView(composeView)

    // ✅ Measure using PX (NOT raw 595×842)
    composeView.measure(
        View.MeasureSpec.makeMeasureSpec(pageWidthPx, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(pageHeightPx, View.MeasureSpec.EXACTLY)
    )
    composeView.layout(0, 0, pageWidthPx, pageHeightPx)

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(
        pageWidthPx,
        pageHeightPx,
        1
    ).create()

    val page = pdfDocument.startPage(pageInfo)

    composeView.draw(page.canvas)

    pdfDocument.finishPage(page)

    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
        "Invoice_${System.currentTimeMillis()}.pdf"
    )

    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    root.removeView(composeView)

    return file
}

