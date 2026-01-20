package com.jparkbro.network

//suspend inline fun <reified T> responseToResult(response: Httpre)

fun constructRoute(route: String): String {
    return when {
        route.contains("") -> route // BuildConfig.BASE_URL
        route.startsWith("/") -> "" + route // BuildConfig.BASE_URL + route
        else -> "" + "/$route" // BuildConfig.BASE_URL + "/$route"
    }
}