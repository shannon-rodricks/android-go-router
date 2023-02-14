package com.evdayapps.go_router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.evdayapps.go_router.models.MatchResult
import com.evdayapps.go_router.models.MatchRoute
import com.evdayapps.go_router.models.Route

/**
 * Main routing class
 *
 */
class Router(
    baseRoute: Route,
    private val enableDebugging: Boolean = false
) {

    private val routes: List<MatchRoute>

    init {
        routes = generateMatchRoutes(route = baseRoute, currentPath = "", depth = 0)

        // Print list of routes, generated
        if (enableDebugging) {
            routes.forEach { e -> println("  ".repeat(e.depth) + e) }
        }
    }

    /**
     * Recurses thru a route and generates all routes
     * @return list of [MatchRoute] which is
     */
    private fun generateMatchRoutes(
        route: Route,
        currentPath: String,
        depth: Int
    ): List<MatchRoute> {
        val list = mutableListOf<MatchRoute>()

        val newPath = if (currentPath != "/") "$currentPath/${route.path}" else "/${route.path}"
        val matchRoute = MatchRoute(route = route, path = newPath, depth = depth)
        list.add(matchRoute)

        for (subroute in route.subroutes) {
            val subroutes = generateMatchRoutes(
                route = subroute,
                currentPath = newPath,
                depth = depth + 1
            )
            subroutes.forEach { e -> if (e.parentRoute == null) e.parentRoute = matchRoute }
            list += subroutes
        }

        return list
    }

    fun getMatch(path: String): MatchResult? {
        if (enableDebugging) {
            println("Processing: $path")
        }

        // Return the first match found in the list
        // This logic might need to be revisited to handle the exact match condition
        for (route in routes) {
            val matchResult = route.matches(path)
            if (matchResult != null) {
                if (enableDebugging) {
                    println("Match: $matchResult")
                }
                return matchResult
            }
        }

        return null
    }

    /**
     * Load a new screen, defined by [url]
     * [context] is the activity context ideally, but could be the application context
     * [arguments] any extra data that needs to be sent to the new screen
     *
     * @return true if successful, else false
     */
    fun push(context: Context, url: String, arguments: Map<String, Any>?): Boolean {
        val intent = getIntent(context, url, arguments)
        if (intent != null) {
            if (context is Activity) {
                context.startActivity(intent)
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            return true
        }

        return false
    }

    /**
     * Find a match for [url] and generate the appropriate [Intent]
     * @return Intent object or null
     */
    fun getIntent(context: Context, url: String, bootstrapData: Map<String, Any>?): Intent? {
        val match = getMatch(url)
        if (match != null) {
            if (match.intentBuilder != null) {
                val intent = match.intentBuilder.invoke(
                    context,
                    match.pathParams,
                    match.queryParams,
                    bootstrapData
                )

                // Set uri
                intent.data = Uri.parse(url)

                // Set bootstrap data
                if (bootstrapData != null) {
                    intent.putExtra("data", bootstrapData as java.io.Serializable)
                }

                return intent
            }
        }

        return null
    }


}