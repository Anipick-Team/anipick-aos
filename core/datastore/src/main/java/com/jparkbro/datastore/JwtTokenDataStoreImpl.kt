package com.jparkbro.datastore

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jparkbro.model.auth.AuthToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Named

class JwtTokenDataStoreImpl @Inject constructor(
    @Named("jwt") private val dataStore: DataStore<Preferences>
) : JwtTokenDataStore {
    
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("encrypted_access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("encrypted_refresh_token")
        private const val KEY_ALIAS = "jwt_token_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    }
    
    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        
        if (keyStore.containsAlias(KEY_ALIAS)) {
            return keyStore.getKey(KEY_ALIAS, null) as SecretKey
        }
        
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }
    
    private fun encryptData(data: String): String {
        val secretKey = getOrCreateSecretKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray())
        
        // IV + 암호화된 데이터를 Base64로 인코딩
        val combined = iv + encryptedData
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }
    
    private fun decryptData(encryptedData: String): String {
        val secretKey = getOrCreateSecretKey()
        val combined = Base64.decode(encryptedData, Base64.DEFAULT)
        
        // IV와 암호화된 데이터 분리
        val iv = combined.sliceArray(0..11) // GCM IV는 12바이트
        val cipherText = combined.sliceArray(12 until combined.size)
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)
        
        val decryptedData = cipher.doFinal(cipherText)
        return String(decryptedData)
    }

    override suspend fun saveToken(token: AuthToken): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val encryptedAccessToken = encryptData(token.accessToken)
                
                val encryptedRefreshToken = encryptData(token.refreshToken)
                
                dataStore.edit { preferences ->
                    preferences[ACCESS_TOKEN_KEY] = encryptedAccessToken
                    preferences[REFRESH_TOKEN_KEY] = encryptedRefreshToken
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
                val encryptedToken = dataStore.data.first()[ACCESS_TOKEN_KEY]
                val accessToken = encryptedToken?.let { decryptData(it) }
                Result.success(accessToken)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getRefreshToken(): Result<String?> {
        return withContext(Dispatchers.IO) {
            try {
                val encryptedToken = dataStore.data.first()[REFRESH_TOKEN_KEY]
                val refreshToken = encryptedToken?.let { decryptData(it) }
                Result.success(refreshToken)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun clearToken(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                dataStore.edit { preferences ->
                    preferences.clear()
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}