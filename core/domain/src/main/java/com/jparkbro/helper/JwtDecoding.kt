package com.jparkbro.helper

import android.util.Base64
import org.json.JSONObject

fun isTokenExpired(token: String): Boolean {
    val decoded = decodeJWT(token) ?: return true
    val expTimestamp = decoded.optLong("exp", 0L)
    if (expTimestamp == 0L) return true

    val currentTimestamp = System.currentTimeMillis() / 1000
    return currentTimestamp > expTimestamp
}

fun decodeJWT(token: String): JSONObject? {
    return try {
        val parts = token.split(".")
        val payload = parts[1]
        val decoded = Base64.decode(payload, Base64.URL_SAFE)
        JSONObject(String(decoded))
    } catch (e: Exception) {
        null
    }
}