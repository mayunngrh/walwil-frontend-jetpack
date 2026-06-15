package com.mansur.walawili.ui.screens.trip.sheet

import androidx.compose.runtime.mutableStateOf

class TransportSheetState {
    val isVisible = mutableStateOf(false)
    val selected = mutableStateOf<Set<String>>(emptySet())

    fun open() { isVisible.value = true }
    fun close() { isVisible.value = false }

    fun toggle(option: String) {
        val current = selected.value
        selected.value = if (current.contains(option)) current - option else current + option
    }

    fun confirm(onResult: (String) -> Unit) {
        onResult(selected.value.joinToString(", "))
        close()
    }
}
