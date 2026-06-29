package com.quinn.virginactive.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.quinn.virginactive.R
import org.koin.compose.viewmodel.koinViewModel
import kotlin.collections.copy
import kotlin.math.log

@Composable
internal fun LoginScreen(
    successfulLogin: () -> Unit
) {
    val loginViewModel = koinViewModel<LoginViewModel>()
    val state by loginViewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is LoginViewModel.State.LoginSuccessful) {
            successfulLogin()
        }
    }

    LoginContent(
        state = state,
        viewData = loginViewModel.viewData,
        login = loginViewModel::login,
        updateField = loginViewModel::updateField
    )
}

@Composable
internal fun LoginContent(
    state: LoginViewModel.State,
    viewData: LoginViewData,
    login: () -> Unit,
    updateField: (LoginViewData.() -> LoginViewData) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().safeContentPadding(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        when (state) {
            LoginViewModel.State.Loading -> {
                CircularProgressIndicator()
            }
            LoginViewModel.State.LogInRequired -> {
                LoginFields(
                    viewData = viewData,
                    login = login,
                    updateField = updateField
                )
            }
            LoginViewModel.State.LoginSuccessful -> {
                //Should never happen
            }
        }
    }
}

@Composable
private fun LoginFields(
    viewData: LoginViewData,
    login: () -> Unit,
    updateField: (LoginViewData.() -> LoginViewData) -> Unit
) {
    Text(
        text = "Virgin Active"
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Please sign into your account"
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = viewData.email,
        onValueChange = { updateField { copy(email = it)}},
        label = { Text("Email") }
    )

    Spacer(modifier = Modifier.height(16.dp))

    var showPassword by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = viewData.password,
        onValueChange = { updateField { copy(password = it)}},
        label = { Text("Password")},
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {

            val icon = if (showPassword) {
                R.drawable.ic_hide_password
            } else {
                R.drawable.ic_show_password
            }
            Icon(
                painter = androidx.compose.ui.res.painterResource(icon),
                contentDescription = null,
                modifier = Modifier.clickable {
                    showPassword = !showPassword
                }
            )
        },
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = login,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Login")
    }
}