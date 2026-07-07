package com.quinn.virginactive.classdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinn.virginactive.LocalClassReminder
import com.quinn.virginactive.timetable.ClassViewData
import com.quinn.virginactive.timetable.usecases.AlreadyBookedException
import com.quinn.virginactive.timetable.usecases.BookClassUseCase
import com.quinn.virginactive.timetable.usecases.CancelBookingUseCase
import com.quinn.virginactive.timetable.usecases.ClassInPastException
import com.quinn.virginactive.timetable.usecases.GetClassViewDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class ClassDetailsViewModel constructor(
    val getClassViewDataUseCase: GetClassViewDataUseCase,
    val bookClassUseCase: BookClassUseCase,
    val cancelBookingUseCase: CancelBookingUseCase,
    val localClassReminder: LocalClassReminder
) : ViewModel() {

    sealed class State {
        data object Loading : State()
        data class Loaded(val data: ClassViewData) : State()
        data class ClassNotFound(val id: String) : State()
        data class Error(val message: String) : State()
    }

    sealed class BookingState {
        data object None: BookingState()
        data class Error(val message: String): BookingState()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state : StateFlow<State> = _state

    private val _bookingState = MutableStateFlow<BookingState>(BookingState.None)
    val bookingState : StateFlow<BookingState> = _bookingState



    fun loadClassDetails(classId: String) {
        _state.value = State.Loading
        viewModelScope.launch {
            try {
                getClassViewDataUseCase.getClassViewData(classId).collectLatest {
                    if (it != null) {
                        _state.value = State.Loaded(it)
                    } else {
                        _state.value = State.ClassNotFound(classId)
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _state.value = State.Error(ex.message ?: "Unknown error")
            }
        }
    }

    fun bookClass(classId: String) {
        if (_state.value !is State.Loaded) return
        val existingLoadedState = (_state.value as State.Loaded)
        _state.value = State.Loading
        viewModelScope.launch {
            try {
                val bookedViewData = bookClassUseCase.bookClass(classId)
                _state.value = State.Loaded(bookedViewData)
            } catch (ex: Exception) {
                _state.value = existingLoadedState
                val errorMessage = when (ex) {
                    is AlreadyBookedException -> "This class is already booked"
                    is ClassInPastException -> "This class has already started"
                    else -> {
                        "Unknown error"
                    }
                }
                _bookingState.value = BookingState.Error(errorMessage)
            }
        }
    }

    fun cancelClass(classId: String) {
        if (_state.value !is State.Loaded) return
        val existingLoadedState = (_state.value as State.Loaded)
        _state.value = State.Loading
        viewModelScope.launch {
            try {
                cancelBookingUseCase.cancelBooking(classId)
            } catch (ex: Exception) {
                ex.printStackTrace()
                _state.value = existingLoadedState
                _bookingState.value = BookingState.Error("Something went wrong, please try again")
            }
        }
    }

    fun setLocalReminder(viewData: ClassViewData) {
        localClassReminder.setLocalReminder(
            title = viewData.title,
            location = viewData.trainer,
            startTime = viewData.startTimeEpoch,
            endTime = viewData.endTimeEpoch
        )
    }
}