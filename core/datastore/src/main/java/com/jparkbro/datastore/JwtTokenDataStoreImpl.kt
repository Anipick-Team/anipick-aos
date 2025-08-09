package com.jparkbro.datastore

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.jparkbro.model.auth.AuthToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JwtTokenDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : JwtTokenDataStore {
    private val encryptedPrefs by lazy {
        EncryptedSharedPreferences.create(
            "secure_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    override suspend fun saveToken(token: AuthToken): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                encryptedPrefs.edit {
                    putString("access_token", token.accessToken)
                    putString("refresh_token", token.refreshToken)
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getAccessToken(): Result<String?> {
        return withContext(Dispatchers.IO) {
            try {
                val accessToken = encryptedPrefs.getString("access_token", null)
                Result.success(accessToken)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getRefreshToken(): Result<String?> {
        return withContext(Dispatchers.IO) {
            try {
                val refreshToken = encryptedPrefs.getString("refresh_token", null)
                Result.success(refreshToken)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun clearToken(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                encryptedPrefs.edit {
                    clear()
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}