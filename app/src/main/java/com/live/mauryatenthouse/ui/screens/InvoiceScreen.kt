package com.live.mauryatenthouse.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.live.mauryatenthouse.R
import com.live.mauryatenthouse.domain.model.Invoice
import com.live.mauryatenthouse.domain.model.InvoiceItem
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.viewmodel.InvoiceViewModel
import com.live.mauryatenthouse.utils.showToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.io.encoding.Base64


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(viewModel: InvoiceViewModel = viewModel(), navController: NavController) {

    val itemList = listOf(
        "चारपाई",
        "फाइबर कुर्सी",
        "VIP कुर्सी",
        "सोफा",
        "सेंटर टेबल",
        "मेज",
        "गोल मेज",
        "मैट",
        "स्टेज मेज",
        "मेन गेट",
        "ट्रस्ट",
        "36 फीट राउंड",
        "18 फीट राउंड",
        "प्लेन टेंट",
        "कनोपी",
        "कनोपी ट्रस्ट",
        "जनवाश टेंट",
        "बाउंड्री टेंट",
        "हलवाई टेंट",
        "गैलरी",
        "गद्दा",
        "फोम गद्दा",
        "रजाई",
        "कंबल",
        "पंखा",
        "बड़ा कुलर",
        "छोटा कुलर",
        "कॉफी मशीन",
        "भगोना",
        "कराही",
        "नइया",
        "डस्टबिन",
        "थार",
        "सर्विस ट्रे",
        "बाल्टी",
        "कलछुल",
        "ट्रे",
        "बफर सेट",
        "ड्रम",
        "जग",
        "गैस चूल्हा",
        "भट्टी",
        "पेपर सेल साइडर",
        "LED काउंटर",
        "जयमाल सेट",
        "मंडप हल्दी सेट",
        "लाइट",
        "भाड़ा मजदूरी",
        "वेटर",
        "अन्य"
    )

    var expanded by remember { mutableStateOf(false) }
    var desc by rememberSaveable { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var invoiceFor by rememberSaveable { mutableStateOf("Name") }
    var address by rememberSaveable { mutableStateOf("लोहिंदा चौराहा महराजगंज") }
    var phoneNumber by rememberSaveable { mutableStateOf("0123456789") }
    var projectName by rememberSaveable { mutableStateOf("") }
    var dueDate by rememberSaveable {
        mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
    }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showOtherDialog by remember { mutableStateOf(false) }
    var otherItemText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    val invoice = Invoice(
        customerEvent = projectName,
        customerName = invoiceFor,
        customerAddress = address,
        customerPhone = phoneNumber,
        invoiceNo = "123456",
        date = dueDate,
        items = viewModel.items
    )

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    if (viewModel.items.isEmpty()) {
                        showToast(context, "कृपया पहले समान जोड़ें")
                        return@Button
                    }
                    InvoiceHolder.currentInvoice = invoice
                    navController.navigate(Routes.INVOICE_PREVIEW)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("बिल देखें")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {

            InvoiceHeader(
                dueDate = dueDate,
                onDateClick = { showDatePicker = true }
            )


            Spacer(Modifier.height(24.dp))

            InvoiceInfoRow(
                invoiceFor = invoiceFor,
                address = address,
                phoneNumber = phoneNumber,
                projectName = projectName,
                onInvoiceForChange = { invoiceFor = it },
                onAddressChange = { address = it },
                onPhoneNumberChange = { phoneNumber = it },
                onProjectChange = { projectName = it }
            )

            Spacer(Modifier.height(24.dp))

            // ---- ITEM INPUT UI (unchanged) ----
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = desc,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("समान") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    itemList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                expanded = false
                                if (item == "अन्य") {
                                    otherItemText = ""
                                    showOtherDialog = true   // ✅ OPEN DIALOG
                                } else {
                                    desc = item             // ✅ NORMAL SELECTION
                                }
                            }
                        )
                    }
                }

            }

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                OutlinedTextField(
                    value = qty,
                    onValueChange = { newValue ->
                        // optional: only digits allow
                        if (newValue.all { it.isDigit() }) {
                            qty = newValue
                        }
                    },
                    label = { Text("पीस") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { newValue ->
                        // allow digits + decimal
                        if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            price = newValue
                        }
                    },
                    label = { Text("समान का दाम") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.weight(1f)
                )
            }


            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    // 1️⃣ Item name validation
                    if (desc.isBlank()) {
                        showToast(context, "कृपया समान चुनें")
                        return@Button
                    }

                    // 2️⃣ Quantity validation
                    if (qty.isBlank()) {
                        showToast(context, "कृपया पीस भरें")
                        return@Button
                    }

                    val qtyInt = qty.toIntOrNull()
                    if (qtyInt == null || qtyInt <= 0) {
                        showToast(context, "पीस सही संख्या में भरें")
                        return@Button
                    }

                    // 3️⃣ Price validation
                    if (price.isBlank()) {
                        showToast(context, "कृपया दाम भरें")
                        return@Button
                    }

                    val priceDouble = price.toDoubleOrNull()
                    if (priceDouble == null || priceDouble <= 0) {
                        showToast(context, "दाम सही संख्या में भरें")
                        return@Button
                    }

                    // 4️⃣ Duplicate item validation
                    val alreadyAdded = viewModel.items.any {
                        it.description == desc
                    }

                    if (alreadyAdded) {
                        showToast(context, "यह समान पहले से जोड़ा गया है")
                        return@Button
                    }

                    // ✅ All validations passed
                    viewModel.addItem(desc, qtyInt, priceDouble)

                    // Clear fields
                    desc = ""
                    qty = ""
                    price = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("समान जोड़े")
            }


            Spacer(Modifier.height(24.dp))

            // ✅ Scrollable table
            InvoiceTable(
                items = viewModel.items,
                subTotal = viewModel.getSubTotal()
            )

            Spacer(Modifier.height(80.dp)) // ⬅ space above fixed button
        }
    }

    // 🔹 DATE PICKER
    if (showDatePicker) {
        LaunchedEffect(Unit) {
            val calendar = Calendar.getInstance()

            val picker = DatePickerDialog(
                context,
                { _, year, month, day ->
                    dueDate = "%02d/%02d/%d".format(day, month + 1, year)
                    showDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            picker.setOnDismissListener {
                showDatePicker = false
            }

            picker.show()
        }
    }

    if (showOtherDialog) {
        AlertDialog(
            onDismissRequest = { showOtherDialog = false },
            title = { Text("अन्य सामान जोड़ें") },
            text = {
                OutlinedTextField(
                    value = otherItemText,
                    onValueChange = { otherItemText = it },
                    label = { Text("समान का नाम") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (otherItemText.isNotBlank()) {
                            desc = otherItemText.trim() // ✅ SET AS SELECTED
                            showOtherDialog = false
                        }
                    }
                ) {
                    Text("जोड़ें")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtherDialog = false }) {
                    Text("रद्द करें")
                }
            }
        )
    }


}

