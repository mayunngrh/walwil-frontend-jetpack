package com.mansur.walawili.ui.screens.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mansur.walawili.ui.components.trip.TripPlanningHeader
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val Primary = Color(0xFF2E2C8C)
private val RangeBackground = Color(0xFFE8E9F5)

@Composable
fun DatePickerScreen(
    initialDateRange: String,
    onApply: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var yearMonth by remember { mutableStateOf(YearMonth.of(2026, 10)) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    val nightCount = if (startDate != null && endDate != null)
        java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate).toInt()
    else 0

    val summaryText = if (startDate != null && endDate != null) {
        val monthShort = startDate!!.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        val endMonthShort = endDate!!.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        val sameMonth = startDate!!.month == endDate!!.month && startDate!!.year == endDate!!.year
        if (sameMonth)
            "${startDate!!.dayOfMonth} – ${endDate!!.dayOfMonth} $monthShort ${startDate!!.year}"
        else
            "${startDate!!.dayOfMonth} $monthShort – ${endDate!!.dayOfMonth} $endMonthShort ${endDate!!.year}"
    } else ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F1EE))
    ) {
        TripPlanningHeader(
            title = "When are you going?",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Month header
            Text(
                text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${yearMonth.year}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1A38)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Day-of-week labels
            val dayNames = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
            Row(modifier = Modifier.fillMaxWidth()) {
                dayNames.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = Color(0xFF9896B0),
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Calendar grid
            CalendarGrid(
                yearMonth = yearMonth,
                startDate = startDate,
                endDate = endDate,
                onDateClick = { date ->
                    when {
                        startDate == null -> startDate = date
                        endDate == null && date >= startDate!! -> endDate = date
                        endDate == null && date < startDate!! -> {
                            endDate = startDate
                            startDate = date
                        }
                        else -> {
                            startDate = date
                            endDate = null
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Summary pill
            if (startDate != null && endDate != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(RangeBackground, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = summaryText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B1A38)
                    )
                    Text(
                        text = "· $nightCount nights",
                        fontSize = 14.sp,
                        color = Primary,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        // Apply button
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE0DFF0))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F1EE))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = {
                    onApply(summaryText)
                    onBackClick()
                },
                enabled = startDate != null && endDate != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    disabledContainerColor = Color(0xFFB0AECA)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Apply dates",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDay = yearMonth.atDay(1)
    val startDow = firstDay.dayOfWeek.value % 7 // Sunday = 0
    val daysInMonth = yearMonth.lengthOfMonth()
    val totalCells = startDow + daysInMonth
    val rows = (totalCells + 6) / 7

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val cellIndex = row * 7 + col
                    val day = cellIndex - startDow + 1
                    if (day < 1 || day > daysInMonth) {
                        Box(modifier = Modifier.weight(1f).height(44.dp))
                    } else {
                        val date = yearMonth.atDay(day)
                        val isStart = date == startDate
                        val isEnd = date == endDate
                        val inRange = startDate != null && endDate != null &&
                                date > startDate && date < endDate
                        val isRangeStart = isStart && endDate != null
                        val isRangeEnd = isEnd && startDate != null

                        DayCell(
                            day = day,
                            isStart = isStart,
                            isEnd = isEnd,
                            inRange = inRange,
                            isRangeStart = isRangeStart,
                            isRangeEnd = isRangeEnd,
                            modifier = Modifier.weight(1f),
                            onClick = { onDateClick(date) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isStart: Boolean,
    isEnd: Boolean,
    inRange: Boolean,
    isRangeStart: Boolean,
    isRangeEnd: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isSelected = isStart || isEnd

    Box(
        modifier = modifier.height(44.dp),
        contentAlignment = Alignment.Center
    ) {
        // Left half: fill if this is an end date or a middle range day
        if (inRange || isRangeEnd) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterStart)
                    .background(RangeBackground)
            )
        }
        // Right half: fill if this is a start date or a middle range day
        if (inRange || isRangeStart) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterEnd)
                    .background(RangeBackground)
            )
        }

        // Circle for selected
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isSelected) Primary else Color.Transparent)
                .clickable(onClick = onClick)
        ) {
            Text(
                text = day.toString(),
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color(0xFF1B1A38),
                textAlign = TextAlign.Center
            )
        }
    }
}
