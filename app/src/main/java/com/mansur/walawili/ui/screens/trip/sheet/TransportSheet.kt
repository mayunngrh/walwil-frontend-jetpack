package com.mansur.walawili.ui.screens.trip.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mansur.walawili.ui.screens.trip.sheet.components.SheetBadge
import com.mansur.walawili.ui.screens.trip.sheet.components.SheetDragHandle

private val options = listOf("Flight", "Rental car", "Scooter", "Train", "Taxi / Grab", "Walk", "Ferry")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportSheet(state: TransportSheetState, onConfirm: () -> Unit) {
    val selected by state.selected

    ModalBottomSheet(
        onDismissRequest = { state.close() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { SheetDragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("How will you get around?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1A38))
                    Text("Tap all that apply.", fontSize = 13.sp, color = Color(0xFF9896B0), modifier = Modifier.padding(top = 2.dp))
                }
                SheetBadge("CHOOSE")
            }

            Spacer(modifier = Modifier.height(20.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    val isSelected = selected.contains(option)
                    SelectableChip(label = option, isSelected = isSelected, onClick = { state.toggle(option) })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onConfirm,
                enabled = selected.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2C8C), disabledContainerColor = Color(0xFFB0AECA)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = if (selected.isEmpty()) "Confirm" else "Confirm · ${selected.size} selected",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            }
        }
    }
}

@Composable
internal fun SelectableChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (isSelected) Color(0xFF2E2C8C) else Color.Transparent, RoundedCornerShape(50.dp))
            .border(1.5.dp, if (isSelected) Color(0xFF2E2C8C) else Color(0xFFDDDCEA), RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color.White else Color(0xFF1B1A38)
        )
    }
}
