package com.mansur.walawili.ui.screens.trip

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mansur.walawili.ui.screens.trip.sheet.PlacesSheetState
import com.mansur.walawili.ui.screens.trip.sheet.PurposeSheetState
import com.mansur.walawili.ui.screens.trip.sheet.TransportSheetState
import com.mansur.walawili.ui.screens.trip.sheet.TravelersSheetState

sealed class Screen {
    object TripPlanning : Screen()
    object Destination : Screen()
    object DatePicker : Screen()
    object Itinerary : Screen()
}

data class TripDetails(
    val dateRange: String = "",
    val destination: String = "",
    val transportation: String = "",
    val travelers: String = "",
    val purpose: String = "",
    val placeTypes: String = ""
)

class TripPlanningViewModel : ViewModel() {

    private val _currentScreen = mutableStateOf<Screen>(Screen.TripPlanning)
    val currentScreen = _currentScreen

    private val _tripDetails = mutableStateOf(TripDetails())
    val tripDetails = _tripDetails

    // --- Sheets ---
    val transportSheet = TransportSheetState()
    val travelersSheet = TravelersSheetState()
    val purposeSheet = PurposeSheetState()
    val placesSheet = PlacesSheetState()

    // --- Navigation ---
    fun navigateTo(screen: Screen) { _currentScreen.value = screen }
    fun navigateBack() { _currentScreen.value = Screen.TripPlanning }

    // --- Trip field updates ---
    fun updateDateRange(value: String) { _tripDetails.value = _tripDetails.value.copy(dateRange = value) }
    fun updateDestination(value: String) { _tripDetails.value = _tripDetails.value.copy(destination = value) }

    fun confirmTransport() = transportSheet.confirm { _tripDetails.value = _tripDetails.value.copy(transportation = it) }
    fun confirmTravelers() = travelersSheet.confirm { _tripDetails.value = _tripDetails.value.copy(travelers = it) }
    fun confirmPurpose() = purposeSheet.confirm { _tripDetails.value = _tripDetails.value.copy(purpose = it) }
    fun confirmPlaces() = placesSheet.confirm { _tripDetails.value = _tripDetails.value.copy(placeTypes = it) }

    fun createItinerary() {
        _currentScreen.value = Screen.Itinerary
    }
}
