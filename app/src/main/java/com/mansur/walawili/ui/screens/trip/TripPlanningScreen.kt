package com.mansur.walawili.ui.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mansur.walawili.ui.components.trip.CreateItineraryButton
import com.mansur.walawili.ui.components.trip.TripFormField
import com.mansur.walawili.ui.components.trip.TripPlanningHeader

private val purposeOptions = listOf(
    "Relaxed holiday", "Honeymoon", "Adventure", "Business", "Family time", "Foodie trip", "Culture & history"
)

private val transportOptions = listOf(
    "Flight", "Rental car", "Scooter", "Train", "Taxi / Grab", "Walk", "Ferry"
)

private data class TravelerOption(val label: String, val subtitle: String)

private val travelerOptions = listOf(
    TravelerOption("Just me", "Solo trip"),
    TravelerOption("My partner", "2 travelers"),
    TravelerOption("Family", "With kids"),
    TravelerOption("Friends", "Small group"),
    TravelerOption("Tour group", "10+ people")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlanningScreen(
    viewModel: TripPlanningViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val tripDetails = viewModel.tripDetails.value
    val showSheet by viewModel.showTransportSheet
    val selected by viewModel.selectedTransport
    val showTravelersSheet by viewModel.showTravelersSheet
    val selectedTraveler by viewModel.selectedTraveler
    val showPurposeSheet by viewModel.showPurposeSheet
    val selectedPurpose by viewModel.selectedPurpose
    val customPurpose by viewModel.customPurpose

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeTransportSheet() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 4.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .background(Color(0xFFDDDCEA), RoundedCornerShape(2.dp))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "How will you get around?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B1A38)
                        )
                        Text(
                            text = "Tap all that apply.",
                            fontSize = 13.sp,
                            color = Color(0xFF9896B0),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF3DC), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "CHOOSE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB07800)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Chip grid
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    transportOptions.forEach { option ->
                        val isSelected = selected.contains(option)
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) Color(0xFF2E2C8C) else Color.Transparent,
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .border(
                                    width = 1.5.dp,
                                    color = if (isSelected) Color(0xFF2E2C8C) else Color(0xFFDDDCEA),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .clickable { viewModel.toggleTransport(option) }
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = option,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF1B1A38)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.confirmTransport() },
                    enabled = selected.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E2C8C),
                        disabledContainerColor = Color(0xFFB0AECA)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    val label = if (selected.isEmpty()) "Confirm"
                    else "Confirm · ${selected.size} selected"
                    Text(
                        text = label,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    if (showTravelersSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeTravelersSheet() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 4.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .background(Color(0xFFDDDCEA), RoundedCornerShape(2.dp))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Who's coming with you?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1A38)
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF3DC), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "CHOOSE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB07800)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                travelerOptions.forEach { option ->
                    val isSelected = selectedTraveler == option.label
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectTraveler(option.label) }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(
                                            color = if (isSelected) Color(0xFF2E2C8C) else Color(0xFFEBEDF5),
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.People,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else Color(0xFF3D3B8E),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = option.label,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1B1A38)
                                    )
                                    Text(
                                        text = option.subtitle,
                                        fontSize = 12.sp,
                                        color = Color(0xFF9896B0)
                                    )
                                }
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        color = if (isSelected) Color(0xFF2E2C8C) else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 1.5.dp,
                                        color = if (isSelected) Color(0xFF2E2C8C) else Color(0xFFDDDCEA),
                                        shape = CircleShape
                                    )
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 78.dp, end = 20.dp),
                            thickness = 0.5.dp,
                            color = Color(0xFFEDECF4)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.confirmTraveler() },
                    enabled = selectedTraveler != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E2C8C),
                        disabledContainerColor = Color(0xFFB0AECA)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Confirm",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    if (showPurposeSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closePurposeSheet() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 4.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .background(Color(0xFFDDDCEA), RoundedCornerShape(2.dp))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "What's the purpose?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B1A38)
                        )
                        Text(
                            text = "Pick one — or write your own.",
                            fontSize = 13.sp,
                            color = Color(0xFF9896B0),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF3DC), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PICK / TYPE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB07800)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    purposeOptions.forEach { option ->
                        val isSelected = selectedPurpose == option && customPurpose.isEmpty()
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) Color(0xFF2E2C8C) else Color.Transparent,
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .border(
                                    width = 1.5.dp,
                                    color = if (isSelected) Color(0xFF2E2C8C) else Color(0xFFDDDCEA),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .clickable {
                                    viewModel.selectPurpose(option)
                                    viewModel.setCustomPurpose("")
                                }
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = option,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF1B1A38)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom text input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, Color(0xFFDDDCEA), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "+",
                        fontSize = 16.sp,
                        color = Color(0xFFB0AECA),
                        fontWeight = FontWeight.Normal
                    )
                    BasicTextField(
                        value = customPurpose,
                        onValueChange = {
                            viewModel.setCustomPurpose(it)
                            if (it.isNotEmpty()) viewModel.selectPurpose("")
                        },
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = Color(0xFF1B1A38)
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(Color(0xFF2E2C8C)),
                        decorationBox = { inner ->
                            if (customPurpose.isEmpty()) {
                                Text(
                                    text = "Type your own purpose...",
                                    fontSize = 15.sp,
                                    color = Color(0xFFB0AECA)
                                )
                            }
                            inner()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                val canConfirm = selectedPurpose != null || customPurpose.isNotEmpty()
                Button(
                    onClick = { viewModel.confirmPurpose() },
                    enabled = canConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E2C8C),
                        disabledContainerColor = Color(0xFFB0AECA)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Confirm",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F1EE)),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            TripPlanningHeader(title = "New trip", onBackClick = onBackClick)
        }

        item {
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 20.dp)
            ) {
                Text(
                    text = "Let's plan your trip",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B1A38),
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Give me the brief so Walla Wili can draft a day-by-day plan you can tweak later.",
                    fontSize = 14.sp,
                    color = Color(0xFF9896B0),
                    lineHeight = 20.sp
                )
            }
        }

        item {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                tonalElevation = 0.dp
            ) {
                Column {
                    TripFormField(
                        label = "When are you going?",
                        value = tripDetails.dateRange,
                        placeholder = "",
                        icon = Icons.Outlined.CalendarMonth,
                        onClick = { viewModel.navigateTo(Screen.DatePicker) }
                    )
                    TripFormField(
                        label = "Where to?",
                        value = tripDetails.destination,
                        placeholder = "",
                        icon = Icons.Outlined.Room,
                        onClick = { viewModel.navigateTo(Screen.Destination) }
                    )
                    TripFormField(
                        label = "How will you get around?",
                        value = tripDetails.transportation,
                        placeholder = "Flight, rental car, scooter...",
                        icon = Icons.Outlined.DirectionsCar,
                        onClick = { viewModel.openTransportSheet() }
                    )
                    TripFormField(
                        label = "Who's coming with you?",
                        value = tripDetails.travelers,
                        placeholder = "",
                        icon = Icons.Outlined.People,
                        onClick = { viewModel.openTravelersSheet() }
                    )
                    TripFormField(
                        label = "What's the purpose?",
                        value = tripDetails.purpose,
                        placeholder = "Relaxed holiday, honeymoon...",
                        icon = Icons.Outlined.TrackChanges,
                        onClick = { viewModel.openPurposeSheet() }
                    )
                    TripFormField(
                        label = "What kind of places?",
                        value = tripDetails.placeTypes,
                        placeholder = "Beaches, cafés, culture...",
                        icon = Icons.Outlined.Explore,
                        isLast = true,
                        onClick = { viewModel.updatePlaceTypes("Beaches, cafés, culture...") }
                    )
                }
            }
        }

        item {
            CreateItineraryButton(
                onClick = { viewModel.createItinerary() }
            )
        }
    }
}
