package com.quinn.virginactive.home

data class HomeViewData(
    val items: List<HomeItemViewData>
)

interface HomeItemViewData

data class GreetingItemViewData(
    val title: String
) : HomeItemViewData

data class HeroItemViewData(
    val title: String,
    val subtitle: String
) : HomeItemViewData

data class MyClubItemViewData(
    val name: String,
    val addressLine: String,
    val openingHoursToday: String,
    val phoneNumber: String
) : HomeItemViewData

data class ClassCarouselItemViewData(
    val title: String,
    val clubId: String?,
    val items: List<CarouselItemViewData>
) : HomeItemViewData

data class MyRewardsItemViewData(
    val title: String,
    val items: List<CarouselItemViewData>
) : HomeItemViewData

data class PromotionItemViewData(
    val title: String,
    val subtitle: String,
    val imageRef: String
) : HomeItemViewData

data class MyGoalsItemViewData(
    val title: String,
    val items: List<CarouselItemViewData>
) : HomeItemViewData

data class CarouselItemViewData(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageRef: String?,
    val badge: String?,
    val startTime: String?,
    val actionLabel: String?,
    val actionType: HomeViewDataActionType?
) {
    data class HomeViewDataActionType(
        val type: String,
        val clubId: String? = null,
        val classId: String? = null
    )
}