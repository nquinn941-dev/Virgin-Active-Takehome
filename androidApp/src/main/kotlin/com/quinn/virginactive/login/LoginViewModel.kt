package com.quinn.virginactive.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quinn.virginactive.UserManager
import com.quinn.virginactive.auth.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class LoginViewModel constructor(
    private val loginUseCase: LoginUseCase,
    private val userManager: UserManager
): ViewModel() {

    sealed class State {
        data object Loading: State()
        data object LoginSuccessful : State()
        data object LogInRequired : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state : StateFlow<State> = _state

    var viewData by mutableStateOf(LoginViewData())
        private set

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            userManager.observeUserState().collectLatest {
                _state.value = when (it) {
                    UserManager.UserState.Loading -> State.Loading
                    UserManager.UserState.LoggedOut -> State.LogInRequired
                    is UserManager.UserState.LoggedIn -> State.LoginSuccessful
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            loginUseCase.login(username = viewData.email, password = viewData.password)
        }
    }

    fun updateField(update: LoginViewData.() -> LoginViewData) {
        viewData = viewData.update()
    }
}