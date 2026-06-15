package com.mansur.walawili.ui.screens.trip.sheet

import androidx.compose.runtime.mutableStateOf

class TravelersSheetState {
    val isVisible = mutableStateOf(false)
    val selected = mutableStateOf<String?>(null)

    fun open() { isVisible.value = true }
    fun close() { isVisible.value = false }

    fun select(option: String) { selected.value = option }

    fun confirm(onResult: (String) -> Unit) {
        selected.value?.let { onResult(it) }
        close()
    }
}
