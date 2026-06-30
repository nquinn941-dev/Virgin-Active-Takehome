package com.quinn.virginactive.classdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quinn.virginactive.timetable.ClassViewData
import org.koin.compose.viewmodel.koinViewModel

object ClassDetailColors {
    val Background = Color(0xFFFFFFFF)
    val SurfaceMuted = Color(0xFFF2F2F4)
    val TextPrimary = Color(0xFF15171A)
    val TextSecondary = Color(0xFF7A7D85)
    val PillRed = Color(0xFFE3473F)
    val PillRedText = Color(0xFFFFFFFF)
    val Mint = Color(0xFFCFF6E4)
    val MintText = Color(0xFF0E7A4F)
    val Amber = Color(0xFFFCEFC7)
    val AmberBorder = Color(0xFFE8B23A)
    val AmberText = Color(0xFF8A6510)
    val Coral = Color(0xFFFCE3E1)
    val CoralText = Color(0xFFB23A2E)
    val Danger = Color(0xFFE3473F)
}

@Composable
internal fun ClassDetailsScreen(
    id: String,
) {

    val viewModel = koinViewModel<ClassDetailsViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadClassDetails(classId = id)
    }

    ClassDetailsContent(
        state = state,
        retry = { viewModel.loadClassDetails(id) },
        bookClass = { viewModel.bookClass(id) },
        cancelClass = { viewModel.cancelClass(id) },
        setReminder = { }
    )


}

@Composable
private fun ClassDetailsContent(
    state: ClassDetailsViewModel.State,
    retry: () -> Unit,
    bookClass: (String) -> Unit,
    cancelClass: (String) -> Unit,
    setReminder: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is ClassDetailsViewModel.State.ClassNotFound -> {
                Text(
                    text = "No class found for id ${state.id}"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = retry) {
                    Text("Retry")
                }
            }
            is ClassDetailsViewModel.State.Error -> {
                Text(state.message)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = retry) {
                    Text("Retry")
                }
            }
            is ClassDetailsViewModel.State.Loaded -> {
                LoadedClassDetailsContent(
                    viewData = state.data,
                    bookClass = {
                        bookClass(state.data.classId)
                    },
                    cancelClass = {
                        cancelClass(state.data.classId)
                    },
                    setReminder = setReminder
                )
            }
            ClassDetailsViewModel.State.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun LoadedClassDetailsContent(
    viewData: ClassViewData,
    bookClass: () -> Unit,
    cancelClass: () -> Unit,
    setReminder: () -> Unit
) {
    Surface(color = ClassDetailColors.Background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            TypePill(viewData.classDisplayType)

            Spacer(Modifier.height(12.dp))

            Text(
                text = viewData.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = ClassDetailColors.TextPrimary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "with ${viewData.trainer}",
                fontSize = 14.sp,
                color = ClassDetailColors.TextSecondary
            )

            Spacer(Modifier.height(20.dp))

            DetailCard(viewData)

            Spacer(Modifier.height(16.dp))

            StatusBanner(viewData.bookingStatus, viewData.waitlistCount)

            if (viewData.startsWithin12Hours &&
                (viewData.bookingStatus == ClassViewData.BookingStatus.BOOKED ||
                        viewData.bookingStatus == ClassViewData.BookingStatus.WAITLISTED)
            ) {
                Spacer(Modifier.height(12.dp))
                InfoBanner(message = "This class starts within 12 hours. Cancelling may forfeit any associated cost.")
            }

            if (viewData.isInPast) {
                Spacer(Modifier.height(12.dp))
                InfoBanner(message = "This class is in the past")
            }
            viewData.confirmationDetails?.let {
                Spacer(Modifier.height(12.dp))
                ConfirmationBlock(viewData.confirmationDetails)
            }

            Spacer(Modifier.height(20.dp))

            if (!viewData.isInPast) {
                PrimaryOrSecondaryActions(
                    item = viewData,
                    onBook = bookClass,
                    onCancelBooking = cancelClass,
                    onSetReminder = setReminder
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ---------- Type pill ----------

@Composable
private fun TypePill(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(ClassDetailColors.PillRed)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            color = ClassDetailColors.PillRedText
        )
    }
}

// ---------- Detail card ----------

@Composable
private fun DetailCard(item: ClassViewData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ClassDetailColors.SurfaceMuted)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        DetailRow(label = "Date", value = item.date)
        DetailRow(label = "Time", value = item.time)
        DetailRow(label = "Availability", value = item.availability)
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = ClassDetailColors.TextSecondary)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ClassDetailColors.TextPrimary)
    }
}

