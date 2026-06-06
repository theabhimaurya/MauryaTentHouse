package com.live.mauryatenthouse.ui.screens

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.live.mauryatenthouse.R
import com.live.mauryatenthouse.domain.model.Invoice
import com.live.mauryatenthouse.domain.model.InvoiceItem
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme
import com.live.mauryatenthouse.ui.viewmodel.InvoiceViewModel
import com.live.mauryatenthouse.utils.showToast
import java.util.Calendar

private val DarkGreen = Color(0xFF2E7D32)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(viewModel: InvoiceViewModel = viewModel(), navController: NavController) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val devanagariFont = FontFamily(Font(resId = R.font.devanagari_regular, weight = FontWeight.Normal))

    var showDatePicker by remember { mutableStateOf(false) }

    Log.d("viewModel", "InvoiceScreen: ${viewModel.editingInvoiceId}")

    Scaffold(
        containerColor = OffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (viewModel.editingInvoiceId == null) "New Invoice / नया बिल" else "Update Invoice / बिल बदलें",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Maroon,
                        fontFamily = devanagariFont
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Maroon)
                    }
                },
                actions = {
                    OutlinedButton(
                        onClick = { /* Change language */ },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("EN/हिं", fontSize = 10.sp, color = Color.DarkGray, lineHeight = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OffWhite)
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Discount Section
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFF3F3))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "DISCOUNT / छूट",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontFamily = devanagariFont
                        )
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .border(1.dp, Maroon, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("- ₹", fontSize = 14.sp, color = Maroon)
                                BasicTextField(
                                    value = viewModel.discount,
                                    onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.discount = it },
                                    modifier = Modifier.width(60.dp),
                                    textStyle = LocalTextStyle.current.copy(
                                        textAlign = TextAlign.End,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Maroon
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        if (viewModel.discount.isEmpty()) {
                                            Text(
                                                "0",
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.End,
                                                style = LocalTextStyle.current.copy(
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Maroon
                                                )
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Column {
                            Text("Grand Total /", fontSize = 12.sp, color = Color.Gray, fontFamily = devanagariFont)
                            Text("कुल राशि", fontSize = 12.sp, color = Color.Gray, fontFamily = devanagariFont)
                            Text("₹${viewModel.getGrandTotal().toInt()}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Maroon)
                        }
                        Button(
                            onClick = {
                                if (viewModel.items.isEmpty()) {
                                    showToast(context, "कृपया पहले समान जोड़ें")
                                    return@Button
                                }
                                val invoice = Invoice(
                                    customerEvent = viewModel.customerEvent,
                                    customerName = viewModel.customerName,
                                    customerAddress = viewModel.customerAddress,
                                    customerPhone = viewModel.customerPhone,
                                    invoiceNo = "INV-${System.currentTimeMillis().toString().takeLast(6)}",
                                    date = viewModel.dueDate,
                                    items = viewModel.items.toList(),
                                    laborWages = viewModel.laborWages.toDoubleOrNull() ?: 0.0,
                                    transportFreight = viewModel.transportFreight.toDoubleOrNull() ?: 0.0,
                                    discount = viewModel.discount.toDoubleOrNull() ?: 0.0
                                )
                                InvoiceHolder.currentInvoice = invoice
                                navController.navigate(Routes.invoicePreview(false))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .fillMaxWidth(0.7f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Generate & Preview / बिल\nबनाएं और देखें", textAlign = TextAlign.Center, fontSize = 14.sp, fontFamily = devanagariFont)
                                Spacer(Modifier.width(8.dp))
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(16.dp))

            // 1. Event Date
            CollapsibleSection(title = "1. Event Date / कार्यक्रम की तिथि") {
                OutlinedTextField(
                    value = viewModel.dueDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Date / तिथि", fontFamily = devanagariFont) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date", tint = Maroon)
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            // 2. Customer Details
            CollapsibleSection(title = "2. Customer Details / ग्राहक विवरण") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFEBEE)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(12.dp), tint = Maroon)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("Name / नाम", fontSize = 12.sp, color = Color.Gray)
                        EditableText(
                            value = viewModel.customerName,
                            onValueChange = { viewModel.customerName = it },
                            placeholder = "Enter Name",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Phone / फ़ोन", fontSize = 12.sp, color = Color.Gray)
                        EditableText(
                            value = viewModel.customerPhone,
                            onValueChange = { viewModel.customerPhone = it },
                            placeholder = "+91 00000 00000",
                            keyboardType = KeyboardType.Phone
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("ADDRESS / पता", fontSize = 12.sp, color = Color.Gray, fontFamily = devanagariFont)
                        EditableText(
                            value = viewModel.customerAddress,
                            onValueChange = { viewModel.customerAddress = it },
                            placeholder = "Address",
                            fontFamily = devanagariFont
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 3. Event Name
            CollapsibleSection(title = "3. Event Name / कार्यक्रम") {
                EventDropdown(
                    selectedEvent = viewModel.customerEvent,
                    onEventSelected = { viewModel.customerEvent = it },
                    fontFamily = devanagariFont
                )
            }

            Spacer(Modifier.height(16.dp))

            // 4. Item Adder
            CollapsibleSection(title = "4. Item Adder / सामान सूची") {
                ItemAdderUI(
                    onAddItem = { desc, qty, price -> viewModel.addItem(desc, qty, price) },
                    fontFamily = devanagariFont
                )
            }

            Spacer(Modifier.height(16.dp))

            // 5. Wages/Freight
            CollapsibleSection(title = "5. Wages/Freight || मजदूरी / भाड़ा") {
                OutlinedTextField(
                    value = viewModel.laborWages,
                    onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.laborWages = it },
                    label = { Text("Labor Wages / मजदूरी राशि खर्च", fontFamily = devanagariFont) },
                    prefix = { Text("₹") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = viewModel.transportFreight,
                    onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.transportFreight = it },
                    label = { Text("Transport Freight / गाड़ी भाड़ा खर्च", fontFamily = devanagariFont) },
                    prefix = { Text("₹") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))

            // Items List / Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF3F3))
                    .border(1.dp, Color(0xFFFFCDD2), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.items.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Items added will appear here / जोड़े गए सामान यहाँ दिखेंगे",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            fontFamily = devanagariFont
                        )
                    }
                } else {
                    Column {
                        viewModel.items.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${index + 1}. ${item.description}", modifier = Modifier.weight(1f))
                                Text("${item.unitPrice.toInt()}", modifier = Modifier.padding(horizontal = 8.dp))
                                Text("x${item.qty}")
                                Text("₹${item.total.toInt()}", modifier = Modifier.padding(start = 20.dp))
                                IconButton(onClick = { viewModel.removeItem(index) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = Maroon, modifier = Modifier.size(16.dp))
                                }
                            }
                            if (index < viewModel.items.size - 1) HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                viewModel.dueDate = "%02d/%02d/%d".format(day, month + 1, year)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener { showDatePicker = false }
            show()
        }
    }
}

@Composable
fun CollapsibleSection(
    title: String,
    content: @Composable () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.DarkGray)
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
fun EditableText(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    fontWeight: FontWeight = FontWeight.Normal,
    fontFamily: FontFamily? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var isEditing by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isEditing = true }
    ) {
        if (isEditing) {
            val textStyle = LocalTextStyle.current.copy(
                fontWeight = fontWeight,
                fontFamily = fontFamily ?: FontFamily.Default
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .focusRequester(focusRequester),
                textStyle = textStyle,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.LightGray,
                            style = textStyle
                        )
                    }
                    innerTextField()
                }
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            DisposableEffect(Unit) {
                onDispose { isEditing = false }
            }
        } else {
            Text(
                text = value.ifEmpty { placeholder },
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                color = if (value.isEmpty()) Color.LightGray else Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDropdown(
    selectedEvent: String,
    onEventSelected: (String) -> Unit,
    fontFamily: FontFamily
) {
    var expanded by remember { mutableStateOf(false) }
    val events = listOf(

        "Wedding Programs / शादी कार्यक्रम",
        "Engagement / सगाई",
        "Tilak / तिलक",
        "Haldi / हल्दी",
        "Mehndi / मेहंदी",
        "Sangeet / संगीत",
        "Baraat / बारात",
        "Jaimala / जयमाल",
        "Marriage / विवाह",
        "Reception / रिसेप्शन",
        "Vidaai / विदाई",

        "Religious Programs / धार्मिक कार्यक्रम",
        "Satyanarayan Katha / सत्यनारायण कथा",
        "Havan Yagya / हवन / यज्ञ",
        "Bhagwat Katha / भागवत कथा",
        "Ram Katha / राम कथा",
        "Kirtan / कीर्तन",
        "Jagran / जागरण",
        "Puja Path / पूजा पाठ",

        "Condolence Programs / शोक कार्यक्रम",
        "Shok Sabha / शोक सभा",
        "Terahvi / तेरहवीं",
        "Brahmbhoj / ब्रह्मभोज",
        "Barsi / बरसी",
        "Shradhanjali Sabha / श्रद्धांजलि सभा",

        "Social Programs / सामाजिक कार्यक्रम",
        "Birthday / जन्मदिन",
        "Naamkaran / नामकरण",
        "Mundan / मुंडन",
        "Chhath Puja / छठ पूजा",
        "Durga Puja / दुर्गा पूजा",
        "Ganesh Puja / गणेश पूजा",

        "Business Programs / व्यावसायिक कार्यक्रम",
        "Shop Opening / दुकान उद्घाटन",
        "Foundation Ceremony / शिलान्यास",
        "Meeting / मीटिंग",
        "Seminar / सेमिनार",
        "Conference / कॉन्फ्रेंस",
        "Product Launch / प्रोडक्ट लॉन्च"

    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedEvent,
            onValueChange = {},
            readOnly = true,
            label = { Text("Event Type / कार्यक्रम का नाम", fontFamily = fontFamily) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            events.forEach { event ->
                DropdownMenuItem(
                    text = { Text(event, fontFamily = fontFamily) },
                    onClick = {
                        onEventSelected(event)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAdderUI(
    onAddItem: (String, Int, Double) -> Unit,
    fontFamily: FontFamily
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var showCustomItemDialog by remember { mutableStateOf(false) }
    var customItemName by remember { mutableStateOf("") }

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
        "वेटर",
        "अन्य"
    )

    if (showCustomItemDialog) {
        AlertDialog(
            onDismissRequest = { showCustomItemDialog = false },
            title = { Text("Add Custom Item / सामान जोड़ें", fontFamily = fontFamily, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = customItemName,
                    onValueChange = { customItemName = it },
                    label = { Text("Enter Item Name / सामान का नाम लिखें", fontFamily = fontFamily) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Maroon,
                        unfocusedBorderColor = Color.Gray
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customItemName.isNotBlank()) {
                            selectedItem = customItemName
                            customItemName = ""
                            showCustomItemDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon)
                ) {
                    Text("ADD")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomItemDialog = false }) {
                    Text("CANCEL", color = Color.Gray)
                }
            }
        )
    }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                label = { Text("Item / सामान", fontFamily = fontFamily) },
                placeholder = { Text("Select Item / सामान चुनें", fontFamily = fontFamily) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                itemList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, fontFamily = fontFamily) },
                        onClick = {
                            if (item == "अन्य") {
                                showCustomItemDialog = true
                            } else {
                                selectedItem = item
                            }
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = qty,
                onValueChange = { if (it.all { char -> char.isDigit() }) qty = it },
                label = { Text("Quantity/पीस", fontFamily = fontFamily) },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.width(12.dp))
            OutlinedTextField(
                value = price,
                onValueChange = { if (it.all { char -> char.isDigit() }) price = it },
                label = { Text("Rate / दर", fontFamily = fontFamily) },
                prefix = { Text("₹") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val q = qty.toIntOrNull() ?: 0
                val p = price.toDoubleOrNull() ?: 0.0
                if (selectedItem.isNotEmpty() && q > 0 && p > 0) {
                    onAddItem(selectedItem, q, p)
                    selectedItem = ""
                    qty = ""
                    price = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add Item / सामान जोड़ें", fontFamily = fontFamily)
        }
    }
}

object InvoiceHolder {
    var currentInvoice: Invoice? = null
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InvoiceScreenPreview() {
    MauryaTentHouseTheme {
        InvoiceScreen(navController = rememberNavController())
    }
}
