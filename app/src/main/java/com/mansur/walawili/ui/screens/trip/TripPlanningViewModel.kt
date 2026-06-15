package com.mansur.walawili.ui.screens.trip

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class TripDetails(
    val dateRange: String = "",
    val destination: String = "",
    val transportation: String = "",
    val travelers: String = "",
    val purpose: String = "",
    val placeTypes: String = ""
)

class TripPlanningViewModel : ViewModel() {
    private val _tripDetails = mutableStateOf(TripDetails())
    val tripDetails: State<TripDetails> = _tripDetails

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