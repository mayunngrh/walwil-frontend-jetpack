package com.mansur.walawili.ui.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mansur.walawili.ui.components.trip.CreateItineraryButton
import com.mansur.walawili.ui.components.trip.TripFormField
import com.mansur.walawili.ui.components.trip.TripPlanningHeader

@Composable
fun TripPlanningScreen(
    viewModel: TripPlanningViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val tripDetails = viewModel.tripDetails.value

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
                        onClick = { viewModel.updateDateRange("5 – 9 Oct 2026") }
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
                        onClick = { viewModel.updateTransportation("Flight, rental car, scooter...") }
                    )
                    TripFormField(
                        label = "Who's coming with you?",
                        value = tripDetails.travelers,
                        placeholder = "",
                        icon = Icons.Outlined.People,
                        onClick = { viewModel.updateTravelers("Me + partner") }
                    )
                    TripFormField(
                        label = "What's the purpose?",
                        value = tripDetails.purpose,
                        placeholder = "Relaxed holiday, honeymoon...",
                        icon = Icons.Outlined.TrackChanges,
                        onClick = { viewModel.updatePurpose("Relaxed holiday, honeymoon...") }
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
