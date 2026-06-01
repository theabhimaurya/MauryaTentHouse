package com.live.mauryatenthouse.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.live.mauryatenthouse.R
import com.live.mauryatenthouse.ui.navigation.Routes
import com.live.mauryatenthouse.ui.theme.MauryaTentHouseTheme
import com.live.mauryatenthouse.ui.viewmodel.InvoiceViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBookingScreen(viewModel: InvoiceViewModel = viewModel(), navController: NavController) {
    val context = LocalContext.current
    val devanagariFont = FontFamily(Font(resId = R.font.devanagari_regular, weight = FontWeight.Normal))
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = OffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = null,
                                modifier = Modifier.padding(4.dp),
                                tint = Maroon
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "NEW BOOKING",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "बुकिंग बनाएँ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Maroon,
                                fontFamily = devanagariFont
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Maroon)
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
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (viewModel.items.isEmpty()) {
                                com.live.mauryatenthouse.utils.showToast(context, "कृपया पहले सामान जोड़ें")
                                return@Button
                            }
                            val invoice = com.live.mauryatenthouse.domain.model.Invoice(
                                customerEvent = viewModel.customerEvent,
                                customerName = viewModel.customerName,
                                customerAddress = viewModel.customerAddress,
                                customerPhone = viewModel.customerPhone,
                                invoiceNo = "BKG-${System.currentTimeMillis().toString().takeLast(6)}",
                                date = viewModel.dueDate,
                                items = viewModel.items.toList(),
                                laborWages = 0.0,
                                transportFreight = 0.0,
                                discount = 0.0
                            )
                            InvoiceHolder.currentInvoice = invoice
                            navController.navigate(Routes.BOOKING_PREVIEW)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Maroon),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(10.dp)
                            .height(50.dp)
                            .fillMaxWidth()
                    ) {
                        Text("GENERATE / बुकिंग ✨", fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
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

            // 1. EVENT DATE
            BookingSection(title = "1. EVENT DATE / तिथि", icon = Icons.Default.DateRange) {
                OutlinedTextField(
                    value = viewModel.dueDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("DATE / कार्यक्रम की तिथि", fontFamily = devanagariFont, color = Maroon, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Maroon,
                        unfocusedBorderColor = Maroon,
                        focusedLabelColor = Maroon,
                        unfocusedLabelColor = Maroon
                    ),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date", tint = Maroon)
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(Modifier.height(16.dp))

            // 2. CUSTOMER
            BookingSection(title = "2. CUSTOMER / ग्राहक विवरण", icon = Icons.Default.Person) {
                Surface(
                    color = Color(0xFFFFF7F7),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFE0E0)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(10.dp), tint = Maroon)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("CUSTOMER NAME / नाम", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
                            EditableTextField(
                                value = viewModel.customerName,
                                onValueChange = { viewModel.customerName = it },
                                placeholder = "Enter name",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                
                Spacer(Modifier.height(12.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        color = Color(0xFFFFF7F7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("PHONE / मोबाइल", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
                            EditableTextField(
                                value = viewModel.customerPhone,
                                onValueChange = { viewModel.customerPhone = it },
                                placeholder = "+91 98765 43210",
                                keyboardType = KeyboardType.Phone,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Surface(
                        color = Color(0xFFFFF7F7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("ADDRESS / पता", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold, fontFamily = devanagariFont)
                            EditableTextField(
                                value = viewModel.customerAddress,
                                onValueChange = { viewModel.customerAddress = it },
                                placeholder = "Enter address",
                                fontFamily = devanagariFont,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 3. EVENT
            BookingSection(title = "3. EVENT / कार्यक्रम", icon = Icons.Default.Edit) {
                BookingEventDropdown(
                    selectedEvent = viewModel.customerEvent,
                    onEventSelected = { viewModel.customerEvent = it },
                    fontFamily = devanagariFont
                )
            }

            Spacer(Modifier.height(16.dp))

            // 4. ADD ITEMS
            BookingSection(title = "4. ADD ITEMS / सामान जोड़ें", icon = Icons.Default.ShoppingCart) {
                BookingItemAdderUI(
                    onAddItem = { desc, qty, price -> viewModel.addItem(desc, qty, price) },
                    fontFamily = devanagariFont
                )
            }

            Spacer(Modifier.height(16.dp))

            // Items List
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFFBFB))
                    .border(
                        BorderStroke(1.dp, Color(0xFFFFE0E0)),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.items.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFFEEEE)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.padding(14.dp),
                                tint = Maroon.copy(alpha = 0.4f)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "No items added yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            "जोड़े गए सामान यहाँ डिजिटल लेजर में दिखेंगे",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontFamily = devanagariFont
                        )
                    }
                } else {
                    Column {
                        viewModel.items.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${index + 1}. ${item.description}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Qty: ${item.qty}", fontSize = 12.sp, color = Color.Gray)
                                }
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
                viewModel.dueDate = "%02d / %02d / %d".format(day, month + 1, year)
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
fun BookingSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Maroon)
                Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
            }
            Spacer(Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
fun EditableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp,
    fontFamily: FontFamily? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var textValue by remember(value) { mutableStateOf(value) }
    
    BasicTextField(
        value = textValue,
        onValueChange = { 
            textValue = it
            onValueChange(it)
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = LocalTextStyle.current.copy(
            fontWeight = fontWeight,
            fontSize = fontSize,
            fontFamily = fontFamily ?: FontFamily.Default,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        decorationBox = { innerTextField ->
            if (textValue.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color.LightGray,
                    fontWeight = fontWeight,
                    fontSize = fontSize,
                    fontFamily = fontFamily ?: FontFamily.Default
                )
            }
            innerTextField()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingEventDropdown(
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
            label = { Text("EVENT TYPE / कार्यक्रम का नाम", fontFamily = fontFamily, color = Maroon, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Maroon,
                unfocusedBorderColor = Maroon,
                focusedLabelColor = Maroon,
                unfocusedLabelColor = Maroon
            ),
            textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
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
fun BookingItemAdderUI(
    onAddItem: (String, Int, Double) -> Unit,
    fontFamily: FontFamily
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }

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

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                label = { Text("SELECT ITEM / सामान चुनें", fontFamily = fontFamily, color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                placeholder = { Text("--- Select Item ---", fontFamily = fontFamily) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                itemList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, fontFamily = fontFamily) },
                        onClick = {
                            selectedItem = item
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = qty,
            onValueChange = { if (it.all { char -> char.isDigit() }) qty = it },
            label = { Text("QUANTITY / मात्रा", fontFamily = fontFamily, color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
            placeholder = { Text("Enter quantity") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val q = qty.toIntOrNull() ?: 0
                if (selectedItem.isNotEmpty() && q > 0) {
                    onAddItem(selectedItem, q, 0.0) 
                    selectedItem = ""
                    qty = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)), // Dark Green
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("ADD ITEM / सामान जोड़ें", fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateBookingScreenPreview() {
    MauryaTentHouseTheme {
        CreateBookingScreen(navController = rememberNavController())
    }
}
