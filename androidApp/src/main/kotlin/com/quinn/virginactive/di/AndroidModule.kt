package com.quinn.virginactive.di

import com.quinn.virginactive.home.HomeViewModel
import com.quinn.virginactive.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun androidModule() = module {

    viewModel {
        LoginViewModel(
            loginUseCase = get(),
            userManager = get()
        )
    }

    viewModel {
        HomeViewModel(
            getHomeViewDataUseCase = get()
        )
    }
}