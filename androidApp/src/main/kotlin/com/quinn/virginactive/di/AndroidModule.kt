package com.quinn.virginactive.di

import com.quinn.virginactive.classdetails.ClassDetailsViewModel
import com.quinn.virginactive.home.HomeViewModel
import com.quinn.virginactive.login.LoginViewModel
import com.quinn.virginactive.timetable.TimetableViewModel
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
            getHomeViewDataUseCase = get(),
            getDirectionsUseCase = get()
        )
    }

    viewModel {
        TimetableViewModel(
            getClassesViewDataUseCase = get(),
            userManager = get()
        )
    }

    viewModel {
        ClassDetailsViewModel(
            getClassViewDataUseCase = get(),
            bookClassUseCase = get(),
            cancelBookingUseCase = get(),
            localClassReminder = get()
        )
    }
}