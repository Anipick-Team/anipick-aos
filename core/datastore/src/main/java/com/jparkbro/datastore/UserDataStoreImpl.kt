package com.jparkbro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserDataStore {

    companion object {
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_NICKNAME_KEY = stringPreferencesKey("user_nickname")
    }

    override suspend fun saveUserInfo(userId: Int, nickname: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[USER_ID_KEY] = userId
                preferences[USER_NICKNAME_KEY] = nickname
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserId(): Result<Int> {
        return try {
            val userId = dataStore.data.first()[USER_ID_KEY]
            if (userId != null) {
                Result.success(userId)
            } else {
                Result.failure(Exception("UserId 가 저장되지 않았습니다"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserNickName(): Result<String> {
        return try {
            val nickname = dataStore.data.first()[USER_NICKNAME_KEY]
            if (nickname != null) {
                Result.success(nickname)
            } else {
                Result.failure(Exception("닉네임이 저장되지 않았습니다"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearUserInfo(): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences.remove(USER_ID_KEY)
                preferences.remove(USER_NICKNAME_KEY)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}