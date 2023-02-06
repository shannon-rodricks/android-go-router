package com.evdayapps.go_router.models

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

typealias IntentBuilder = (
    context: Context,
    pathParams: Map<String, String>?,
    queryParams: Map<String, String>?,
    arguments: Map<String, Any>?
) -> Intent

typealias FragmentBuilder = (
    context: Context,
    pathParams: Map<String, String>,
    queryParams: Map<String, String>,
    arguments: Map<String, Any>
) -> Fragment?

/**
 * Raw route class, for definition
 * This class is processed by [Router] to generate entire paths
 *
 * @property path The path for the route
 * @property name An optional name for the route
 * @property appendTrailingSlash
 * @property intentBuilder An intent builder for building an activity intent
 * @property fragmentBuilder A builder for building a fragment
 * @property subroutes List of subroutes under this route (this route's path will be prefixed to their path)
 *
 * TODO Redirection logic (per route and global)
 */
class Route(
    var path: String,
    var name: String? = null,
    var appendTrailingSlash: Boolean = true,
    var intentBuilder: IntentBuilder? = null,
    var fragmentBuilder: FragmentBuilder? = null,
    var subroutes: List<Route> = emptyList()
) {
    init {
        assert(!path.startsWith("/")) { "$path should not start with /" }
        assert(!path.endsWith("/")) { "$path should not end with /" }
    }
}