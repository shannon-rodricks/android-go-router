package com.evdayapps.go_router.models

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

typealias IntentBuilder = (
    context: Context,
    pathParams: Map<String, String>,
    queryParams: Map<String, String>,
    arguments: Map<String, String>
) -> Intent

typealias FragmentBuilder = (
    context: Context,
    pathParams: Map<String, String>,
    queryParams: Map<String, String>,
    arguments: Map<String, String>
) -> Fragment?

class Route(
    var path: String,
    var name: String? = null,
    var optionalTrailingSlash: Boolean = true,
    var intentBuilder: IntentBuilder? = null,
    var fragmentBuilder: FragmentBuilder? = null,
    var subroutes: List<Route> = emptyList()
) {
    init {
        assert(path.endsWith("/") || subroutes.isEmpty(), lazyMessage = {
            "Path `$path` (name: $name) should end with /"
        })
    }
}