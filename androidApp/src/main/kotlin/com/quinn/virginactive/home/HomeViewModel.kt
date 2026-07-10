package com.quinn.virginactive.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinn.virginactive.home.usecases.GetHomeViewDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class HomeViewModel (
    private val getHomeViewDataUseCase: GetHomeViewDataUseCase
): ViewModel() {

    sealed class State {
        object Loading : State()
        data class Loaded(val homeViewData: HomeViewData) : State()
        data class Error(val error: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state : StateFlow<State> = _state

    fun load() {
        if (_state.value !is State.Loaded) _state.value = State.Loading
        viewModelScope.launch {
            _state.value = try {
                val homeViewData = getHomeViewDataUseCase.getHomeViewData()
                State.Loaded(homeViewData)
            } catch (ex: Exception) {
                ex.printStackTrace()
                State.Error(ex.message ?: "Something went wrong try again later")
            }
        }
    }
}