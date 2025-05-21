package jpark.bro.model

data class JwtToken(
    val accessToken: String,
    val refreshToken: String,
)
