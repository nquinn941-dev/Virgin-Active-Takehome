package com.quinn.virginactive.di

import com.quinn.virginactive.TokenStore
import com.quinn.virginactive.UserManager
import com.quinn.virginactive.auth.AuthApi
import com.quinn.virginactive.auth.KtorAuthApi
import com.quinn.virginactive.auth.usecases.LoginUseCase
import com.quinn.virginactive.home.HomeApi
import com.quinn.virginactive.home.KtorHomeApi
import com.quinn.virginactive.home.mappers.HomeViewDataMapper
import com.quinn.virginactive.home.usecases.GetHomeViewDataUseCase
import com.quinn.virginactive.home.usecases.GetDirectionsUseCase
import com.quinn.virginactive.timetable.ClassRepository
import com.quinn.virginactive.timetable.mappers.ClassListViewDataMapper
import com.quinn.virginactive.timetable.networking.ClassApi
import com.quinn.virginactive.timetable.networking.KtorClassApi
import com.quinn.virginactive.timetable.usecases.BookClassUseCase
import com.quinn.virginactive.timetable.usecases.CancelBookingUseCase
import com.quinn.virginactive.timetable.usecases.GetClassViewDataUseCase
import com.quinn.virginactive.timetable.usecases.GetClassesViewDataUseCase
import com.quinn.virginactive.user.KtorProfileApi
import com.quinn.virginactive.user.ProfileApi
import com.quinn.virginactive.user.UserMapper
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


fun sharedModule() = module {

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }.bind<Json>()

    single(named("authClient")) {
        HttpClient {
            expectSuccess = true
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }

            install(ContentNegotiation) {
                json(get())
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }
        }
    }

    single {
        KtorAuthApi(
            httpClient = get(named("authClient"))
        )
    }.bind<AuthApi>()

    single {
        TokenStore(
            platformDeviceIO = get(),
            authApi = get()
        )
    }.bind<TokenStore>()

    single {
        val tokenStore : TokenStore = get()

        HttpClient {
            expectSuccess = true
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }

            install(ContentNegotiation) {
                json(get())
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = tokenStore.getToken() ?: return@loadTokens null
                        BearerTokens(
                            accessToken = token.accessToken,
                            refreshToken = token.refreshToken
                        )
                    }

                    refreshTokens {
                        val token = tokenStore.refresh() ?: return@refreshTokens null
                        BearerTokens(
                            accessToken = token.accessToken,
                            refreshToken = token.refreshToken
                        )
                    }
                }
            }
        }
    }

    single {
        KtorProfileApi(
            httpClient = get()
        )
    }.bind<ProfileApi>()

    single(createdAtStart = true) {
        UserManager(
            authApi = get(),
            coroutineScope = CoroutineScope(context = Dispatchers.IO),
            tokenStore = get(),
            userMapper = UserMapper(),
            profileApi = get()
        )
    }.bind<UserManager>()

    factory {
        LoginUseCase(
            userManager = get()
        )
    }

    single {
        KtorHomeApi(
            httpClient = get()
        )
    }.bind<HomeApi>()

    factory {
        GetHomeViewDataUseCase(
            homeApi = get(),
            homeViewDataMapper = HomeViewDataMapper()
        )
    }

    single {
        KtorClassApi(
            httpClient = get()
        )
    }.bind<ClassApi>()

    single {
        ClassRepository(
            classApi = get(),
            classListViewDataMapper = ClassListViewDataMapper(),
            userManager = get()
        )
    }

    factory {
        GetClassesViewDataUseCase(
            classRepository = get()
        )
    }

    factory {
        GetClassViewDataUseCase(
            classRepository = get()
        )
    }

    factory {
        BookClassUseCase(
            classRepository = get()
        )
    }

    factory {
        CancelBookingUseCase(
            classRepository = get()
        )
    }

    factory {
        GetDirectionsUseCase(
            platformDirectionProvider = get()
        )
    }

}