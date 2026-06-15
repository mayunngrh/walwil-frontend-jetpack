package com.mansur.walawili.ui.screens.itinerary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

private val Primary = Color(0xFF2E2C8C)
private val BgColor = Color(0xFFF2F1EE)
private val AmberText = Color(0xFFB07D00)
private val AmberBg = Color(0xFFFFF3DC)
private val SubtleGray = Color(0xFF9896B0)
private val DividerColor = Color(0xFFE0DFF0)

data class ItineraryStop(
    val id: String,
    val time: String,
    val title: String,
    val location: String,
    val timeRange: String,
    val travelNext: String,
    val description: String,
    val photoLabel: String
)

data class ItineraryDay(
    val label: String,
    val dateHeading: String,
    val areaWeather: String,
    val stops: List<ItineraryStop>
)

private val sampleDays = listOf(
    ItineraryDay(
        label = "Day 1",
        dateHeading = "Monday, 5 Oct",
        areaWeather = "Ubud · 28°",
        stops = listOf(
            ItineraryStop("1a", "08:30", "Breakfast at Sisterfields", "Seminyak", "08:30 – 10:00", "12 min · scooter", "Start the day with great coffee and eggs benny at this beloved Seminyak spot.", "sisterfields photo"),
            ItineraryStop("1b", "10:30", "Tegallalang Rice Terrace", "Tegallalang, Ubud", "10:30 – 12:00", "1 h · rental car", "Iconic emerald terraces with the Bali Swing nearby.", "rice terrace photo"),
            ItineraryStop("1c", "13:00", "Lunch at Locavore To Go", "Ubud", "13:00 – 14:00", "8 min · walk", "Farm-to-table bento boxes from award-winning Locavore.", "locavore photo"),
            ItineraryStop("1d", "15:30", "Sacred Monkey Forest", "Ubud", "15:30 – 17:00", "45 min · car", "Wander ancient temples and meet the resident long-tailed macaques.", "monkey forest photo"),
            ItineraryStop("1e", "18:30", "Sunset at Tanah Lot", "Tabanan", "18:30 – 20:00", "", "One of Bali's most iconic sea temples, magical at golden hour.", "tanah lot photo")
        )
    ),
    ItineraryDay(
        label = "Day 2",
        dateHeading = "Tuesday, 6 Oct",
        areaWeather = "Canggu · 29°",
        stops = listOf(
            ItineraryStop("2a", "09:00", "Surf at Batu Bolong", "Canggu", "09:00 – 11:00", "15 min · scooter", "Beginner-friendly left-hand break perfect for morning sessions.", "surf photo"),
            ItineraryStop("2b", "11:30", "Brunch at Betelnut", "Canggu", "11:30 – 13:00", "20 min · walk", "Healthy bowls and fresh juices in a relaxed garden setting.", "betelnut photo"),
            ItineraryStop("2c", "14:00", "Tanah Lot Temple Tour", "Tabanan", "14:00 – 16:30", "30 min · car", "Explore the iconic offshore temple complex during low tide.", "tanah lot tour photo"),
            ItineraryStop("2d", "19:00", "Dinner at Mozaic", "Ubud", "19:00 – 21:00", "", "Fine dining in a romantic garden — book ahead.", "mozaic photo")
        )
    ),
    ItineraryDay(
        label = "Day 3",
        dateHeading = "Wednesday, 7 Oct",
        areaWeather = "Seminyak · 30°",
        stops = listOf(
            ItineraryStop("3a", "08:00", "Mount Batur Sunrise Trek", "Kintamani", "08:00 – 13:00", "1.5 h · car", "Early start for the rewarding climb to Bali's active volcano.", "mount batur photo"),
            ItineraryStop("3b", "14:00", "Hot Spring Soak", "Toya Bungkah", "14:00 – 15:30", "10 min · walk", "Natural hot springs with views over Lake Batur.", "hot spring photo"),
            ItineraryStop("3c", "17:00", "Spa at COMO Shambhala", "Ubud", "17:00 – 18:30", "45 min · car", "Recover with a traditional Balinese massage.", "spa photo"),
            ItineraryStop("3d", "20:00", "Farewell Dinner", "Seminyak", "20:00 – 22:00", "", "Rooftop dinner at Ku De Ta with ocean views.", "farewell dinner photo")
        )
    )
)

// Shared mutable state lifted so MainActivity can access it for edit navigation
val itineraryDayStops: MutableList<androidx.compose.runtime.snapshots.SnapshotStateList<ItineraryStop>> by lazy {
    sampleDays.map { it.stops.toMutableStateList() }.toMutableStateList()
}

