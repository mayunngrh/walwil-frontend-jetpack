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

    private val _showPurposeSheet = mutableStateOf(false)
    val showPurposeSheet: State<Boolean> = _showPurposeSheet

    private val _selectedPurpose = mutableStateOf<String?>(null)
    val selectedPurpose: State<String?> = _selectedPurpose

    private val _customPurpose = mutableStateOf("")
    val customPurpose: State<String> = _customPurpose

    fun openPurposeSheet() { _showPurposeSheet.value = true }
    fun closePurposeSheet() { _showPurposeSheet.value = false }
    fun selectPurpose(option: String) { _selectedPurpose.value = if (_selectedPurpose.value == option) null else option }
    fun setCustomPurpose(text: String) { _customPurpose.value = text }

    fun confirmPurpose() {
        val value = _customPurpose.value.trim().ifEmpty { _selectedPurpose.value } ?: return
        _tripDetails.value = _tripDetails.value.copy(purpose = value)
        _showPurposeSheet.value = false
    }

    private val _showPlacesSheet = mutableStateOf(false)
    val showPlacesSheet: State<Boolean> = _showPlacesSheet

    private val _selectedPlaces = mutableStateOf<Set<String>>(emptySet())
    val selectedPlaces: State<Set<String>> = _selectedPlaces

    private val _customPlace = mutableStateOf("")
    val customPlace: State<String> = _customPlace

    fun openPlacesSheet() { _showPlacesSheet.value = true }
    fun closePlacesSheet() { _showPlacesSheet.value = false }
    fun togglePlace(option: String) {
        val current = _selectedPlaces.value
        _selectedPlaces.value = if (current.contains(option)) current - option else current + option
    }
    fun setCustomPlace(text: String) { _customPlace.value = text }

    fun confirmPlaces() {
        val custom = _customPlace.value.trim()
        val all = _selectedPlaces.value.toMutableList()
        if (custom.isNotEmpty()) all.add(custom)
        _tripDetails.value = _tripDetails.value.copy(placeTypes = all.joinToString(", "))
        _showPlacesSheet.value = false
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