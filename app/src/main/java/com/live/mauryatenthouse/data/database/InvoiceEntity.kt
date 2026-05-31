package com.live.mauryatenthouse.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.live.mauryatenthouse.domain.model.InvoiceItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerEvent: String,
    val customerName: String,
    val customerAddress: String,
    val customerPhone: String,
    val invoiceNo: String,
    val date: String,
    val items: List<InvoiceItem>,
    val laborWages: Double,
    val transportFreight: Double,
    val discount: Double,
    val grandTotal: Double
)

class Converters {
    @TypeConverter
    fun fromInvoiceItemList(value: List<InvoiceItem>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toInvoiceItemList(value: String): List<InvoiceItem> {
        return Json.decodeFromString(value)
    }
}
