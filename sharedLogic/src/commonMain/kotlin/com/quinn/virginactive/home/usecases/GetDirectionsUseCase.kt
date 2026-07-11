package com.quinn.virginactive.home.usecases

import com.quinn.virginactive.PlatformDirectionProvider

class GetDirectionsUseCase internal constructor(
    val platformDirectionProvider: PlatformDirectionProvider
) {
    fun getDirections(address: String) {
        platformDirectionProvider.getDirections(address)
    }
}