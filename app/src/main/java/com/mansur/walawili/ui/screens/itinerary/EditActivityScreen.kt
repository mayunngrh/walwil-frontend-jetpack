package com.mansur.walawili.ui.screens.itinerary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Primary = Color(0xFF2E2C8C)
private val BgColor = Color(0xFFF2F1EE)
private val SubtleGray = Color(0xFF9896B0)
private val FieldBg = Color.White
private val FieldBorder = Color(0xFFE8E7F5)

private val categories = listOf("Nature", "Culture", "Food", "Adventure")

@Composable
fun EditActivityScreen(
    stop: ItineraryStop,
    onSave: (ItineraryStop) -> Unit,
    onDismiss: () -> Unit
) {
    // Parse travelNext e.g. "1 h · rental car" → mode="Rental car", duration="1 h"
    val (initMode, initDuration) = remember(stop.travelNext) {
        val parts = stop.travelNext.split(" · ")
        if (parts.size == 2) Pair(parts[1].replaceFirstChar { it.uppercaseChar() }, parts[0])
        else Pair(stop.travelNext, "")
    }
    // Parse timeRange e.g. "10:30 – 12:00"
    val (initStart, initEnd) = remember(stop.timeRange) {
        val parts = stop.timeRange.split(" – ")
        if (parts.size == 2) Pair(parts[0].trim(), parts[1].trim())
        else Pair(stop.time, "")
    }

    var activityName by remember { mutableStateOf(stop.title) }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull { stop.description.contains(it, ignoreCase = true) } ?: categories[0]) }
    var location by remember { mutableStateOf(stop.location) }
    var startTime by remember { mutableStateOf(initStart) }
    var endTime by remember { mutableStateOf(initEnd) }
    var gettingThere by remember { mutableStateOf(if (stop.travelNext.isNotEmpty()) "$initMode · $initDuration" else "") }
    var notes by remember { mutableStateOf(stop.description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        // ── Top bar ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 52.dp, bottom = 14.dp, start = 16.dp, end = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFEDECF0))
                    .clickable { onDismiss() }
                    .align(Alignment.CenterStart)
            ) {
                Icon(Icons.Outlined.Close, contentDescription = "Close", tint = Color(0xFF1B1A38), modifier = Modifier.size(18.dp))
            }

            Text(
                "Edit activity",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1A38),
                modifier = Modifier.align(Alignment.Center)
            )

            Text(
                "Save",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                        val updatedTravelNext = gettingThere
                            .split(" · ")
                            .let { p -> if (p.size == 2) "${p[1]} · ${p[0]}" else gettingThere }
                        onSave(
                            stop.copy(
                                title = activityName.trim(),
                                location = location.trim(),
                                timeRange = "$startTime – $endTime",
                                time = startTime,
                                travelNext = updatedTravelNext,
                                description = notes.trim()
                            )
                        )
                    }
            )
        }

        // ── Scrollable form ──────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Photo placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEBEDF5))
            ) {
                Text(
                    stop.photoLabel,
                    fontSize = 13.sp,
                    color = SubtleGray,
                    modifier = Modifier.align(Alignment.Center)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .clickable { }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Outlined.CameraAlt, null, tint = Primary, modifier = Modifier.size(16.dp))
                        Text("Change", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                    }
                }
            }

            // Activity name
            FormSection(label = "Activity name") {
                EditField(
                    value = activityName,
                    onValueChange = { activityName = it },
                    placeholder = "Activity name"
                )
            }

            // Category chips
            FormSection(label = "Category") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(if (isSelected) Primary else Color.White)
                                .border(1.dp, if (isSelected) Primary else FieldBorder, RoundedCornerShape(50.dp))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 16.dp, vertical = 9.dp)
                        ) {
                            Text(
                                cat,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else Color(0xFF1B1A38)
                            )
                        }
                    }
                }
            }

            // Location
            FormSection(label = "Location") {
                EditFieldWithIcon(
                    value = location,
                    onValueChange = { location = it },
                    icon = Icons.Outlined.Room,
                    placeholder = "Location"
                )
            }

            // Starts / Ends
            FormSection(label = null) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Starts", fontSize = 13.sp, color = SubtleGray, fontWeight = FontWeight.Normal)
                        EditFieldWithIcon(
                            value = startTime,
                            onValueChange = { startTime = it },
                            icon = Icons.Outlined.Schedule,
                            placeholder = "10:30"
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Ends", fontSize = 13.sp, color = SubtleGray, fontWeight = FontWeight.Normal)
                        EditFieldWithIcon(
                            value = endTime,
                            onValueChange = { endTime = it },
                            icon = Icons.Outlined.Schedule,
                            placeholder = "12:00"
                        )
                    }
                }
            }

            // Getting there
            FormSection(label = "Getting there") {
                EditFieldWithIcon(
                    value = gettingThere,
                    onValueChange = { gettingThere = it },
                    icon = Icons.Outlined.SwapCalls,
                    placeholder = "e.g. Rental car · 1 h"
                )
            }

            // Notes
            FormSection(label = "Notes") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(FieldBg)
                        .border(1.dp, FieldBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                ) {
                    BasicTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
                        textStyle = TextStyle(fontSize = 15.sp, color = Color(0xFF1B1A38), lineHeight = 22.sp),
                        cursorBrush = SolidColor(Primary),
                        decorationBox = { inner ->
                            if (notes.isEmpty()) Text("Add notes...", fontSize = 15.sp, color = SubtleGray)
                            inner()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }

        // ── Save changes button ──────────────────────────────────
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE0DFF0))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgColor)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = {
                    val updatedTravelNext = gettingThere
                        .split(" · ")
                        .let { p -> if (p.size == 2) "${p[1]} · ${p[0]}" else gettingThere }
                    onSave(
                        stop.copy(
                            title = activityName.trim(),
                            location = location.trim(),
                            timeRange = "$startTime – $endTime",
                            time = startTime,
                            travelNext = updatedTravelNext,
                            description = notes.trim()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Save changes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun FormSection(label: String?, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (label != null) {
            Text(label, fontSize = 13.sp, color = SubtleGray, fontWeight = FontWeight.Normal)
        }
        content()
    }
}

@Composable
private fun EditField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FieldBg)
            .border(1.dp, FieldBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 15.sp, color = Color(0xFF1B1A38), fontWeight = FontWeight.SemiBold),
            singleLine = true,
            cursorBrush = SolidColor(Primary),
            decorationBox = { inner ->
                if (value.isEmpty()) Text(placeholder, fontSize = 15.sp, color = SubtleGray)
                inner()
            }
        )
    }
}

@Composable
private fun EditFieldWithIcon(value: String, onValueChange: (String) -> Unit, icon: ImageVector, placeholder: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FieldBg)
            .border(1.dp, FieldBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, null, tint = SubtleGray, modifier = Modifier.size(16.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(fontSize = 15.sp, color = Color(0xFF1B1A38)),
            singleLine = true,
            cursorBrush = SolidColor(Primary),
            decorationBox = { inner ->
                if (value.isEmpty()) Text(placeholder, fontSize = 15.sp, color = SubtleGray)
                inner()
            }
        )
    }
}
