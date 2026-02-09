package com.live.mauryatenthouse.domain.model

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
    val items: List<InvoiceItem>
) {
    val subTotal: Double
        get() = items.sumOf { it.total }
}
