package com.quinn.virginactive.timetable

import android.text.format.Time
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.quinn.virginactive.uicompose.LocalExtendedColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TimetableScreen(
    viewDetails: (String) -> Unit
) {
    val viewModel = koinViewModel<TimetableViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTimetable()
    }

    TimetableContent(
        state = state,
        retry = viewModel::loadTimetable,
        viewDetails = viewDetails
    )


}

@Composable
private fun TimetableContent(
    state: TimetableViewModel.State,
    retry: () -> Unit,
    viewDetails: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                LoadedClassListContent(viewData = state.data, viewDetails = viewDetails)
            }
        }

    }
}

@Composable
fun LoadedClassListContent(viewData: ClassListViewData, viewDetails: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        viewData.classesPerDay.entries.forEach { (day, classes) ->
            item(key = "header_$day") {
                DayHeader(day = day)
            }

            items(items = classes, key = { it.classId }) { classViewData ->
                ClassCard(
                    viewData = classViewData,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    viewDetails = viewDetails
                )
            }
        }
    }
}

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

@Composable
fun ClassCard(viewData: ClassViewData, modifier: Modifier = Modifier, viewDetails: (String) -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().clickable { viewDetails(viewData.classId) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewData.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(status = viewData.bookingStatus)
            }

            Spacer(modifier = Modifier.height(4.dp))


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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetaItem(label = "Time", value = viewData.time)
                MetaItem(label = "Availability", value = viewData.availability)
            }
        }
    }
}

@Composable
fun StatusBadge(status: ClassViewData.BookingStatus) {
    // Consistent status semantics used across the app:
    // success (open/booked) · tertiary (waitlisted, cautionary) · error (full)
    val extended = LocalExtendedColors.current
    val (label, background, contentColor) = when (status) {
        ClassViewData.BookingStatus.OPEN ->
            Triple("Open", extended.successContainer, extended.onSuccessContainer)

        ClassViewData.BookingStatus.BOOKED ->
            Triple("Booked", extended.successContainer, extended.onSuccessContainer)

        ClassViewData.BookingStatus.WAITLISTED ->
            Triple("Waitlist", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)

        ClassViewData.BookingStatus.FULL ->
            Triple("Full", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
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
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}