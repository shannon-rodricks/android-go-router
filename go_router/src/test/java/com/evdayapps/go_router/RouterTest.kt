package com.evdayapps.go_router

import com.evdayapps.go_router.models.Route
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class RouterTest {

    lateinit var route : Route
    lateinit var router : Router

    @Before
    fun setup() {
        route = Route(
            path = "/",
            subroutes = listOf(
                Route(
                    path = "events/",
                    name = "event-list",
                    subroutes = listOf(
                        Route(
                            path = "(?<slug>[\\w]+)/",
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
            val input = router.getMatch(it)
            assertNotNull(input)
            assertEquals("event-detail", input?.route?.name)

            // Test path params extraction
            val params = input?.pathParams(it)
            assertEquals("test", params!!["slug"])
        }
    }

    @Test
    fun `Test event report route match`() {
        val path = "/events/test/report"
        val input = router.getMatch(path)
        assertNotNull(input)
        assertEquals("event-report", input?.route?.name)

        // Test path params extraction
        val params = input?.pathParams(path)
        assertEquals("test", params!!["slug"])
    }

}