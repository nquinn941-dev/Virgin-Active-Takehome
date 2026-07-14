package com.quinn.virginactive

import com.quinn.virginactive.home.HomeApi
import com.quinn.virginactive.home.KtorHomeApi
import com.quinn.virginactive.home.responses.ClassCarouselBlock
import com.quinn.virginactive.home.responses.ExperimentalBlock
import com.quinn.virginactive.home.responses.GreetingBlock
import com.quinn.virginactive.home.responses.HeroBlock
import com.quinn.virginactive.home.responses.MyClubBlock
import com.quinn.virginactive.home.responses.MyGoalsBlock
import com.quinn.virginactive.home.responses.MyRewardsBlock
import com.quinn.virginactive.home.responses.PromotionBlock
import com.quinn.virginactive.home.responses.UnknownBlock
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class KtorHomeApiTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    private fun buildApi(engine: MockEngine): HomeApi {
        val client = HttpClient(engine) {
            install(ContentNegotiation) { json(json) }
        }
        return KtorHomeApi(client)
    }

    // ---------- Happy path: current response ----------

    @Test
    fun `deserializes full manifest with all block types`() = runTest {
        val responseJson = """
            {
    "blocks": [
        {
            "type": "greeting",
            "title": "Good afternoon, Avid"
        },
        {
            "type": "hero",
            "title": "Crush your goals this week",
            "subtitle": "You've attended 3 classes — 1 more to hit your weekly target."
        },
        {
            "type": "myClub",
            "name": "Virgin Active Sea Point",
            "addressLine": "96 Beach Rd, Sea Point, Cape Town, 8005",
            "openingHoursToday": "05:00 – 22:00",
            "phoneNumber": "+27 21 439 1240"
        },
        {
            "type": "classCarousel",
            "title": "Your week",
            "viewAllAction": {
                "type": "openTimetable",
                "clubId": "club_sea_point"
            },
            "items": [
                {
                    "id": "sp-power-spin::2026-07-13",
                    "title": "Power Spin",
                    "subtitle": "Thabo Ndlovu",
                    "imageRef": "spin",
                    "badge": "Booked",
                    "startsAt": "2026-07-13T06:00:00+02:00",
                    "actionLabel": "View",
                    "actionRef": {
                        "type": "openClass",
                        "clubId": "club_sea_point",
                        "classId": "sp-power-spin::2026-07-13"
                    }
                },
                {
                    "id": "sp-sunrise-yoga::2026-07-13",
                    "title": "Sunrise Yoga",
                    "subtitle": "Lerato Molefe",
                    "imageRef": "yoga",
                    "badge": "Booked",
                    "startsAt": "2026-07-13T07:00:00+02:00",
                    "actionLabel": "View",
                    "actionRef": {
                        "type": "openClass",
                        "clubId": "club_sea_point",
                        "classId": "sp-sunrise-yoga::2026-07-13"
                    }
                },
                {
                    "id": "sp-hiit-lab::2026-07-13",
                    "title": "HIIT Lab",
                    "subtitle": "Sipho Dlamini",
                    "imageRef": "hiit",
                    "badge": "Waitlisted",
                    "startsAt": "2026-07-13T17:30:00+02:00",
                    "actionLabel": "View",
                    "actionRef": {
                        "type": "openClass",
                        "clubId": "club_sea_point",
                        "classId": "sp-hiit-lab::2026-07-13"
                    }
                },
                {
                    "id": "sp-lunchtime-spin::2026-07-15",
                    "title": "Lunchtime Spin",
                    "subtitle": "Thabo Ndlovu",
                    "imageRef": "spin",
                    "startsAt": "2026-07-15T12:00:00+02:00",
                    "actionLabel": "View",
                    "actionRef": {
                        "type": "openClass",
                        "clubId": "club_sea_point",
                        "classId": "sp-lunchtime-spin::2026-07-15"
                    }
                },
                {
                    "id": "sp-evening-flow::2026-07-15",
                    "title": "Evening Flow Yoga",
                    "subtitle": "Lerato Molefe",
                    "imageRef": "yoga",
                    "startsAt": "2026-07-15T17:00:00+02:00",
                    "actionLabel": "View",
                    "actionRef": {
                        "type": "openClass",
                        "clubId": "club_sea_point",
                        "classId": "sp-evening-flow::2026-07-15"
                    }
                },
                {
                    "id": "sp-total-body-hiit::2026-07-16",
                    "title": "Total Body HIIT",
                    "subtitle": "Sipho Dlamini",
                    "imageRef": "hiit",
                    "startsAt": "2026-07-16T06:00:00+02:00",
                    "actionLabel": "View",
                    "actionRef": {
                        "type": "openClass",
                        "clubId": "club_sea_point",
                        "classId": "sp-total-body-hiit::2026-07-16"
                    }
                }
            ]
        },
        {
            "type": "myRewards",
            "title": "Your rewards",
            "items": [
                {
                    "id": "reward_001",
                    "title": "Free smoothie at the juice bar",
                    "subtitle": "Show this at any Virgin Active café",
                    "imageRef": "reward_smoothie",
                    "badge": "Expires Sunday"
                },
                {
                    "id": "reward_002",
                    "title": "20% off retail at any club",
                    "subtitle": "Valid on apparel and accessories",
                    "imageRef": "reward_retail"
                },
                {
                    "id": "reward_003",
                    "title": "Bring a friend free this month",
                    "subtitle": "One guest pass, any location",
                    "imageRef": "reward_guest",
                    "badge": "Limited time"
                }
            ]
        },
        {
            "type": "myGoals",
            "title": "Your goals",
            "items": [
                {
                    "id": "goal_001",
                    "title": "Attend 4 classes per week",
                    "subtitle": "3 of 4 this week",
                    "imageRef": "goal_classes"
                },
                {
                    "id": "goal_002",
                    "title": "Complete 10 spin classes this month",
                    "subtitle": "7 of 10 completed",
                    "imageRef": "goal_spin"
                },
                {
                    "id": "goal_003",
                    "title": "Try a new class type",
                    "subtitle": "You haven't tried Pilates yet!",
                    "imageRef": "goal_explore"
                }
            ]
        },
        {
            "type": "promotion",
            "title": "Summer Shape-Up Challenge",
            "subtitle": "Join 12 classes in 4 weeks and earn bonus rewards. Starts 1 June.",
            "imageRef": "promo_summer_challenge"
        },
        {
            "type": "experimental",
            "payload": {
                "kind": "futureFeature",
                "title": "Coming soon",
                "data": {
                    "foo": "bar"
                }
            }
        }
    ]
}
        """.trimIndent()

        val engine = MockEngine { respondJson(responseJson) }
        val api = buildApi(engine)

        val result = api.getHomeManifest()

        assertEquals(8, result.blocks.size)
        assertIs<GreetingBlock>(result.blocks[0])
        assertIs<HeroBlock>(result.blocks[1])
        assertIs<MyClubBlock>(result.blocks[2])
        assertIs<ClassCarouselBlock>(result.blocks[3])
        assertIs<MyRewardsBlock>(result.blocks[4])
        assertIs<MyGoalsBlock>(result.blocks[5])
        assertIs<PromotionBlock>(result.blocks[6])
    }

    // ---------- Optional / nullable fields ----------

    @Test
    fun `CardItem with null optional fields deserializes correctly`() = runTest {
        val responseJson = """
            {
              "blocks": [
                {
                  "type": "myGoals",
                  "title": "Your goals",
                  "items": [
                    { "id": "g1", "title": "Run 5k", "subtitle": null, "imageRef": null }
                  ]
                }
              ]
            }
        """.trimIndent()

        val api = buildApi(MockEngine { respondJson(responseJson) })
        val result = api.getHomeManifest()

        val block = result.blocks.single() as MyGoalsBlock
        assertNull(block.items.single().subtitle)
        assertNull(block.items.single().badge)
    }

    // ---------- Empty state ----------

    @Test
    fun `empty blocks list deserializes to empty list, not error`() = runTest {
        val api = buildApi(MockEngine { respondJson("""{"blocks": []}""") })
        val result = api.getHomeManifest()
        assertTrue(result.blocks.isEmpty())
    }

    // ---------- Unknown block type — the important one ----------

    @Test
    fun `unknown block type does not crash entire manifest parse`() = runTest {
        val responseJson = """
            {
              "blocks": [
                { "type": "greeting", "title": "Hi" },
                { "type": "weatherWidget", "someNewField": "value" }
              ]
            }
        """.trimIndent()

        val api = buildApi(MockEngine { respondJson(responseJson) })

        val result = api.getHomeManifest()
        assertEquals(2, result.blocks.size)
        assertIs<GreetingBlock>(result.blocks[0])

        val unknown = assertIs<UnknownBlock>(result.blocks[1])
        assertEquals("weatherWidget", unknown.type)

        assertIs<UnknownBlock>(result.blocks[1])
    }

    // ---------- Malformed JSON ----------

    @Test
    fun `malformed json throws error`() = runTest {
        val api = buildApi(MockEngine { respondJson("""{ "blocks": [ { "type": """) })
        assertFailsWith<JsonConvertException> { api.getHomeManifest() }
    }

    // ---------- HTTP error statuses ----------

    @Test
    fun `404 response throws error`() = runTest {
        val engine = MockEngine { respond("", HttpStatusCode.NotFound) }
        val api = buildApi(engine)
        assertFailsWith<ClientRequestException> { api.getHomeManifest() }
    }

    @Test
    fun `500 response throws ServerResponseException`() = runTest {
        val engine = MockEngine { respond("", HttpStatusCode.InternalServerError) }
        val api = buildApi(engine)
        assertFailsWith<ServerResponseException> { api.getHomeManifest() }
    }

    private fun MockRequestHandleScope.respondJson(content: String) = respond(
        content = content,
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}