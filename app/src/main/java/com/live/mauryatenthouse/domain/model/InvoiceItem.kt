package com.live.mauryatenthouse.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceItem(
    val serial: Int? = 0,
    val description: String,
    val qty: Int,
    val unitPrice: Double
) {
    val total: Double
        get() = qty * unitPrice
}

data class Invoice(
    val customerEvent: String,
    val customerName: String,
    val customerAddress: String,
    val customerPhone: String,
    val invoiceNo: String,
    val date: String,
    val items: List<InvoiceItem>,
    val laborWages: Double = 0.0,
    val transportFreight: Double = 0.0,
    val discount: Double = 0.0
) {
    val subTotal: Double
        get() = items.sumOf { it.total }

    val grandTotal: Double
        get() = subTotal + laborWages + transportFreight - discount
}
