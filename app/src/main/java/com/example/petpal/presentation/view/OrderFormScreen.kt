package com.example.petpal.presentation.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.petpal.R
import com.example.petpal.data.model.Pet
import com.example.petpal.presentation.component.OrderFormField
import com.example.petpal.presentation.theme.BlackText
import com.example.petpal.presentation.theme.GrayText
import com.example.petpal.presentation.theme.PetPalDarkGreen
import com.example.petpal.presentation.theme.White
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFormScreen(
    serviceType: String,
    onNavigateBack: () -> Unit,
    selectedTierFromNav: String? = null,
    selectedPetsFromNav: List<Pet>? = null,
    selectedBranchFromNav: String? = null,
    startTimeFromNav: String? = null,
    startDateFromNav: String? = null,
    endTimeFromNav: String? = null,
    endDateFromNav: String? = null,
    onNavigateToPetSelection: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onNavigateToTierSelection: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onNavigateToBranchSelection: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onSubmitOrder: (List<Pet>, String, String, String, String, String, String, String, String, Int, Double, Double) -> Unit = { _, _, _, _, _, _, _, _, _, _, _, _ -> }
) {
    val context = LocalContext.current

    // Get current date for Daycare service
    val currentCalendar = Calendar.getInstance()
    val currentDateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentCalendar.time)

    // Form state
    var selectedPets by remember { mutableStateOf<List<Pet>>(selectedPetsFromNav ?: emptyList()) }
    var selectedPetNames by remember {
        mutableStateOf<String?>(
            selectedPetsFromNav?.joinToString { pet: Pet -> pet.name }
        )
    }
    var startTime by remember { mutableStateOf(startTimeFromNav ?: "00:00") }
    var startDate by remember { mutableStateOf(startDateFromNav ?: if (serviceType == "Daycare") currentDateString else "00/00/0000") }
    var endTime by remember { mutableStateOf(endTimeFromNav ?: "00:00") }
    var endDate by remember { mutableStateOf(endDateFromNav ?: if (serviceType == "Daycare") currentDateString else "00/00/0000") }
    var selectedTier by remember { mutableStateOf<String?>(selectedTierFromNav) }
    var selectedBranch by remember { mutableStateOf<String?>(selectedBranchFromNav) }
    var note by remember { mutableStateOf("") }
    var isPriceExpanded by remember { mutableStateOf(false) }

    // Update selectedPets when selectedPetsFromNav changes
    LaunchedEffect(selectedPetsFromNav) {
        selectedPetsFromNav?.let {
            selectedPets = it
            selectedPetNames = it.joinToString { pet -> pet.name }
        }
    }

    // Update selectedPets when selectedPetsFromNav changes
    LaunchedEffect(selectedPetsFromNav) {
        selectedPetsFromNav?.let {
            selectedPets = it
            selectedPetNames = it.joinToString { pet -> pet.name }
        }
    }

    // Dialog states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // Calculate total price
    val tierPrices = mapOf(
        "Regular" to 50000.0,
        "VIP" to 75000.0,
        "VVIP" to 100000.0
    )

    // Calculate duration from start and end times/dates
    val durationHours = remember(startTime, startDate, endTime, endDate) {
        try {
            if (serviceType == "Boarding") {
                // For Boarding: Calculate days between dates
                if (startDate != "00/00/0000" && endDate != "00/00/0000") {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    val startDateTime = dateFormat.parse("$startDate $startTime")
                    val endDateTime = dateFormat.parse("$endDate $endTime")

                    if (startDateTime != null && endDateTime != null) {
                        val diffInMillis = endDateTime.time - startDateTime.time
                        val diffInHours = diffInMillis / (1000 * 60 * 60)
                        maxOf(diffInHours.toInt(), 0)
                    } else 0
                } else 0
            } else {
                // For Daycare: Calculate hours between times on the same day
                if (startTime != "00:00" && endTime != "00:00") {
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val startTimeParsed = timeFormat.parse(startTime)
                    val endTimeParsed = timeFormat.parse(endTime)

                    if (startTimeParsed != null && endTimeParsed != null) {
                        val diffInMillis = endTimeParsed.time - startTimeParsed.time
                        val diffInHours = diffInMillis / (1000 * 60 * 60)
                        maxOf(diffInHours.toInt(), 0)
                    } else 0
                } else 0
            }
        } catch (e: Exception) {
            0
        }
    }

    val tierPrice = selectedTier?.let { tierPrices[it] } ?: 0.0
    val totalPrice = durationHours * tierPrice

    // Check if form is complete
    val isFormComplete = selectedPetNames != null &&
                         selectedTier != null &&
                         selectedBranch != null &&
                         (if (serviceType == "Boarding") {
                             startDate != "00/00/0000" && endDate != "00/00/0000"
                         } else {
                             (startTime != "00:00" || endTime != "00:00") && startDate != "00/00/0000"
                         })

    val scrollState = rememberScrollState()
    val iconRotation by animateFloatAsState(
        targetValue = if (isPriceExpanded) 0f else 180f,
        animationSpec = tween(durationMillis = 300),
        label = "icon rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_arrowleft_foreground),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Buat Pesanan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            // Pet Selection
            OrderFormField(
                iconRes = R.drawable.icon_petoption_foreground,
                label = "Hewan Peliharaan",
                value = selectedPetNames ?: "Pilih hewan peliharaanmu",
                onClick = { onNavigateToPetSelection(startTime, startDate, endTime, endDate) },
                isPlaceholder = selectedPetNames == null,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Duration Section
            Text(
                text = "Durasi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Start Duration
            DurationField(
                label = "Dari :",
                time = startTime,
                date = startDate,
                onClick = {
                    if (serviceType == "Boarding") {
                        showStartDatePicker = true
                    } else {
                        showStartTimePicker = true
                    }
                },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // End Duration
            DurationField(
                label = "Sampai :",
                time = endTime,
                date = endDate,
                onClick = {
                    if (serviceType == "Boarding") {
                        showEndDatePicker = true
                    } else {
                        showEndTimePicker = true
                    }
                },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Service Tier
            OrderFormField(
                iconRes = R.drawable.icon_tier_foreground,
                label = "Tingkat Layanan",
                value = selectedTier ?: "Tingkat Layanan",
                onClick = { onNavigateToTierSelection(startTime, startDate, endTime, endDate) },
                isPlaceholder = selectedTier == null,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Branch
            OrderFormField(
                iconRes = R.drawable.icon_branch_foreground,
                label = "Cabang",
                value = selectedBranch ?: "Cabang",
                onClick = { onNavigateToBranchSelection(startTime, startDate, endTime, endDate) },
                isPlaceholder = selectedBranch == null,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Notes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Catatan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_note_foreground),
                                contentDescription = "Note icon",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Ketik catatanmu di sini",
                                color = GrayText,
                                fontSize = 14.sp
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = GrayText.copy(alpha = 0.3f),
                        focusedBorderColor = PetPalDarkGreen,
                        unfocusedContainerColor = GrayText.copy(alpha = 0.1f),
                        focusedContainerColor = GrayText.copy(alpha = 0.1f),
                        unfocusedTextColor = BlackText,
                        focusedTextColor = BlackText
                    )
                )
            }
        }

        // Date/Time Pickers
        if (showStartDatePicker) {
            DateTimePickerDialog(
                onDismiss = { showStartDatePicker = false },
                onDateTimeSelected = { time, date ->
                    startTime = time
                    startDate = date
                    showStartDatePicker = false
                },
                isStart = true,
                currentStartTime = startTime,
                currentStartDate = startDate,
                context = context
            )
        }

        if (showStartTimePicker) {
            TimePickerDialog(
                onDismiss = { showStartTimePicker = false },
                onTimeSelected = { time ->
                    startTime = time
                    showStartTimePicker = false
                },
                isStart = true,
                currentStartTime = startTime,
                context = context
            )
        }

        if (showEndDatePicker) {
            DateTimePickerDialog(
                onDismiss = { showEndDatePicker = false },
                onDateTimeSelected = { time, date ->
                    endTime = time
                    endDate = date
                    showEndDatePicker = false
                },
                isStart = false,
                currentStartTime = startTime,
                currentStartDate = startDate,
                context = context
            )
        }

        if (showEndTimePicker) {
            TimePickerDialog(
                onDismiss = { showEndTimePicker = false },
                onTimeSelected = { time ->
                    endTime = time
                    showEndTimePicker = false
                },
                isStart = false,
                currentStartTime = startTime,
                context = context
            )
        }

        // Price Section - Sticky above button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 24.dp)
        ) {
            // Total Price Header (Clickable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isPriceExpanded = !isPriceExpanded }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Harga : Rp ${formatPrice(totalPrice)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlackText,
                    modifier = Modifier.weight(1f)
                )

                Image(
                    painter = painterResource(id = R.drawable.icon_pricedetail_foreground),
                    contentDescription = "Price detail",
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(iconRotation)
                )
            }

            // Expanded Price Details
            if (isPriceExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    HorizontalDivider(
                        color = GrayText.copy(alpha = 0.3f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    PriceDetailRow(
                        label = "Durasi",
                        value = "$durationHours jam"
                    )

                    PriceDetailRow(
                        label = "Harga per jam",
                        value = "Rp ${formatPrice(tierPrice)}"
                    )

                    HorizontalDivider(
                        color = GrayText.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    PriceDetailRow(
                        label = "Total",
                        value = "Rp ${formatPrice(totalPrice)}",
                        isBold = true
                    )
                }
            }
        }

        // Bottom Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp, top = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (isFormComplete) PetPalDarkGreen else GrayText.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .clickable(enabled = isFormComplete) {
                        if (isFormComplete) {
                            onSubmitOrder(
                                selectedPets,
                                selectedPetNames ?: "",
                                startTime,
                                startDate,
                                endTime,
                                endDate,
                                selectedTier ?: "",
                                selectedBranch ?: "",
                                note,
                                durationHours,
                                tierPrice,
                                totalPrice
                            )
                        }
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_arrowright),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Lakukan Pembayaran",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

