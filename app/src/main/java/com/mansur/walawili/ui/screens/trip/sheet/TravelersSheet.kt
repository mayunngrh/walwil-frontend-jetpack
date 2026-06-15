package com.mansur.walawili.ui.screens.trip.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.People
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

private data class TravelerOption(val label: String, val subtitle: String)

private val options = listOf(
    TravelerOption("Just me", "Solo trip"),
    TravelerOption("My partner", "2 travelers"),
    TravelerOption("Family", "With kids"),
    TravelerOption("Friends", "Small group"),
    TravelerOption("Tour group", "10+ people")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelersSheet(state: TravelersSheetState, onConfirm: () -> Unit) {
    val selected by state.selected

    ModalBottomSheet(
        onDismissRequest = { state.close() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { SheetDragHandle() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Who's coming with you?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1A38))
                SheetBadge("CHOOSE")
            }

            Spacer(modifier = Modifier.height(8.dp))

            options.forEach { option ->
                val isSelected = selected == option.label
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { state.select(option.label) }
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(if (isSelected) Color(0xFF2E2C8C) else Color(0xFFEBEDF5), RoundedCornerShape(14.dp))
                            ) {
                                Icon(Icons.Outlined.People, null, tint = if (isSelected) Color.White else Color(0xFF3D3B8E), modifier = Modifier.size(22.dp))
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(option.label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1B1A38))
                                Text(option.subtitle, fontSize = 12.sp, color = Color(0xFF9896B0))
                            }
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(24.dp)
                                .background(if (isSelected) Color(0xFF2E2C8C) else Color.Transparent, CircleShape)
                                .border(1.5.dp, if (isSelected) Color(0xFF2E2C8C) else Color(0xFFDDDCEA), CircleShape)
                        ) {
                            if (isSelected) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(start = 78.dp, end = 20.dp), thickness = 0.5.dp, color = Color(0xFFEDECF4))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfirm,
                enabled = selected != null,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2C8C), disabledContainerColor = Color(0xFFB0AECA)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Confirm", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