@Composable
fun InvoiceHeader(
    dueDate: String,
    onDateClick: () -> Unit
) {
    Column {

        Text(
            text = "मौर्या टेन्ट हाउस",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A64F5)
        )
        Text("लोहिंदा चौराहा, तेजीबाजार रोड,")
        Text("महराजगंज जौनपुर ऊ० प्र०")
        Text("9415807288")

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Invoice",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.clickable { onDateClick() } // ✅ CLICK HERE
            ) {
                Text(
                    text = "दिनांक",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dueDate,
                    fontSize = 14.sp,
                    color = Color(0xFF5A64F5)
                )
            }
        }
    }
}


@Composable
fun InvoiceInfoRow(
    invoiceFor: String,
    address: String,
    phoneNumber: String,
    projectName: String,
    onInvoiceForChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onProjectChange: (String) -> Unit,
) {

    var showTextDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogValue by remember { mutableStateOf("") }
    var onSave: ((String) -> Unit)? by remember { mutableStateOf(null) }

    // 🔹 TEXT INPUT DIALOG
    if (showTextDialog) {
        AlertDialog(
            onDismissRequest = { showTextDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onSave?.invoke(dialogValue)
                    showTextDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTextDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text(dialogTitle) },
            text = {
                if (dialogTitle == "ग्राहक का फोन नंबर") {
                    OutlinedTextField(
                        value = dialogValue,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        onValueChange = { newValue ->
                            if (newValue.length <= 10 && newValue.all { it.isDigit() }) {
                                dialogValue = newValue
                            }},
                        singleLine = true
                    )
                }else {
                    OutlinedTextField(
                        value = dialogValue,
                        onValueChange = { dialogValue = it },
                        singleLine = true
                    )
                }
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // -------- Invoice For --------
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("ग्राहक का नाम/पता", fontWeight = FontWeight.Bold)

            Text(
                text = invoiceFor,
                modifier = Modifier.clickable {
                    dialogTitle = "ग्राहक का नाम"
                    dialogValue = invoiceFor
                    onSave = onInvoiceForChange
                    showTextDialog = true
                }
            )

            Text(
                text = address,
                color = Color.Black,
                maxLines = 3,              // ✅ prevent overflow
                fontSize = 13.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable {
                    dialogTitle = "ग्राहक का पता"
                    dialogValue = address
                    onSave = onAddressChange
                    showTextDialog = true
                }
            )

            Text(
                text = phoneNumber,
                color = Color.Black,
                modifier = Modifier.clickable {
                    dialogTitle = "ग्राहक का फोन नंबर"
                    dialogValue = phoneNumber
                    onSave = onPhoneNumberChange
                    showTextDialog = true
                }
            )
        }

        // -------- Project --------
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("कार्यक्रम का नाम", fontWeight = FontWeight.Bold)

            EventDropdown(
                selectedEvent = projectName,
                onEventSelected = { selected ->
                    onProjectChange(selected) // ✅ AUTO-FILL
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDropdown(
    selectedEvent: String,
    onEventSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val eventCategories = mapOf(
        "शादी कार्यक्रम" to listOf(
            "सगाई", "तिलक", "हल्दी", "मेहंदी", "संगीत",
            "बारात", "जयमाल", "विवाह", "रिसेप्शन", "विदाई"
        ),

        "धार्मिक कार्यक्रम" to listOf(
            "सत्यनारायण कथा", "हवन / यज्ञ", "भागवत कथा",
            "राम कथा", "कीर्तन", "जागरण", "पूजा पाठ"
        ),

        "शोक कार्यक्रम" to listOf(
            "शोक सभा", "तेरहवीं", "ब्रह्मभोज",
            "बरसी", "श्रद्धांजलि सभा"
        ),

        "सामाजिक कार्यक्रम" to listOf(
            "जन्मदिन", "नामकरण", "मुंडन",
            "छठ पूजा", "दुर्गा पूजा", "गणेश पूजा"
        ),

        "व्यावसायिक कार्यक्रम" to listOf(
            "दुकान उद्घाटन", "शिलान्यास", "मीटिंग",
            "सेमिनार", "कॉन्फ्रेंस", "प्रोडक्ट लॉन्च"
        )
    )


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedEvent,
            onValueChange = {},
            readOnly = true,
            label = { Text("कार्यक्रम का नाम",
                fontSize = 13.sp) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            eventCategories.forEach { (category, events) ->

                // 🔹 Category Header
                DropdownMenuItem(
                    text = {
                        Text(
                            category,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    enabled = false,
                    onClick = {}
                )

                // 🔹 Events
                events.forEach { event ->
                    DropdownMenuItem(
                        text = { Text("• $event") },
                        onClick = {
                            onEventSelected(event)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun InvoiceTable(items: List<InvoiceItem>, subTotal: Double) {

    Spacer(Modifier.height(24.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(8.dp)
    ) {
        Text("समान", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
        Text("पीस", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("दाम", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text("कुल रू०", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
    }

    Divider()

    items.forEach {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(it.description, modifier = Modifier.weight(2f))
            Text(it.qty.toString(), modifier = Modifier.weight(1f))
            Text("₹${it.unitPrice.toInt()}", modifier = Modifier.weight(1f))
            Text("₹${it.total.toInt()}", modifier = Modifier.weight(1f))
        }
        Divider()
    }

    Spacer(Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text("सामान का कुल रू० ", fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(16.dp))
        Text("₹${subTotal.toInt()}", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InvoiceOutPutUI(invoice: Invoice) {

    val devanagariFont = FontFamily(
        Font(
            resId = R.font.devanagari_regular,
            weight = FontWeight.Normal
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "मौर्या टेन्ट हाउस",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = devanagariFont,
            color = Color(0xFF25343F)
        )
        Text(
            "लोहिंदा चौराहा,तेजीबाजार रोड,महराजगंज,जौनपुर ऊ० प्र०",
            fontFamily = devanagariFont,
            color = Color(0xFF1B1C1E),
            fontSize = 14.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                "प्रो० अमर बहादुर मौर्य",
                fontFamily = devanagariFont,
                color = Color(0xFFE62727),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "मो० 9415807288",
                fontFamily = devanagariFont,
                color = Color(0xFFE62727),
                fontSize = 14.sp,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            thickness = 1.dp,
            color = Color(0xFFCDDC39)
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFFFF9800)
        )
        Text(
            "नोट - हमारे यहाँ शादी व विवाह किसी भी शुभ अवसर पर टेन्ट सम्बन्धित ऑर्डर लिया जाता है।",
            fontFamily = devanagariFont,
            color = Color(0xFF000000),
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 6.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                "दिनांक:",
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Text(
                text = invoice.date ?: ".............",
                modifier = Modifier.padding(start = 6.dp),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "नाम श्री:",
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Text(
                text = invoice.customerName ?: ".....................",
                modifier = Modifier.padding(start = 6.dp),
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "मो०:",
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Text(
                text = invoice.customerPhone ?: ".............",
                modifier = Modifier.padding(start = 6.dp),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        ) {
            Text(
                "पता:",
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Text(
                text = invoice.customerAddress ?: "............................................",
                modifier = Modifier.padding(start = 6.dp),
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "प्रोग्राम का नाम:",
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
                fontStyle = FontStyle.Normal
            )
            Text(
                text = invoice.customerEvent ?: "...................",
                modifier = Modifier.padding(start = 10.dp),
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            thickness = 1.dp,
            color = Color(0xFF000000)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                "क्र०स०",
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "समान",
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "पीस",
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "रू०",
                modifier = Modifier.padding(end = 16.dp),
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
        }

        invoice.items.forEachIndexed { index, it ->
            ItemsUi(it, index)
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            thickness = 1.dp,
            color = Color(0xFF000000)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "कुल रू०",
                modifier = Modifier.padding(6.dp),
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Text(
                text = invoice.subTotal.toInt().toString() ?: "-----------",
                modifier = Modifier.padding(6.dp),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFF000000)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            text = """ नोट-
1. टेंट के किसी भी सामान में तोड़-फोड़ व खो जाने पर इसकी जिम्मेदारी स्वयं ग्राहक की होगी।
2. सामान नुकसान होने व खो जाने पर उसका बिल भी स्वयं भुगतान करना होगा।
3. कृपया पर्ची में दिए गए सभी सामान व सामग्री को जाँच एवं चेक ज़रूर कर लें, अन्यथा आप स्वयं जिम्मेदार होंगे।
4. कृपया कार्यक्रम में आपके अनुसार जो सामग्री पर्ची में नोट है, वही उपलब्ध कराई गई है।

धन्यवाद 
हस्ताक्षर व मोहर
""".trimIndent(),
            fontFamily = devanagariFont,
            fontSize = 9.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Start
        )

    }
}

@Composable
fun ItemsUi(invoiceItem: InvoiceItem, index: Int) {

    val devanagariFont = FontFamily(
        Font(
            resId = R.font.devanagari_regular,
            weight = FontWeight.Normal
        )
    )

    Column {
        // ✅ Hide divider for first item

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            thickness = 1.dp,
            color = DividerDefaults.color
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text(
                text = invoiceItem.serial.toString(),
                modifier = Modifier.padding(start = 12.dp),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = invoiceItem.description,
                modifier = Modifier.padding(start = 12.dp),
                fontFamily = devanagariFont,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = invoiceItem.qty.toString(),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = invoiceItem.total.toInt().toString(),
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF000000),
                fontSize = 14.sp,
            )
        }
    }
}

object InvoiceHolder {
    var currentInvoice: Invoice? = null
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
//    InvoiceScreen()
//    val invoice = InvoiceItem(1, "test", 1, 1.0)
//    ItemsUi(invoice)
}
