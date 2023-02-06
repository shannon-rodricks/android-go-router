# android-go-router
A url router for Android that is inspired by Flutter's go_router

### Why this library?
As a developer who's worked with Android for more than a decade, and then recently shifted to Flutter and as a consequence been using go_router there, 
I decided to see if I could implement the same in Android, while accounting for Android's requirements.
Traditional Android apps utilise a mix of different techniques to accomplish this and none of them are perfect.
This library aims to come as close as possible to solving those issues.

## Objectives
- [x] Clear route declaration, with subroutes
- [x] Easy route audit
- [x] Path parameter extraction
- [x] Query parameter extraction
- [ ] Route redirection, with inheritance
- [ ] Backstack creation
- [ ] Global redirection via an externally provided configuration[^1]
- [ ] Prioritise an exact match over a regex match[^2]

## Route Declaration
```
**
 * Raw route class, for definition
 * This class is processed by [Router] to generate entire paths
 *
 * @property path The path for the route
 * @property name An optional name for the route
 * @property appendTrailingSlash
 * @property intentBuilder An intent builder for building an activity intent
 * @property fragmentBuilder A builder for building a fragment
 * @property subroutes List of subroutes under this route (this route's path will be prefixed to their path)
 */
class Route(
    var path: String,
    var name: String? = null,
    var appendTrailingSlash: Boolean = true,
    var intentBuilder: IntentBuilder? = null,
    var fragmentBuilder: FragmentBuilder? = null,
    var subroutes: List<Route> = emptyList()
)
```
where `path` could be `any regex or simple string`

`IntentBuilder` and `FragmentBuilder` are
```
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
```


## Example route hierarchy
```
Route(
    path = "",
    name = "root",
    subroutes = listOf(
        Route(
            path = "events",
            name = "event-list",
            subroutes = listOf(
                Route(
                    path = "(?<slug>[\\w]+)",
                    name = "event-detail",
                    subroutes = listOf(
                        Route(path = "report", name = "event-report")
                    )
                ),
            )
        ),
        Route(path = "events/create", name = "create-event"),
        Route(
            path = "movies",
            name = "movie-list",
            subroutes = listOf(
                Route(
                    path = "(?<slug>[\\w]+)",
                    name = "movie-detail",
                    subroutes = listOf(
                        Route(path = "report", name = "movie-report")
                    )
                ),
            )
        ),
    )
)
```
which will result in this heirarchy
```
/   [name: root]
  /events   [name: event-list]
    /events/(?<slug>[\w]+)   [name: event-detail]
      /events/(?<slug>[\w]+)/report   [name: event-report]
  /events/create   [name: create-event]
  /movies   [name: movie-list]
    /movies/(?<slug>[\w]+)   [name: movie-detail]
      /movies/(?<slug>[\w]+)/report   [name: movie-report]
```

## Routing
The primary class is `Router`.
This class is intantiated with a base route (and subroutes under it) like
`val router = Router(baseRoute)`

To query a match we do
`val match = router.getMatch(url)`
which returns a `MatchResult` which can be queried for the intentBuilder and FragmentBuilder

#### Example
```
getMatch("/events/test?action=none&timestamp=001")
```
results in
```
MatchResult(
	name: event-detail
	path: /events/(?<slug>[\w]+)
	pathParams: {slug=test}
	queryParams: {action=none, timestamp=001}
)
```


[^1]: Sometimes, we might require redirection based on temporary business requirements, to divert one path to another
[^2]: A path like `/events/create` should be prioritised over `/events/(?<slug>[\w]+)` when querying `/events/create`
