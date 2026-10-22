package com.jparkbro.core.datastore

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jparkbro.core.common.auth.TokenProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

private object TokenKeys {
    val ACCESS_TOKEN = stringPreferencesKey("encrypted_access_token")
    val REFRESH_TOKEN = stringPreferencesKey("encrypted_refresh_token")
}

private const val KEY_ALIAS = "jwt_token_key"
private const val TRANSFORMATION = "AES/GCM/NoPadding"
private const val ANDROID_KEYSTORE = "AndroidKeyStore"
private const val GCM_IV_SIZE = 12
private const val GCM_TAG_BITS = 128

/** 토큰을 Android Keystore(AES/GCM)로 암호화해 [jwtTokenDataStore]에 저장한다. 키 alias와 저장 형식(IV + 암호문, Base64)은 v1과 같다. */
class TokenDataStore(
    private val context: Context,
) : TokenProvider {

    override val isLoggedIn: Flow<Boolean> = context.jwtTokenDataStore.data.map { getAccessToken() != null }

    override suspend fun getAccessToken(): String? = readToken(TokenKeys.ACCESS_TOKEN)

    override suspend fun getRefreshToken(): String? = readToken(TokenKeys.REFRESH_TOKEN)

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        withContext(Dispatchers.IO) {
            val encryptedAccessToken = encrypt(accessToken)
            val encryptedRefreshToken = encrypt(refreshToken)
            context.jwtTokenDataStore.edit { prefs ->
                prefs[TokenKeys.ACCESS_TOKEN] = encryptedAccessToken
                prefs[TokenKeys.REFRESH_TOKEN] = encryptedRefreshToken
            }
        }
    }

    override suspend fun clearTokens() {
        context.jwtTokenDataStore.edit { it.clear() }
    }

    override suspend fun isRefreshTokenValid(): Boolean {
        val token = getRefreshToken() ?: return false
        return !isJwtExpired(token)
    }

    /** 복호화에 실패하면(키 유실 등) 토큰이 없는 것으로 본다 */
    private suspend fun readToken(key: Preferences.Key<String>): String? =
        withContext(Dispatchers.IO) {
            val encrypted = context.jwtTokenDataStore.data.first()[key] ?: return@withContext null
            runCatching { decrypt(encrypted) }
                .onFailure { Timber.e(it, "토큰 복호화 실패") }
                .getOrNull()
        }

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        val spec = KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            .apply { init(spec) }
            .generateKey()
    }

    private fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION).apply { init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey()) }
        val combined = cipher.iv + cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    private fun decrypt(encrypted: String): String {
        val combined = Base64.decode(encrypted, Base64.DEFAULT)
        val iv = combined.copyOfRange(0, GCM_IV_SIZE)
        val cipherText = combined.copyOfRange(GCM_IV_SIZE, combined.size)
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), GCMParameterSpec(GCM_TAG_BITS, iv))
        }
        return String(cipher.doFinal(cipherText))
    }

    private fun isJwtExpired(jwt: String): Boolean {
        val expiresAtSeconds = decodeJwt(jwt)?.optLong("exp", 0L) ?: return true
        if (expiresAtSeconds == 0L) return true
        return System.currentTimeMillis() / 1000 > expiresAtSeconds
    }

    private fun decodeJwt(jwt: String): JSONObject? = runCatching {
        val payload = jwt.split(".")[1]
        JSONObject(String(Base64.decode(payload, Base64.URL_SAFE)))
    }.getOrNull()
}
