package com.live.mauryatenthouse.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.live.mauryatenthouse.domain.model.InvoiceItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InvoiceViewModel : ViewModel() {

    var items = mutableStateListOf<InvoiceItem>()
        private set

    var customerName by mutableStateOf("")
    var customerPhone by mutableStateOf("")
    var customerAddress by mutableStateOf("तेजी बाजार, जौनपुर")
    var customerEvent by mutableStateOf("")
    var dueDate by mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
    
    var laborWages by mutableStateOf("")
    var transportFreight by mutableStateOf("")
    var discount by mutableStateOf("")

    fun addItem(desc: String, qty: Int, price: Double) {
        items.add(
            InvoiceItem(items.size + 1, desc, qty, price)
        )
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
    }

    fun getSubTotal(): Double {
        return items.sumOf { it.total }
    }

    fun getGrandTotal(): Double {
        val labor = laborWages.toDoubleOrNull() ?: 0.0
        val freight = transportFreight.toDoubleOrNull() ?: 0.0
        val disc = discount.toDoubleOrNull() ?: 0.0
        return getSubTotal() + labor + freight - disc
    }
}
