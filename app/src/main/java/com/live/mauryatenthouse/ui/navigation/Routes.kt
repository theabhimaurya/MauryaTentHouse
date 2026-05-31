package com.live.mauryatenthouse.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val PIN = "pin"
    const val HOME = "home"
    const val INVOICE = "invoice"
    const val INVOICE_PREVIEW = "invoice_preview/{isReadOnly}"
    
    fun invoicePreview(isReadOnly: Boolean) = "invoice_preview/$isReadOnly"
}