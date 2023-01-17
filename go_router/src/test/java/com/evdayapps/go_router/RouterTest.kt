package com.evdayapps.go_router

import com.evdayapps.go_router.models.Route
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class RouterTest {

    lateinit var route: Route
    lateinit var router: Router

    @Before
    fun setup() {
        route = Route(
            path = "",
            subroutes = listOf(
                Route(
                    path = "events",
                    name = "event-list",
                    subroutes = listOf(
                        Route(
                            path = "(?<slug>[\\w]+)",
                            name = "event-detail",
                            subroutes = listOf(
                                Route(
                                    path = "report",
                                    name = "event-report",
                                )
                            )
                        ),
                    )
                ),
            )
        )

        router = Router(baseRoute = route, enableDebugging = true)
    }

    @Test
    fun `Test event details route (with(out) trailing slash) match with slug extraction`() {
        listOf(
            "/events/test",
            "/events/test/"
        ).forEach {
            val matchResult = router.getMatch(it)
            assertNotNull(matchResult)
            assertEquals("event-detail", matchResult?.route?.name)

            // Test path params extraction
            assertEquals("test", matchResult?.pathParams!!["slug"])
        }
    }

    @Test
    fun `Test event report route match`() {
        val path = "/events/test/report"
        val matchResult = router.getMatch(path)
        assertNotNull(matchResult)
        assertEquals("event-report", matchResult?.route?.name)

        // Test path params extraction
        assertEquals("test", matchResult?.pathParams!!["slug"])
    }

    @Test
    fun `Test event details route with query params`() {
        listOf(
            "/events/test",
            "/events/test/"
        ).forEach {
            val matchResult = router.getMatch("$it?action=none&timestamp=001")
            assertNotNull(matchResult)
            assertEquals("event-detail", matchResult?.route?.name)

            // Test path params extraction
            assertEquals("test", matchResult?.pathParams!!["slug"])

            // Test query params extraction
            assertEquals("none", matchResult.queryParams["action"])
            assertEquals("001", matchResult.queryParams["timestamp"])
        }
    }

}