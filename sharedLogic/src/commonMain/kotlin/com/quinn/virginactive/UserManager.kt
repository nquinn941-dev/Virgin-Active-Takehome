package com.quinn.virginactive

import com.quinn.virginactive.auth.AuthApi
import com.quinn.virginactive.auth.request.LoginRequest
import com.quinn.virginactive.user.ProfileApi
import com.quinn.virginactive.user.User
import com.quinn.virginactive.user.UserMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class NotLoggedInException : Exception()

sealed class UserState {
    object Loading : UserState()
    data class LoggedIn(val user: User) : UserState()
    object LoggedOut : UserState()
}

interface UserRepo {
    fun observeUserState() : StateFlow<UserState>

    suspend fun login(request: LoginRequest)

    fun getUserStateSnapshot() : UserState

    suspend fun logout()
}

class UserManager internal constructor(
    private val authApi: AuthApi,
    private val coroutineScope: CoroutineScope,
    private val tokenStore: TokenStore,
    private val userMapper: UserMapper,
    private val profileApi: ProfileApi
): UserRepo {

    private val userState: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)

    init {
        getProfileOnLogin()
    }

    override suspend fun login(request: LoginRequest) {
        userState.value = try {
            val response = authApi.login(request)
            val expiryTimeInstant = Clock.System.now().plus(response.expiresIn.seconds)

            val token = AuthToken(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                expiry = expiryTimeInstant
            )
            tokenStore.saveToken(token)
            val user = userMapper.mapToUser(response = response.user)
            UserState.LoggedIn(user)
        } catch (ex: Exception) {
            ex.printStackTrace()
            UserState.LoggedOut
        }
    }

    override fun observeUserState(): StateFlow<UserState> = userState.asStateFlow()

    private fun getProfileOnLogin() {
        coroutineScope.launch {
            userState.value = try {
                val profileResponse = profileApi.getProfile()
                val user = userMapper.mapToUser(profileResponse)
                UserState.LoggedIn(user)
            } catch (ex: Exception) {
                ex.printStackTrace()
                UserState.LoggedOut
            }

        }
    }

    override fun getUserStateSnapshot(): UserState {
        return userState.value
    }

    @Throws(NotLoggedInException::class)
    fun getLoggedInUser() : User = (userState.value as? UserState.LoggedIn)?.user ?: throw NotLoggedInException()

    override suspend fun logout() {
        userState.value = UserState.LoggedOut
        tokenStore.clearToken()
    }
}