package com.evdayapps.go_router.models

import java.util.regex.Pattern

val namedGroupPattern = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>"

class MatchRoute(
    val path : String,
    val route: Route
) {

    private val pattern: Pattern = path.let {
        val tmpPath = if (path.endsWith("/") && route.optionalTrailingSlash) "${path.dropLast(1)}[/]*" else path
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

    fun matches(input: String): Boolean = pattern.matcher(input).matches()

    fun pathParams(input: String): Map<String, String> {
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
}