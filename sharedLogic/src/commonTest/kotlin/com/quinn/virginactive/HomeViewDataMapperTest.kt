package com.quinn.virginactive

import com.quinn.virginactive.home.mappers.HomeViewDataMapper
import com.quinn.virginactive.home.responses.Action
import com.quinn.virginactive.home.responses.ClassCarouselBlock
import com.quinn.virginactive.home.responses.ClassCarouselItem
import com.quinn.virginactive.home.responses.GreetingBlock
import com.quinn.virginactive.home.responses.HeroBlock
import com.quinn.virginactive.home.responses.HomeResponse
import com.quinn.virginactive.home.responses.MyClubBlock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewDataMapperTest {

    private val mapper = HomeViewDataMapper()


//    @Test
//    fun `maps full response`() = runTest {
//        val greetingBlock = GreetingBlock(
//            type = "greeting",
//            title = "Good afternoon, Avid"
//        )
//
//        val heroBlock = HeroBlock(
//            type = "hero",
//            title = "Crush your goals this week",
//            subtitle = "You've attended 3 classes — 1 more to hit your weekly target."
//        )
//
//        val myClubBlock = MyClubBlock(
//            type = "myClub",
//            name = "Virgin Active Sea Point",
//            addressLine = "96 Beach Rd, Sea Point, Cape Town, 8005",
//            openingHoursToday = "05:00 – 22:00",
//            phoneNumber = "+27 21 439 1240"
//        )
//
//        val classCarouselBlock = ClassCarouselBlock(
//            type = "classCarousel",
//            title = "Your week",
//            viewAllAction = Action(
//                type = "openTimetable",
//                clubId = "club_sea_point"
//            ),
//            items = listOf(
//                ClassCarouselItem(
//                    id = "sp-power-spin::2026-07-13",
//                    title = "Power Spin",
//                    subtitle = "",
//                    imageRef = "",
//                    badge = TODO(),
//                    startsAt = TODO(),
//                    actionLabel = TODO(),
//                    actionRef = TODO()
//                )
//            )
//        )
//    }

    @Test
    fun `maps empty response`() = runTest {
        val response = HomeResponse(blocks = emptyList())

        val actual = mapper.mapToHomeViewData(response)

        assertEquals(0, actual.items.size)
    }
}