@Composable
fun ItineraryScreen(
    tripTitle: String = "Bali trip",
    tripSubtitle: String = "5 – 9 Oct · 2 travelers",
    onBackClick: () -> Unit,
    onEditStop: (ItineraryStop) -> Unit = {}
) {
    var selectedDayIndex by remember { mutableIntStateOf(0) }

    // Use shared mutable state so edits from EditActivityScreen reflect here
    val dayStops = remember { itineraryDayStops }

    var expandedStopId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedDayIndex) { expandedStopId = null }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        // ── Header ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 52.dp, bottom = 14.dp)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDECF0))
                    .clickable { onBackClick() }
                    .align(Alignment.CenterStart)
            ) {
                Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Back", tint = Color(0xFF1B1A38), modifier = Modifier.size(16.dp))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(Alignment.Center)) {
                Text(tripTitle, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1A38))
                Text(tripSubtitle, fontSize = 13.sp, color = SubtleGray)
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDECF0))
                    .align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Outlined.Share, contentDescription = "Share", tint = Primary, modifier = Modifier.size(18.dp))
            }
        }

        // ── Scrollable content ──────────────────────────────────
        val currentStops = dayStops[selectedDayIndex]
        val lazyListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        // Scroll expanded card into view (3 header items: banner, tabs, heading)
        val headerCount = 3
        LaunchedEffect(expandedStopId) {
            if (expandedStopId != null) {
                val stopIndex = currentStops.indexOfFirst { it.id == expandedStopId }
                if (stopIndex >= 0) {
                    coroutineScope.launch {
                        // Wait for AnimatedVisibility expand to finish (~300ms)
                        delay(320)
                        val itemIndex = headerCount + stopIndex
                        val layoutInfo = lazyListState.layoutInfo
                        val visibleItem = layoutInfo.visibleItemsInfo.firstOrNull { it.index == itemIndex }
                        val viewportEnd = layoutInfo.viewportEndOffset

                        if (visibleItem != null) {
                            val itemBottom = visibleItem.offset + visibleItem.size
                            val overflow = itemBottom - viewportEnd
                            if (overflow > 0) {
                                // Item is partially off-screen — scroll just enough to show its bottom
                                lazyListState.scroll {
                                    scrollBy(overflow.toFloat() + 24f)
                                }
                            }
                        } else {
                            // Item not visible at all — scroll it into view
                            lazyListState.animateScrollToItem(itemIndex, scrollOffset = -80)
                        }
                    }
                }
            }
        }

        val reorderState = rememberReorderableLazyListState(lazyListState) { from, to ->
            val fromStop = from.index - headerCount
            val toStop = to.index - headerCount
            if (fromStop >= 0 && toStop >= 0 && fromStop < currentStops.size && toStop < currentStops.size) {
                currentStops.add(toStop, currentStops.removeAt(fromStop))
            }
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // AI banner
            item(key = "banner") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .background(AmberBg, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("✦", fontSize = 14.sp, color = AmberText)
                    Text(
                        "Drafted by Walla Willi AI — drag, edit or add anything.",
                        fontSize = 14.sp, color = AmberText,
                        fontWeight = FontWeight.SemiBold, lineHeight = 20.sp
                    )
                }
            }

            // Day tabs
            item(key = "tabs") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    sampleDays.forEachIndexed { index, day ->
                        val isSelected = index == selectedDayIndex
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(if (isSelected) Primary else Color.White)
                                .border(1.dp, if (isSelected) Primary else DividerColor, RoundedCornerShape(50.dp))
                                .clickable { selectedDayIndex = index }
                                .padding(horizontal = 18.dp, vertical = 9.dp)
                        ) {
                            Text(day.label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else Color(0xFF1B1A38))
                        }
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AmberBg)
                            .clickable { }
                    ) {
                        Text("+", fontSize = 18.sp, color = AmberText, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Date + weather heading
            item(key = "heading") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(sampleDays[selectedDayIndex].dateHeading, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1A38))
                    Text("· ${sampleDays[selectedDayIndex].areaWeather}", fontSize = 14.sp, color = SubtleGray)
                }
            }

            // Reorderable stop items
            itemsIndexed(currentStops, key = { _, stop -> stop.id }) { index, stop ->
                ReorderableItem(reorderState, key = stop.id) { isDragging ->
                    val isExpanded = expandedStopId == stop.id
                    val isLast = index == currentStops.lastIndex

                    val cardModifier = if (isDragging)
                        Modifier.shadow(8.dp, RoundedCornerShape(16.dp))
                    else Modifier

                    TimelineStopItem(
                        stop = stop,
                        isExpanded = isExpanded,
                        isLast = isLast,
                        isDragging = isDragging,
                        dragHandleModifier = Modifier.draggableHandle(),
                        cardModifier = cardModifier,
                        onToggle = {
                            expandedStopId = if (isExpanded) null else stop.id
                        },
                        onEditClick = { onEditStop(stop) }
                    )
                }
            }
        }

        // ── Bottom nav ──────────────────────────────────────────
        HorizontalDivider(thickness = 0.5.dp, color = DividerColor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem(icon = Icons.Outlined.Home, label = "Home", isActive = false)
            BottomNavItem(icon = Icons.Outlined.MenuBook, label = "Plans", isActive = true)
            BottomNavItem(icon = Icons.Outlined.Person, label = "Profile", isActive = false)
        }
    }
}

