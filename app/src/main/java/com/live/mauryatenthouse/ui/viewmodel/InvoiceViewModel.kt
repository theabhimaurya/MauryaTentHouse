package com.live.mauryatenthouse.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.live.mauryatenthouse.data.database.AppDatabase
import com.live.mauryatenthouse.data.database.InvoiceEntity
import com.live.mauryatenthouse.domain.model.Invoice
import com.live.mauryatenthouse.domain.model.InvoiceItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InvoiceViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.invoiceDao()

    var editingInvoiceId by mutableStateOf<Long?>(null)
        private set

    var items = mutableStateListOf<InvoiceItem>()
        private set

    var customerName by mutableStateOf("")
    var customerPhone by mutableStateOf("")
    var customerAddress by mutableStateOf("")
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

    val allInvoices: Flow<List<InvoiceEntity>> = dao.getAllInvoices()

    fun saveInvoice(invoice: Invoice, onComplete: () -> Unit) {
        viewModelScope.launch {
            val entity = InvoiceEntity(
                id = editingInvoiceId ?: 0L,
                customerEvent = invoice.customerEvent,
                customerName = invoice.customerName,
                customerAddress = invoice.customerAddress,
                customerPhone = invoice.customerPhone,
                invoiceNo = invoice.invoiceNo,
                date = invoice.date,
                items = invoice.items,
                laborWages = invoice.laborWages,
                transportFreight = invoice.transportFreight,
                discount = invoice.discount,
                grandTotal = invoice.grandTotal
            )
            dao.upsertInvoice(entity)
            clearForm()
            onComplete()
        }
    }

    fun loadInvoiceForEditing(invoice: InvoiceEntity) {
        editingInvoiceId = invoice.id
        customerName = invoice.customerName
        customerPhone = invoice.customerPhone
        customerAddress = invoice.customerAddress
        customerEvent = invoice.customerEvent
        dueDate = invoice.date
        laborWages = invoice.laborWages.toInt().let { if (it == 0) "" else it.toString() }
        transportFreight = invoice.transportFreight.toInt().let { if (it == 0) "" else it.toString() }
        discount = invoice.discount.toInt().let { if (it == 0) "" else it.toString() }
        items.clear()
        items.addAll(invoice.items)
    }

    fun resetForm() {
        clearForm()
    }

    fun deleteInvoice(invoice: InvoiceEntity) {
        viewModelScope.launch {
            dao.deleteInvoice(invoice)
        }
    }

    private fun clearForm() {
        editingInvoiceId = null
        items.clear()
        customerName = ""
        customerPhone = ""
        customerAddress = ""
        customerEvent = ""
        dueDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        laborWages = ""
        transportFreight = ""
        discount = ""
    }
}
