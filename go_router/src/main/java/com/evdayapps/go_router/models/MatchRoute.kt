package com.evdayapps.go_router.models

import java.util.regex.Pattern

// Pattern used to extract group names from a route path
// which are then used to generate the pathParams map
const val namedGroupPattern = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>"

/**
 * Processed [Route]
 * Responsible for the actual matching
 * Also responsible for generating the Mat
 */
class MatchRoute(
    val path : String,
    val route: Route
) {

    private val pattern: Pattern = path.let {
        val tmpPath = if (!path.endsWith("/") && route.appendTrailingSlash) "$path[/]*" else path
        Pattern.compile(tmpPath)
    }
    private val groupNames : List<String> = pattern.let {
        val namedGroupMatcher = Pattern.compile(namedGroupPattern).matcher(path)
        val list = mutableListOf<String>()

        while(namedGroupMatcher.find()) {
            list.add(namedGroupMatcher.group(1) as String)
        }

        list
    }

    /**
     * Tests if [input] matches [pattern]
     * @return An instance of [MatchResult] if [input] matches, else null
     */
    fun matches(input: String): MatchResult? {
        val path = if(input.contains("?")) input.split("?")[0] else input
        if(pattern.matcher(path).matches()) {
            return MatchResult(
                route = route,
                pathParams = getPathParams(path),
                queryParams = getQueryParams(input)
            )
        }

        return null
    }

    private fun getPathParams(input: String): Map<String, String> {
        val groups = pattern.toRegex().matchEntire(input)?.groups
        val result = mutableMapOf<String, String>()
        for(groupName in groupNames) {
            val group = groups?.get(groupName)
            if(group != null) {
                result[groupName] = group.value
            }
        }

        return result
    }

    private fun getQueryParams(input : String): Map<String, String> {
        if(input.contains("?")) {
            val map = mutableMapOf<String, String>()
            val params = input.split("?")[1].split("&")
            for(param in params) {
                val split = param.split("=")
                map[split[0]] = split[1]
            }

            return map
        }

        return emptyMap()
    }
}