package com.jparkbro.datastore

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.model.auth.AuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject

/**
 * v2 [TokenProvider]를 v1 [JwtTokenDataStore] 위에 얹는다. v1 로그인(구글 포함)이 저장한 토큰을 v2 core.network도
 * 같이 쓰고, v2 쪽에서 갱신·삭제한 토큰도 v1 화면에 그대로 반영된다. v1 화면을 모두 v2로 바꾼 뒤 v2 TokenDataStore로
 * 옮길 때는 이 저장소의 토큰을 이전해야 로그인이 풀리지 않는다.
 */
class JwtTokenProvider(
    private val jwtTokenDataStore: JwtTokenDataStore,
    jwtDataStore: DataStore<Preferences>,
) : TokenProvider {

    override val isLoggedIn: Flow<Boolean> = jwtDataStore.data.map { getAccessToken() != null }

    override suspend fun getAccessToken(): String? = jwtTokenDataStore.getAccessToken().getOrNull()

    override suspend fun getRefreshToken(): String? = jwtTokenDataStore.getRefreshToken().getOrNull()

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        jwtTokenDataStore.setToken(AuthToken(accessToken = accessToken, refreshToken = refreshToken))
    }

    override suspend fun clearTokens() {
        jwtTokenDataStore.setToken(null)
    }

    override suspend fun isRefreshTokenValid(): Boolean {
        val token = getRefreshToken() ?: return false
        return !isJwtExpired(token)
    }

    private fun isJwtExpired(jwt: String): Boolean {
        val decoded = decodeJwt(jwt) ?: return true
        val expiresAtSeconds = decoded.optLong("exp", 0L)
        if (expiresAtSeconds == 0L) return true

        val nowSeconds = System.currentTimeMillis() / 1000
        return nowSeconds > expiresAtSeconds
    }

    private fun decodeJwt(jwt: String): JSONObject? {
        return try {
            val payload = jwt.split(".")[1]
            val decoded = Base64.decode(payload, Base64.URL_SAFE)
            JSONObject(String(decoded))
        } catch (e: Exception) {
            null
        }
    }
}
