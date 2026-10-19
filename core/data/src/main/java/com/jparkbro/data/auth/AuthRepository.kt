package com.jparkbro.data.auth

import android.app.Activity
import com.jparkbro.model.auth.AuthResponse
import com.jparkbro.model.auth.LoginProvider

interface AuthRepository {
    suspend fun getKakaoAuthToken(activity: Activity): Result<String>
    suspend fun getGoogleAuthToken(activity: Activity): Result<String>
    suspend fun socialLogin(provider: LoginProvider, socialToken: String): Result<AuthResponse>

}
