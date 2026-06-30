package com.quinn.virginactive.home.mappers

import com.quinn.virginactive.home.CarouselItemViewData
import com.quinn.virginactive.home.ClassCarouselItemViewData
import com.quinn.virginactive.home.GreetingItemViewData
import com.quinn.virginactive.home.HeroItemViewData
import com.quinn.virginactive.home.HomeViewData
import com.quinn.virginactive.home.MyClubItemViewData
import com.quinn.virginactive.home.MyGoalsItemViewData
import com.quinn.virginactive.home.MyRewardsItemViewData
import com.quinn.virginactive.home.PromotionItemViewData
import com.quinn.virginactive.home.responses.ClassCarouselBlock
import com.quinn.virginactive.home.responses.ExperimentalBlock
import com.quinn.virginactive.home.responses.GreetingBlock
import com.quinn.virginactive.home.responses.HeroBlock
import com.quinn.virginactive.home.responses.HomeResponse
import com.quinn.virginactive.home.responses.MyClubBlock
import com.quinn.virginactive.home.responses.MyGoalsBlock
import com.quinn.virginactive.home.responses.MyRewardsBlock
import com.quinn.virginactive.home.responses.PromotionBlock

class HomeViewDataMapper internal constructor() {

    fun mapToHomeViewData(response: HomeResponse): HomeViewData {
        return HomeViewData(
            items = response.blocks.mapNotNull { block ->
                return@mapNotNull when (block) {
                    is GreetingBlock -> GreetingItemViewData(
                        title = block.title
                    )

                    is HeroBlock -> HeroItemViewData(
                        title = block.title,
                        subtitle = block.subtitle
                    )

                    is MyClubBlock -> MyClubItemViewData(
                        name = block.name,
                        addressLine = block.addressLine,
                        openingHoursToday = block.openingHoursToday,
                        phoneNumber = block.phoneNumber
                    )

                    is ClassCarouselBlock -> ClassCarouselItemViewData(
                        title = block.title,
                        clubId = block.viewAllAction.clubId,
                        items = block.items.map { item ->
                            CarouselItemViewData(
                                id = item.id,
                                title = item.title,
                                subtitle = item.subtitle,
                                imageRef = item.imageRef,
                                badge = item.badge,
                                startTime = item.startsAt.convertToReadableDate(),
                                actionLabel = item.actionLabel,
                                actionType = CarouselItemViewData.HomeViewDataActionType(
                                    type = item.actionRef.type,
                                    clubId = item.actionRef.clubId,
                                    classId = item.actionRef.classId
                                )
                            )
                        }
                    )

                    is MyRewardsBlock -> MyRewardsItemViewData(
                        title = block.title,
                        items = block.items.map { item ->
                            CarouselItemViewData(
                                id = item.id,
                                title = item.title,
                                subtitle = item.subtitle.orEmpty(),
                                imageRef = item.imageRef,
                                badge = item.badge,
                                startTime = null,
                                actionLabel = null,
                                actionType = null
                            )
                        }
                    )

                    is PromotionBlock -> PromotionItemViewData(
                        title = block.title,
                        subtitle = block.subtitle,
                        imageRef = block.imageRef
                    )

                    is MyGoalsBlock -> MyGoalsItemViewData(
                        title = block.title,
                        items = block.items.map { item ->
                            CarouselItemViewData(
                                id = item.id,
                                title = item.title,
                                subtitle = item.subtitle.orEmpty(),
                                imageRef = item.imageRef,
                                badge = item.badge,
                                startTime = null,
                                actionLabel = null,
                                actionType = null
                            )
                        }
                    )
                    else -> null
                }
            }
        )
    }
}

private fun String.convertToReadableDate() : String {
    val date = this.split("T").first()
    val time = this.substringAfter("T").substringBeforeLast("+")
    val (_, month, day) = date.split("-")
    return "$day/$month - $time"
}