@Composable
private fun DurationField(
    label: String,
    time: String,
    date: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = GrayText.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_calendar_foreground),
            contentDescription = "Calendar",
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            fontSize = 14.sp,
            color = BlackText,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$time, $date",
            fontSize = 14.sp,
            color = if (time == "00:00" && date == "00/00/0000") GrayText else BlackText
        )
    }
}

@Composable
private fun PriceDetailRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = BlackText
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = BlackText
        )
    }
}

private fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"))
    return formatter.format(price.toLong())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimePickerDialog(
    onDismiss: () -> Unit,
    onDateTimeSelected: (String, String) -> Unit,
    isStart: Boolean,
    @Suppress("UNUSED_PARAMETER") currentStartTime: String,
    currentStartDate: String,
    context: android.content.Context
) {
    val calendar = Calendar.getInstance()

    // Parse current start date/time if available for end date validation
    if (!isStart && currentStartDate != "00/00/0000") {
        try {
            val parts = currentStartDate.split("/")
            if (parts.size == 3) {
                calendar.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
            }
        } catch (_: Exception) {
            // Keep current date
        }
    }

    val now = Calendar.getInstance()
    val todayStart = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val minDate = if (isStart) {
        // For start date, allow today (but will validate time later)
        todayStart.timeInMillis
    } else {
        // For end date, minimum is start date
        calendar.timeInMillis
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )

    var showTimePicker by remember { mutableStateOf(false) }

    if (!showTimePicker) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Validate date is not before minimum
                            if (millis >= minDate) {
                                showTimePicker = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Tidak dapat memilih tanggal yang sudah lewat",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                ) {
                    Text("Pilih Waktu")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    } else {
        val timePickerState = rememberTimePickerState(
            initialHour = 0,
            initialMinute = 0,
            is24Hour = true
        )

        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedCalendar = Calendar.getInstance().apply {
                                timeInMillis = millis
                            }

                            val dateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(selectedCalendar.time)
                            val timeString = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)

                            // Validate time if same day
                            val isSameDay = selectedCalendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                                    selectedCalendar.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)

                            if (isSameDay) {
                                val selectedHour = timePickerState.hour
                                val selectedMinute = timePickerState.minute
                                val currentHour = now.get(Calendar.HOUR_OF_DAY)
                                val currentMinute = now.get(Calendar.MINUTE)

                                if (selectedHour > currentHour || (selectedHour == currentHour && selectedMinute >= currentMinute)) {
                                    onDateTimeSelected(timeString, dateString)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Tidak dapat memilih waktu yang sudah lewat",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                onDateTimeSelected(timeString, dateString)
                            }
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Batal")
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TimePicker(state = timePickerState)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit,
    isStart: Boolean,
    currentStartTime: String,
    context: android.content.Context
) {
    val now = Calendar.getInstance()
    val currentHour = now.get(Calendar.HOUR_OF_DAY)
    val currentMinute = now.get(Calendar.MINUTE)

    // Parse start time for end time validation
    var startHour = currentHour
    var startMinute = currentMinute
    if (!isStart && currentStartTime != "00:00") {
        try {
            val parts = currentStartTime.split(":")
            if (parts.size == 2) {
                startHour = parts[0].toInt()
                startMinute = parts[1].toInt()
            }
        } catch (_: Exception) {
            // Use current time
        }
    }

    val minHour = if (isStart) currentHour else startHour
    val minMinute = if (isStart) currentMinute else startMinute

    val timePickerState = rememberTimePickerState(
        initialHour = minHour,
        initialMinute = minMinute,
        is24Hour = true
    )

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedHour = timePickerState.hour
                    val selectedMinute = timePickerState.minute

                    // Validate time
                    val isValid = if (isStart) {
                        selectedHour > minHour || (selectedHour == minHour && selectedMinute >= minMinute)
                    } else {
                        selectedHour > minHour || (selectedHour == minHour && selectedMinute > minMinute)
                    }

                    if (isValid) {
                        val timeString = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                        onTimeSelected(timeString)
                    } else {
                        val message = if (isStart) {
                            "Tidak dapat memilih waktu yang sudah lewat"
                        } else {
                            "Waktu akhir harus lebih dari waktu mulai"
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimePicker(state = timePickerState)
            }
        }
    )
}
