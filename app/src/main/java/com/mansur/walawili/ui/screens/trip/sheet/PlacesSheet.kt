package com.mansur.walawili.ui.screens.trip.sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mansur.walawili.ui.screens.trip.sheet.components.SheetBadge
import com.mansur.walawili.ui.screens.trip.sheet.components.SheetDragHandle

private val options = listOf(
    "Beaches", "Cafés", "Temples & culture", "Nature", "Local food", "Nightlife", "Shopping", "Art & museums", "Waterfalls"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesSheet(state: PlacesSheetState, onConfirm: () -> Unit) {
    val selected by state.selected
    val customText by state.customText

    ModalBottomSheet(
        onDismissRequest = { state.close() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { SheetDragHandle() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("What kind of places?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1A38))
                    Text("Pick as many as you like, or add your own.", fontSize = 13.sp, color = Color(0xFF9896B0), modifier = Modifier.padding(top = 2.dp))
                }
                SheetBadge("MULTI / TYPE")
            }

            Spacer(modifier = Modifier.height(20.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    SelectableChip(label = option, isSelected = selected.contains(option)) { state.toggle(option) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, Color(0xFFDDDCEA), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("+", fontSize = 16.sp, color = Color(0xFFB0AECA))
                BasicTextField(
                    value = customText,
                    onValueChange = { state.setCustomText(it) },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(fontSize = 15.sp, color = Color(0xFF1B1A38)),
                    singleLine = true,
                    cursorBrush = SolidColor(Color(0xFF2E2C8C)),
                    decorationBox = { inner ->
                        if (customText.isEmpty()) Text("e.g. hidden waterfalls, surf spots...", fontSize = 15.sp, color = Color(0xFFB0AECA))
                        inner()
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            val totalSelected = selected.size + if (customText.trim().isNotEmpty()) 1 else 0
            Button(
                onClick = onConfirm,
                enabled = totalSelected > 0,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2C8C), disabledContainerColor = Color(0xFFB0AECA)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = if (totalSelected == 0) "Confirm" else "Confirm · $totalSelected selected",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            }
        }
    }
}
