package com.mansur.walawili.ui.screens.trip.sheet

import androidx.compose.runtime.mutableStateOf

class PurposeSheetState {
    val isVisible = mutableStateOf(false)
    val selected = mutableStateOf<String?>(null)
    val customText = mutableStateOf("")

    fun open() { isVisible.value = true }
    fun close() { isVisible.value = false }

    fun select(option: String) {
        selected.value = if (selected.value == option) null else option
    }

    fun setCustomText(text: String) {
        customText.value = text
        if (text.isNotEmpty()) selected.value = null
    }

    fun confirm(onResult: (String) -> Unit) {
        val value = customText.value.trim().ifEmpty { selected.value } ?: return
        onResult(value)
        close()
    }
}