@Composable
private fun TimelineStopItem(
    stop: ItineraryStop,
    isExpanded: Boolean,
    isLast: Boolean,
    isDragging: Boolean,
    dragHandleModifier: Modifier,
    cardModifier: Modifier,
    onToggle: () -> Unit,
    onEditClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left: time + dot + line
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(52.dp)) {
            Text(stop.time, fontSize = 13.sp, color = SubtleGray)
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .border(2.5.dp, Primary, CircleShape)
                    .background(Color.White)
            )
            if (!isLast || stop.travelNext.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(if (isExpanded) 300.dp else 72.dp)
                        .background(Color(0xFFDDDCEA))
                )
            }
        }

        // Right: card + travel connector
        Column(modifier = Modifier.weight(1f)) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(cardModifier)
                    .then(
                        if (isExpanded) Modifier.border(1.5.dp, Primary, RoundedCornerShape(16.dp))
                        else Modifier
                    )
            ) {
                Column(modifier = Modifier.clickable { onToggle() }) {
                    // Collapsed header row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stop.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1B1A38))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(top = 3.dp)
                            ) {
                                Icon(Icons.Outlined.Room, null, tint = SubtleGray, modifier = Modifier.size(12.dp))
                                Text(stop.location, fontSize = 13.sp, color = SubtleGray)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Drag handle — long-press to drag
                            Icon(
                                Icons.Outlined.DragIndicator,
                                contentDescription = "Drag to reorder",
                                tint = if (isDragging) Primary else SubtleGray.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .then(dragHandleModifier)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = SubtleGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Expanded content
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 14.dp).padding(bottom = 14.dp)) {
                            // Photo placeholder
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFEBEDF5))
                            ) {
                                Text(stop.photoLabel, fontSize = 13.sp, color = SubtleGray)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Outlined.Schedule, null, tint = SubtleGray, modifier = Modifier.size(14.dp))
                                    Text(stop.timeRange, fontSize = 13.sp, color = SubtleGray)
                                }
                                if (stop.travelNext.isNotEmpty()) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Outlined.DirectionsCar, null, tint = SubtleGray, modifier = Modifier.size(14.dp))
                                        Text(stop.travelNext, fontSize = 13.sp, color = SubtleGray)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(stop.description, fontSize = 14.sp, color = Color(0xFF1B1A38), lineHeight = 20.sp)
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { onEditClick() },
                                    modifier = Modifier.weight(1f).height(40.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text("Edit details", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                }
                                OutlinedButton(
                                    onClick = {},
                                    modifier = Modifier.weight(1f).height(40.dp),
                                    border = BorderStroke(1.5.dp, Primary),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
                                ) {
                                    Text("Directions", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                                }
                            }
                        }
                    }
                }
            }

            // Travel connector below card
            if (stop.travelNext.isNotEmpty() && !isExpanded) {
                Row(
                    modifier = Modifier.padding(start = 4.dp).padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Outlined.SwapCalls, null, tint = SubtleGray, modifier = Modifier.size(14.dp))
                    Text(stop.travelNext, fontSize = 12.sp, color = SubtleGray)
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun BottomNavItem(icon: ImageVector, label: String, isActive: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = label, tint = if (isActive) Primary else SubtleGray, modifier = Modifier.size(22.dp))
        Text(label, fontSize = 11.sp, color = if (isActive) Primary else SubtleGray,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal)
    }
}
