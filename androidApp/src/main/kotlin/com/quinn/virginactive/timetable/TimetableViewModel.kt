package com.quinn.virginactive.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinn.virginactive.UserManager
import com.quinn.virginactive.UserState
import com.quinn.virginactive.timetable.usecases.GetClassesViewDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class TimetableViewModel(
    private val getClassesViewDataUseCase: GetClassesViewDataUseCase,
    private val userManager: UserManager
): ViewModel() {

    sealed class State {
        data object Loading : State()
        data class Error(val message: String) : State()
        data class Loaded(val data: ClassListViewData) : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state : StateFlow<State> = _state

    fun loadTimetable() {
        _state.value = State.Loading
        val user = (userManager.getUserStateSnapshot() as? UserState.LoggedIn)?.user
        user?.let {
            try {
                viewModelScope.launch {
                    val viewData = getClassesViewDataUseCase.getClassesViewData(clubId = it.clubInfo.id)
                    _state.value = State.Loaded(viewData)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _state.value = State.Error(ex.message ?: "Unknown error")
            }
        } ?: {
            _state.value = State.Error("User not found")
        }
    }
}