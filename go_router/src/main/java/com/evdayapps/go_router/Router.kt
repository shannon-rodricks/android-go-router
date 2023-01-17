package com.evdayapps.go_router

import android.content.Context
import com.evdayapps.go_router.models.MatchResult
import com.evdayapps.go_router.models.MatchRoute
import com.evdayapps.go_router.models.Route

class Router(
    baseRoute: Route,
    private val enableDebugging: Boolean = false
) {

    private val routes: List<MatchRoute>

    init {
        routes = generateMatchRoutes(baseRoute, "")
        if(enableDebugging) {
            for(route in routes) {
                println(route.path)
            }
        }
    }

    private fun generateMatchRoutes(route: Route, currentPath: String): List<MatchRoute> {
        val list = mutableListOf<MatchRoute>()
        val newPath = if(currentPath != "/") "$currentPath/${route.path}" else "/${route.path}"
        list.add(MatchRoute(route = route, path = newPath))
        for (subroute in route.subroutes) {
            list += generateMatchRoutes(route = subroute, currentPath = newPath)
        }

        return list
    }

    fun getMatch(path : String) : MatchResult? {
        for(route in routes) {
            val matchResult = route.matches(path)
            if(matchResult != null) {
                return matchResult
            }
        }

        return null
    }

    fun push(context: Context, url: String, arguments: Map<String, Any>) {
        // TODO
    }

}