package com.mansur.walawili.ui.screens.trip.sheet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SheetDragHandle() {
    Box(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 4.dp)
            .width(36.dp)
            .height(4.dp)
            .background(Color(0xFFDDDCEA), RoundedCornerShape(2.dp))
    )
}

@Composable
fun SheetBadge(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFF3DC), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB07800))
    }
}
