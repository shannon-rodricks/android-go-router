package com.evdayapps.go_router

import android.content.Intent
import org.junit.Test

class MatchRouteTest {

    @Test
    fun testRegexExtraction() {
        var route = Route(
            path = "/items/:id",
        )
        var match = MatchRoute(route)
        assert(match.)
    }

}