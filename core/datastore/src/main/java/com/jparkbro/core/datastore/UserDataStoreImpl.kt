package com.jparkbro.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/** user_id·user_nickname은 v1 키 그대로다. email은 v2에서 추가했다. */
private object UserKeys {
    val USER_ID = longPreferencesKey("user_id")
    val NICKNAME = stringPreferencesKey("user_nickname")
    val EMAIL = stringPreferencesKey("email")
}

class UserDataStoreImpl(
    private val context: Context,
) : UserDataStore {
    override val userId: Flow<Long?> = context.anipickDataStore.data.map { it[UserKeys.USER_ID] }
    // v1은 nickname이 없을 때 빈 문자열로 저장했다
    override val nickname: Flow<String?> = context.anipickDataStore.data.map { it[UserKeys.NICKNAME]?.takeIf(String::isNotEmpty) }
    override val email: Flow<String?> = context.anipickDataStore.data.map { it[UserKeys.EMAIL] }

    override suspend fun getUserId(): Long? = userId.first()

    override suspend fun getNickname(): String? = nickname.first()

    override suspend fun getEmail(): String? = email.first()

    override suspend fun saveUser(userId: Long, nickname: String?) {
        context.anipickDataStore.edit { prefs ->
            prefs[UserKeys.USER_ID] = userId
            if (nickname != null) prefs[UserKeys.NICKNAME] = nickname else prefs.remove(UserKeys.NICKNAME)
        }
    }

    override suspend fun saveNickname(nickname: String) {
        context.anipickDataStore.edit { prefs -> prefs[UserKeys.NICKNAME] = nickname }
    }

    override suspend fun saveEmail(email: String) {
        context.anipickDataStore.edit { prefs -> prefs[UserKeys.EMAIL] = email }
    }

    /** 같은 파일에 최근 본 애니도 있어서 전체 clear 대신 사용자 키만 지운다 */
    override suspend fun clearUser() {
        context.anipickDataStore.edit { prefs ->
            prefs.remove(UserKeys.USER_ID)
            prefs.remove(UserKeys.NICKNAME)
            prefs.remove(UserKeys.EMAIL)
        }
    }
}
