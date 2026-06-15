package com.mansur.walawili.ui.screens.trip.sheet

import androidx.compose.runtime.mutableStateOf

class PlacesSheetState {
    val isVisible = mutableStateOf(false)
    val selected = mutableStateOf<Set<String>>(emptySet())
    val customText = mutableStateOf("")

    fun open() { isVisible.value = true }
    fun close() { isVisible.value = false }

    fun toggle(option: String) {
        val current = selected.value
        selected.value = if (current.contains(option)) current - option else current + option
    }

    fun setCustomText(text: String) { customText.value = text }

    fun confirm(onResult: (String) -> Unit) {
        val all = selected.value.toMutableList()
        val custom = customText.value.trim()
        if (custom.isNotEmpty()) all.add(custom)
        if (all.isEmpty()) return
        onResult(all.joinToString(", "))
        close()
    }
}
