package jpark.bro.data

import android.app.Activity
import jpark.bro.model.JwtToken

interface AuthRepository {

    suspend fun emailSignup()

    suspend fun emailLogin(): Result<JwtToken>

    suspend fun getKakaoAuthToken(activity: Activity): Result<String>

    suspend fun getGoogleAuthToken(activity: Activity): Result<String>

    suspend fun socialLogin(socialToken: String): Result<JwtToken>
}