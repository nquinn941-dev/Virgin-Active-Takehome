package com.quinn.virginactive.home.usecases

import com.quinn.virginactive.home.HomeApi
import com.quinn.virginactive.home.HomeViewData
import com.quinn.virginactive.home.mappers.HomeViewDataMapper

class GetHomeViewDataUseCase internal constructor(
    val homeApi: HomeApi,
    val homeViewDataMapper: HomeViewDataMapper
) {

    suspend fun getHomeViewData(): HomeViewData {
        val response = homeApi.getHomeManifest()
        return homeViewDataMapper.mapToHomeViewData(response)
    }
}