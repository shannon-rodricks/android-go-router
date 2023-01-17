package com.evdayapps.go_router

import android.content.Context
import com.evdayapps.go_router.models.MatchRoute
import com.evdayapps.go_router.models.Route

class Router(
    baseRoute: Route,
    private val enableDebugging: Boolean = false
) {

    private val routes: List<MatchRoute>

    init {
        routes = generateRoutes(baseRoute, "")
        if(enableDebugging) {
            for(route in routes) {
                println(route.path)
            }
        }
    }

    private fun generateRoutes(route: Route, currentPath: String): List<MatchRoute> {
        val list = mutableListOf<MatchRoute>()
        list.add(MatchRoute(route = route, path = currentPath + route.path))
        for (subroute in route.subroutes) {
            list.addAll(generateRoutes(route = subroute, currentPath = currentPath + route.path))
        }

        return list
    }

    fun getMatch(path : String) : MatchRoute? {
        for(route in routes) {
            if(route.matches(path)) {
                return route
            }
        }

        return null
    }

    fun push(context: Context, url: String, arguments: Map<String, Any>) {
        // TODO
    }

}