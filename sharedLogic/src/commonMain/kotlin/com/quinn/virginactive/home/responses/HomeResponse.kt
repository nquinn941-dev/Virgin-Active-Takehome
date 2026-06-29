package com.quinn.virginactive.home.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class HomeResponse(
    val blocks: List<HomeBlock>
)

@Serializable
sealed class HomeBlock {
    abstract val type: String
}

@Serializable
@SerialName("greeting")
data class GreetingBlock(
    override val type: String,
    val title: String
) : HomeBlock()

@Serializable
@SerialName("hero")
data class HeroBlock(
    override val type: String,
    val title: String,
    val subtitle: String
) : HomeBlock()

@Serializable
@SerialName("myClub")
data class MyClubBlock(
    override val type: String,
    val name: String,
    val addressLine: String,
    val openingHoursToday: String,
    val phoneNumber: String
) : HomeBlock()

@Serializable
@SerialName("classCarousel")
data class ClassCarouselBlock(
    override val type: String,
    val title: String,
    val viewAllAction: Action,
    val items: List<ClassCarouselItem>
) : HomeBlock()

@Serializable
@SerialName("myRewards")
data class MyRewardsBlock(
    override val type: String,
    val title: String,
    val items: List<CardItem>
) : HomeBlock()

@Serializable
@SerialName("myGoals")
data class MyGoalsBlock(
    override val type: String,
    val title: String,
    val items: List<CardItem>
) : HomeBlock()

@Serializable
@SerialName("promotion")
data class PromotionBlock(
    override val type: String,
    val title: String,
    val subtitle: String,
    val imageRef: String
) : HomeBlock()

@Serializable
@SerialName("experimental")
data class ExperimentalBlock(
    override val type: String,
    val payload: JsonObject
) : HomeBlock()

@Serializable
data class Action(
    val type: String,
    val clubId: String? = null,
    val classId: String? = null
)

@Serializable
data class ClassCarouselItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageRef: String,
    val badge: String? = null,
    val startsAt: String,
    val actionLabel: String,
    val actionRef: Action
)

@Serializable
data class CardItem(
    val id: String,
    val title: String,
    val subtitle: String?,
    val imageRef: String?,
    val badge: String? = null
)