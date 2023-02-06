package com.evdayapps.go_router.models

/**
 * Class encapsulating the data for a successful match
 *
 * @property mRoute The defined route
 * @property pathParams The extracted path params from the input
 * @property queryParams The extracted query params from the input
 */
class MatchResult(
    val mRoute: MatchRoute,
    val pathParams: Map<String, String>,
    val queryParams: Map<String, String>
) {
    val intentBuilder = mRoute.route.intentBuilder

    val fragmentBuilder = mRoute.route.fragmentBuilder

    override fun toString(): String {
        return "MatchResult(\n" +
            "\tname: ${mRoute.route.name}\n" +
            "\tpath: ${mRoute.path}\n" +
            "\tpathParams: $pathParams\n" +
            "\tqueryParams: $queryParams\n" +
            ")"
    }
}