package com.quinn.virginactive.auth.usecases

import com.quinn.virginactive.UserManager
import com.quinn.virginactive.auth.request.LoginRequest

class LoginUseCase internal constructor(
    private val userManager: UserManager
) {

    suspend fun login(username: String, password: String) {
        val request = LoginRequest(
            username = username,
            password = password
        )
        userManager.login(request = request)
    }
}