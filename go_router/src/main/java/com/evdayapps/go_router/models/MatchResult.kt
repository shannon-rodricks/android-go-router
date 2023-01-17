package com.evdayapps.go_router.models

/**
 * Class encapsulating the data for a successful match
 * @property route The defined route
 * @property pathParams The extracted path params from the input
 * @property queryParams The extracted query params from the input
 */
class MatchResult(
    val route : Route,
    val pathParams : Map<String, Any>,
    val queryParams : Map<String, Any>
)