// ---------- Status banner (booked / waitlisted / full / open) ----------

@Composable
private fun StatusBanner(status: ClassViewData.BookingStatus, waitlistCount: Int) {
    val (bg, text, label) = when (status) {
        ClassViewData.BookingStatus.BOOKED ->
            Triple(ClassDetailColors.Mint, ClassDetailColors.MintText, "You are booked for this class")
        ClassViewData.BookingStatus.WAITLISTED ->
            Triple(
                ClassDetailColors.Coral,
                ClassDetailColors.CoralText,
                "You're on the waitlist" + if (waitlistCount > 0) " · position $waitlistCount" else ""
            )
        ClassViewData.BookingStatus.FULL ->
            Triple(ClassDetailColors.Coral, ClassDetailColors.CoralText, "This class is full")
        ClassViewData.BookingStatus.OPEN ->
            Triple(ClassDetailColors.Mint, ClassDetailColors.MintText, "Spots are available")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = text)
    }
}

// ---------- Starts soon banner ----------

@Composable
private fun InfoBanner(
    message: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ClassDetailColors.Amber)
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(6.dp)
                .clip(RoundedCornerShape(50))
                .background(ClassDetailColors.AmberBorder)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = message ,
            fontSize = 13.sp,
            color = ClassDetailColors.AmberText,
            lineHeight = 18.sp
        )
    }
}

// ---------- Confirmation block ----------

@Composable
private fun ConfirmationBlock(details: ClassViewData.ClassConfirmationDetails?) {
    details ?: return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ClassDetailColors.Mint)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bookingHeader = if (details.confirmationStatus is ClassViewData.ClassConfirmationDetails.ConfirmationStatus.Waitlisted) {
            "You've been added to the waitlist - position ${(details.confirmationStatus as ClassViewData.ClassConfirmationDetails.ConfirmationStatus.Waitlisted).waitlistPosition}"
        } else {
            "Booking confirmed"
        }
        Text(
            text = bookingHeader,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ClassDetailColors.MintText
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Booking ID: ${details.bookingId}",
            fontSize = 12.sp,
            color = ClassDetailColors.MintText.copy(alpha = 0.85f)
        )
    }
}

// ---------- Actions ----------

@Composable
private fun PrimaryOrSecondaryActions(
    item: ClassViewData,
    onBook: () -> Unit,
    onCancelBooking: () -> Unit,
    onSetReminder: () -> Unit
) {
    when (item.bookingStatus) {
        ClassViewData.BookingStatus.OPEN -> {
            FilledActionButton(label = "Book Class", onClick = onBook)
        }
        ClassViewData.BookingStatus.FULL -> {
            FilledActionButton(label = "Join Waitlist", onClick = onBook)
        }
        ClassViewData.BookingStatus.WAITLISTED -> {
            SecondaryActionButton(label = "Set Reminder", onClick = onSetReminder)
            Spacer(Modifier.height(14.dp))
            DestructiveTextAction(label = "Leave Waitlist", onClick = onCancelBooking)
        }
        ClassViewData.BookingStatus.BOOKED -> {
            SecondaryActionButton(label = "Set Reminder", onClick = onSetReminder)
            Spacer(Modifier.height(14.dp))
            DestructiveTextAction(label = "Cancel Booking", onClick = onCancelBooking)
        }
    }
}

@Composable
private fun FilledActionButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ClassDetailColors.PillRed)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun SecondaryActionButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ClassDetailColors.SurfaceMuted)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = ClassDetailColors.TextPrimary)
    }
}

@Composable
private fun DestructiveTextAction(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ClassDetailColors.Danger)
    }
}