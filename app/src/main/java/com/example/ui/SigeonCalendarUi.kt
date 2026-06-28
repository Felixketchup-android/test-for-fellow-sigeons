package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.Event
import com.example.ui.theme.LollipopBackground
import com.example.ui.theme.LollipopPink
import com.example.ui.theme.LollipopPinkDark
import com.example.ui.theme.LollipopSurface
import com.example.ui.theme.LollipopTeal
import com.example.ui.theme.LollipopTealDark
import com.example.ui.theme.LollipopTealLight
import com.example.ui.theme.LollipopTextPrimary
import com.example.ui.theme.LollipopTextSecondary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Beautiful Material 1.0 Retro Palette
val LollipopEventColors = listOf(
    "#E51C23" to "Material Red",
    "#009688" to "Lollipop Teal",
    "#5677FC" to "Material Blue",
    "#259B24" to "Material Green",
    "#9C27B0" to "Material Purple",
    "#FF9800" to "Material Orange",
    "#00BCD4" to "Cyan Blue",
    "#9E9E9E" to "Steel Grey"
)

@Composable
fun SigeonCalendarUi(viewModel: EventViewModel) {
    val calendar by viewModel.calendarState.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val allEvents by viewModel.allEvents.collectAsState()
    val dailyEvents by viewModel.selectedDateEvents.collectAsState()

    // Formatted date string for the hero banner
    val parsedSelectedDate = remember(selectedDate) {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            parser.parse(selectedDate) ?: Calendar.getInstance().time
        } catch (e: Exception) {
            Calendar.getInstance().time
        }
    }
    
    val heroDayOfWeek = remember(parsedSelectedDate) {
        SimpleDateFormat("EEEE", Locale.US).format(parsedSelectedDate).uppercase()
    }
    
    val heroDateLabel = remember(parsedSelectedDate) {
        SimpleDateFormat("MMMM d, yyyy", Locale.US).format(parsedSelectedDate)
    }

    val monthName = remember(calendar) {
        SimpleDateFormat("MMMM yyyy", Locale.US).format(calendar.time).uppercase()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Android 5.0 flat solid Teal App Bar
            Column {
                // Status Bar spacer to fit edge-to-edge beautifully
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(LollipopTealDark)
                )
                // App Bar Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(LollipopTeal)
                        .shadow(4.dp, shape = RectangleShape)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Sigeon Calendar Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Sigeon Calendar",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddEventDialog() },
                containerColor = LollipopPink,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 12.dp),
                shape = CircleShape,
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("add_event_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Calendar Event",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        containerColor = LollipopBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen Width restriction for large screen tablet adaptation
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
                    .widthIn(max = 600.dp)
            ) {
                // HERO DATE HEADER (Extremely 5.0 Lollipop DatePicker header style!)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LollipopTealDark)
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = heroDayOfWeek,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = heroDateLabel,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 0.5.sp
                    )
                }

                // CALENDAR CARD GRID SHEET
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .shadow(2.dp, shape = RoundedCornerShape(4.dp)),
                    colors = CardDefaults.cardColors(containerColor = LollipopSurface),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        // Month Selector Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.prevMonth() },
                                modifier = Modifier.testTag("prev_month_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Previous Month",
                                    tint = LollipopTeal
                                )
                            }
                            
                            Text(
                                text = monthName,
                                color = LollipopTealDark,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                modifier = Modifier.testTag("month_title_text")
                            )

                            IconButton(
                                onClick = { viewModel.nextMonth() },
                                modifier = Modifier.testTag("next_month_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Next Month",
                                    tint = LollipopTeal
                                )
                            }
                        }

                        // Days of week row: S M T W T F S
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
                            daysOfWeek.forEach { dayName ->
                                Text(
                                    text = dayName,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    color = LollipopTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // 7 Column Calendar Days Grid
                        val gridDays = remember(calendar) { CalendarGridHelper.generateGridDays(calendar) }
                        
                        // We have exactly 42 cells (6 rows of 7 days)
                        for (row in 0 until 6) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (col in 0 until 7) {
                                    val index = row * 7 + col
                                    val day = gridDays[index]
                                    val isSelected = (day.dateString == selectedDate)
                                    
                                    // Count events for this date
                                    val hasEvents = remember(allEvents, day.dateString) {
                                        allEvents.any { it.date == day.dateString }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isSelected -> LollipopTeal
                                                    day.isToday -> LollipopTealLight
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .clickable {
                                                viewModel.selectDate(day.dateString)
                                            }
                                            .testTag("selected_day_indicator_${day.dateString}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = day.dayNumber.toString(),
                                                color = when {
                                                    isSelected -> Color.White
                                                    day.isToday -> LollipopTealDark
                                                    !day.isCurrentMonth -> LollipopTextSecondary.copy(alpha = 0.4f)
                                                    else -> LollipopTextPrimary
                                                },
                                                fontSize = 14.sp,
                                                fontWeight = if (day.isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                            
                                            // Event Indicator Dot
                                            if (hasEvents) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected) Color.White else LollipopPink)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // AGENDA / EVENTS SECTION HEADER
                Text(
                    text = "AGENDA FOR TODAY",
                    color = LollipopTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                // AGENDA LIST
                if (dailyEvents.isEmpty()) {
                    // Empty state in Lollipop era: neat centered card with no events message
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .shadow(1.dp, shape = RoundedCornerShape(4.dp)),
                        colors = CardDefaults.cardColors(containerColor = LollipopSurface),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "No events icon",
                                tint = LollipopTextSecondary.copy(alpha = 0.5f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No events scheduled for this day",
                                color = LollipopTextSecondary,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap the '+' button to add an event.",
                                color = LollipopTextSecondary.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(dailyEvents) { event ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(1.dp, shape = RoundedCornerShape(4.dp))
                                    .testTag("event_item_card_${event.id}"),
                                colors = CardDefaults.cardColors(containerColor = LollipopSurface),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Vertical category strip on left side
                                    Box(
                                        modifier = Modifier
                                            .width(5.dp)
                                            .height(80.dp)
                                            .background(Color(android.graphics.Color.parseColor(event.colorHex)))
                                    )
                                    
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(12.dp)
                                            .height(56.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = event.title,
                                            color = LollipopTextPrimary,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = event.time,
                                                color = LollipopTextSecondary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            
                                            if (event.description.isNotBlank()) {
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = event.description,
                                                    color = LollipopTextSecondary,
                                                    fontSize = 12.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteEvent(event) },
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(end = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Event",
                                            tint = LollipopTextSecondary.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ANDROID 5.0 STYLE RETRO MODAL DIALOG
    if (viewModel.showAddEventDialog) {
        var eventTitle by remember { mutableStateOf("") }
        var eventDescription by remember { mutableStateOf("") }
        var eventTimeHour by remember { mutableStateOf("09") }
        var eventTimeMinute by remember { mutableStateOf("00") }
        var selectedColorHex by remember { mutableStateOf(LollipopEventColors.first().first) }

        Dialog(
            onDismissRequest = { viewModel.closeAddEventDialog() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 340.dp)
                    .padding(24.dp)
                    .shadow(24.dp, shape = RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp),
                color = LollipopSurface
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Dialog Title
                    Text(
                        text = "NEW EVENT",
                        color = LollipopTealDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Title input with retro thin border styling
                    OutlinedTextField(
                        value = eventTitle,
                        onValueChange = { eventTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("event_title_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LollipopTeal,
                            focusedLabelColor = LollipopTeal,
                            unfocusedBorderColor = LollipopTextSecondary.copy(alpha = 0.4f),
                            unfocusedLabelColor = LollipopTextSecondary
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    // Description input
                    OutlinedTextField(
                        value = eventDescription,
                        onValueChange = { eventDescription = it },
                        label = { Text("Description (Optional)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("event_description_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LollipopTeal,
                            focusedLabelColor = LollipopTeal,
                            unfocusedBorderColor = LollipopTextSecondary.copy(alpha = 0.4f),
                            unfocusedLabelColor = LollipopTextSecondary
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Time inputs (retro hour / minute selectors side by side)
                    Text(
                        text = "TIME (24H)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LollipopTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = eventTimeHour,
                            onValueChange = { 
                                if (it.length <= 2 && it.all { c -> c.isDigit() }) {
                                    eventTimeHour = it
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("event_hour_input"),
                            singleLine = true,
                            textStyle = TextStyle(textAlign = TextAlign.Center),
                            placeholder = { Text("HH") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LollipopTeal,
                                unfocusedBorderColor = LollipopTextSecondary.copy(alpha = 0.4f)
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                        
                        Text(
                            text = ":",
                            color = LollipopTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        OutlinedTextField(
                            value = eventTimeMinute,
                            onValueChange = { 
                                if (it.length <= 2 && it.all { c -> c.isDigit() }) {
                                    eventTimeMinute = it
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("event_minute_input"),
                            singleLine = true,
                            textStyle = TextStyle(textAlign = TextAlign.Center),
                            placeholder = { Text("MM") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LollipopTeal,
                                unfocusedBorderColor = LollipopTextSecondary.copy(alpha = 0.4f)
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Color selection grid
                    Text(
                        text = "CHOOSE COLOR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LollipopTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        LollipopEventColors.forEach { (hex, name) ->
                            val color = Color(android.graphics.Color.parseColor(hex))
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable { selectedColorHex = hex }
                                    .shadow(elevation = if (selectedColorHex == hex) 4.dp else 0.dp, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedColorHex == hex) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected color: $name",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dialog Buttons (All Caps Text Buttons, aligned to bottom right)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { viewModel.closeAddEventDialog() },
                            colors = ButtonDefaults.textButtonColors(contentColor = LollipopTeal),
                            shape = RectangleShape,
                            modifier = Modifier.testTag("cancel_event_button")
                        ) {
                            Text(
                                text = "CANCEL",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))

                        TextButton(
                            onClick = { 
                                val validatedHour = eventTimeHour.padStart(2, '0').ifBlank { "09" }
                                val validatedMinute = eventTimeMinute.padStart(2, '0').ifBlank { "00" }
                                viewModel.addEvent(
                                    title = eventTitle,
                                    description = eventDescription,
                                    time = "$validatedHour:$validatedMinute",
                                    colorHex = selectedColorHex
                                )
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = LollipopTeal),
                            shape = RectangleShape,
                            modifier = Modifier.testTag("save_event_button")
                        ) {
                            Text(
                                text = "SAVE",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
