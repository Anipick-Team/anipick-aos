package jpark.bro.anipick.data.model

data class KakaoSignInRequest(
    val accessToken: String,
    val email: String,
    val name: String,
    val profilePicture: String
)
