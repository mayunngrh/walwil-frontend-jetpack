package com.mansur.walawili.ui.screens.destination

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mansur.walawili.ui.components.trip.TripPlanningHeader

data class DestinationSuggestion(
    val name: String,
    val subtitle: String,
    val isSelected: Boolean = false
)

private val allSuggestions = listOf(
    DestinationSuggestion("Bali, Indonesia", "Island · 9 regencies", isSelected = true),
    DestinationSuggestion("Bali Barat National Park", "West Bali"),
    DestinationSuggestion("Balikpapan", "East Kalimantan"),
    DestinationSuggestion("Balige", "North Sumatra · Lake Toba")
)

@Composable
fun DestinationScreen(
    currentDestination: String,
    onDestinationSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var query by remember { mutableStateOf(currentDestination) }
    val focusRequester = remember { FocusRequester() }

    val suggestions = remember(query) {
        if (query.isBlank()) allSuggestions
        else allSuggestions.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.subtitle.contains(query, ignoreCase = true)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F1EE))
    ) {
        TripPlanningHeader(
            title = "Where to?",
            onBackClick = onBackClick
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
            // Search field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF2E2C8C),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .background(Color.White, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Room,
                    contentDescription = null,
                    tint = Color(0xFF9896B0),
                    modifier = Modifier.size(20.dp)
                )

                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF1B1A38),
                        fontWeight = FontWeight.Normal
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(Color(0xFF2E2C8C)),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    decorationBox = { inner ->
                        if (query.isEmpty()) {
                            Text(
                                text = "Search destination...",
                                fontSize = 16.sp,
                                color = Color(0xFFB0AECA)
                            )
                        }
                        inner()
                    }
                )

                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear",
                        tint = Color(0xFF9896B0),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { query = "" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SUGGESTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9896B0),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyColumn {
            items(suggestions) { suggestion ->
                DestinationRow(
                    suggestion = suggestion,
                    isSelected = suggestion.name == currentDestination,
                    onClick = {
                        onDestinationSelected(suggestion.name)
                        onBackClick()
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 72.dp, end = 16.dp),
                    thickness = 0.5.dp,
                    color = Color(0xFFEDECF4)
                )
            }
        }
    }
}

@Composable
private fun DestinationRow(
    suggestion: DestinationSuggestion,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = if (isSelected) Color(0xFF2E2C8C) else Color(0xFFEBEDF5),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Room,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color(0xFF3D3B8E),
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = suggestion.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B1A38)
                )
                Text(
                    text = suggestion.subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF9896B0)
                )
            }
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF2E2C8C),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
