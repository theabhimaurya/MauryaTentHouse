package com.live.mauryatenthouse.utils

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.live.mauryatenthouse.domain.model.InvoiceItem
import java.io.File
import java.io.FileOutputStream

object InvoicePdfGenerator {

    /*
    fun generate(
        context: Context,
        items: List<InvoiceItem>,
        customerName: String,
        mobile: String,
        programName: String,
        discount: Int
    ): File {

        val pdf = PdfDocument()
        val paint = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdf.startPage(pageInfo)
        val canvas = page.canvas

        var y = 40
        paint.textSize = 12f
        paint.typeface = Typeface.DEFAULT_BOLD

        canvas.drawText("टेंट हाउस सामान पर्ची", 200f, y.toFloat(), paint)
        y += 30

        paint.typeface = Typeface.DEFAULT
        canvas.drawText("नाम: $customerName", 20f, y.toFloat(), paint)
        canvas.drawText("मो.: $mobile", 350f, y.toFloat(), paint)
        y += 20

        canvas.drawText("प्रोग्राम: $programName", 20f, y.toFloat(), paint)
        y += 30

        // Table header
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("सामान", 20f, y.toFloat(), paint)
        canvas.drawText("पीस", 260f, y.toFloat(), paint)
        canvas.drawText("रेट", 320f, y.toFloat(), paint)
        canvas.drawText("₹", 420f, y.toFloat(), paint)
        y += 10
        canvas.drawLine(20f, y.toFloat(), 570f, y.toFloat(), paint)
        y += 20

        paint.typeface = Typeface.DEFAULT
        var total = 0

        items.forEach {
            canvas.drawText(it.name, 20f, y.toFloat(), paint)
            canvas.drawText(it.qty.toString(), 270f, y.toFloat(), paint)
            canvas.drawText(it.rate.toString(), 330f, y.toFloat(), paint)
            canvas.drawText(it.amount.toString(), 430f, y.toFloat(), paint)
            total += it.amount
            y += 20
        }

        y += 20
        paint.typeface = Typeface.DEFAULT_BOLD

        canvas.drawText("सामान का कुल ₹ : $total", 320f, y.toFloat(), paint)
        y += 20
        canvas.drawText("छूट ₹ : $discount", 320f, y.toFloat(), paint)
        y += 20
        canvas.drawText("कुल ₹ : ${total - discount}", 320f, y.toFloat(), paint)

        y += 40
        paint.textSize = 10f
        canvas.drawText("नोट: सामान टूट-फूट / खो जाने की जिम्मेदारी ग्राहक की होगी।", 20f, y.toFloat(), paint)

        pdf.finishPage(page)

        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "Invoice_${System.currentTimeMillis()}.pdf"
        )

        pdf.writeTo(FileOutputStream(file))
        pdf.close()

        return file
    }*/
}