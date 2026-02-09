package com.live.mauryatenthouse.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.live.mauryatenthouse.domain.model.InvoiceItem

class InvoiceViewModel : ViewModel() {

    var items = mutableStateListOf<InvoiceItem>()
        private set

    fun addItem(desc: String, qty: Int, price: Double) {
        items.add(
            InvoiceItem( items.size + 1, desc, qty, price)
        )
    }


    fun removeItem(index: Int) {
        items.removeAt(index)
    }

    fun getSubTotal(): Double {
        return items.sumOf { it.total }
    }
}