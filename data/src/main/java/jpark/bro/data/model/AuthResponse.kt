package jpark.bro.data.model

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val expiresIn: Long,
)
