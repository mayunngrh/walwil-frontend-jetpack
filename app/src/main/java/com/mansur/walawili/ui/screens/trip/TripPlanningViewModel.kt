package com.mansur.walawili.ui.screens.trip

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

sealed class Screen {
    object TripPlanning : Screen()
    object Destination : Screen()
    object DatePicker : Screen()
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
    val currentScreen: State<Screen> = _currentScreen

    private val _tripDetails = mutableStateOf(TripDetails())
    val tripDetails: State<TripDetails> = _tripDetails

    private val _showTransportSheet = mutableStateOf(false)
    val showTransportSheet: State<Boolean> = _showTransportSheet

    private val _selectedTransport = mutableStateOf<Set<String>>(emptySet())
    val selectedTransport: State<Set<String>> = _selectedTransport

    fun openTransportSheet() { _showTransportSheet.value = true }
    fun closeTransportSheet() { _showTransportSheet.value = false }

    fun toggleTransport(option: String) {
        val current = _selectedTransport.value
        _selectedTransport.value = if (current.contains(option)) current - option else current + option
    }

    fun confirmTransport() {
        val joined = _selectedTransport.value.joinToString(", ")
        _tripDetails.value = _tripDetails.value.copy(transportation = joined)
        _showTransportSheet.value = false
    }

    private val _showTravelersSheet = mutableStateOf(false)
    val showTravelersSheet: State<Boolean> = _showTravelersSheet

    private val _selectedTraveler = mutableStateOf<String?>(null)
    val selectedTraveler: State<String?> = _selectedTraveler

    fun openTravelersSheet() { _showTravelersSheet.value = true }
    fun closeTravelersSheet() { _showTravelersSheet.value = false }

    fun selectTraveler(option: String) { _selectedTraveler.value = option }

    fun confirmTraveler() {
        _selectedTraveler.value?.let {
            _tripDetails.value = _tripDetails.value.copy(travelers = it)
        }
        _showTravelersSheet.value = false
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        _currentScreen.value = Screen.TripPlanning
    }

    fun updateDateRange(date: String) {
        _tripDetails.value = _tripDetails.value.copy(dateRange = date)
    }

    fun updateDestination(destination: String) {
        _tripDetails.value = _tripDetails.value.copy(destination = destination)
    }

    fun updateTransportation(transport: String) {
        _tripDetails.value = _tripDetails.value.copy(transportation = transport)
    }

    fun updateTravelers(travelers: String) {
        _tripDetails.value = _tripDetails.value.copy(travelers = travelers)
    }

    fun updatePurpose(purpose: String) {
        _tripDetails.value = _tripDetails.value.copy(purpose = purpose)
    }

    fun updatePlaceTypes(types: String) {
        _tripDetails.value = _tripDetails.value.copy(placeTypes = types)
    }

    fun createItinerary() {
        println("Creating itinerary: ${_tripDetails.value}")
    }
}