package com.jparkbro.network.auth

import com.jparkbro.model.auth.AuthResponse
import com.jparkbro.model.auth.LoginProvider

/**
 * Interface representing network calls to the AniPick backend
 */
interface AuthDataSource {
    suspend fun socialLogin(
        provider: LoginProvider,
        socialToken: String,
    ): Result<AuthResponse>
}
