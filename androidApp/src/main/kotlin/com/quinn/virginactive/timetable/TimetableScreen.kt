package com.quinn.virginactive.timetable

import android.text.format.Time
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimetableScreen(

) {
    val viewModel = koinViewModel<TimetableViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTimetable()
    }

    TimetableContent(
        state = state,
        retry = viewModel::loadTimetable
    )


}

@Composable
private fun TimetableContent(
    state: TimetableViewModel.State,
    retry: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is TimetableViewModel.State.Error -> {
                Text(state.message)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = retry) {
                    Text("Retry")
                }
            }

            TimetableViewModel.State.Loading -> {
                CircularProgressIndicator()
            }

            is TimetableViewModel.State.Loaded -> {
                LoadedClassListContent(viewData = state.data)
            }
        }

    }
}

@Composable
fun LoadedClassListContent(viewData: ClassListViewData) {
    val sortedDays = viewData.classesPerDay.entries.sortedBy { it.key }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        sortedDays.forEach { (day, classes) ->
            // Sticky day header
            item(key = "header_$day") {
                DayHeader(day = day)
            }

            // Class cards for that day
            items(items = classes, key = { it.classId }) { classViewData ->
                ClassCard(
                    viewData = classViewData,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

// ── Day header ────────────────────────────────────────────────────────────────

@Composable
fun DayHeader(day: String) {
    Text(
        text = day,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 0.8.sp
        ),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

// ── Class card ────────────────────────────────────────────────────────────────

@Composable
fun ClassCard(viewData: ClassViewData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (viewData.startsWithin12Hours)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Top row: title + status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewData.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(status = viewData.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle row: type · trainer
            Text(
                text = "${viewData.classDisplayType} · ${viewData.trainer}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(10.dp))

            // Bottom meta row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetaItem(label = "Time", value = viewData.time)
                MetaItem(label = "Availability", value = viewData.availability)

//                if (viewData.status == ClassStatus.WAITLIST && viewData.waitlistCount > 0) {
//                    MetaItem(label = "Waitlist", value = "${viewData.waitlistCount}")
//                }

                viewData.confirmationDetails?.let {
                    MetaItem(label = "Booking", value = "#${it.bookingId}")
                }
            }

            // "Starts soon" pill
            if (viewData.startsWithin12Hours) {
                Spacer(modifier = Modifier.height(10.dp))
                StartsSoonPill()
            }
        }
    }
}

// ── Sub-components ────────────────────────────────────────────────────────────

@Composable
fun StatusBadge(status: ClassStatus) {
    val (label, background, contentColor) = when (status) {
        ClassStatus.AVAILABLE -> Triple("Open", Color(0xFF2E7D32), Color.White)
        ClassStatus.FULL -> Triple("Full", Color(0xFFB71C1C), Color.White)
//        ClassStatus.WAITLIST -> Triple("Waitlist", Color(0xFFE65100), Color.White)
//        ClassStatus.CANCELLED -> Triple("Cancelled", Color(0xFF616161), Color.White)
//        ClassStatus.BOOKED -> Triple("Booked", Color(0xFF1565C0), Color.White)
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = background
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun MetaItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                letterSpacing = 0.6.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StartsSoonPill() {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        modifier = Modifier.wrapContentWidth()
    ) {
        Text(
            text = "⏱ Starts within 12 hours",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}