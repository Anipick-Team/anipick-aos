package com.jparkbro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.jparkbro.core.datastore.UserDataStore as V2UserDataStore

/**
 * v2 [V2UserDataStore]의 userId/nickname을 v1 [UserDataStore]에 맡긴다. v1 구글 로그인이 저장한 사용자 정보를 v2 화면이,
 * v2 로그인(카카오·이메일)이 저장한 정보를 v1 화면이 그대로 읽는다. v1에 없는 email만 v2 저장소([emailStore])에 둔다.
 * v1 화면을 모두 v2로 바꾼 뒤 v2 UserDataStoreImpl로 옮길 때는 v1 저장소의 값을 이전해야 한다.
 */
class V1BackedUserDataStore(
    private val v1UserDataStore: UserDataStore,
    v1DataStore: DataStore<Preferences>,
    private val emailStore: V2UserDataStore,
) : V2UserDataStore {

    override val userId: Flow<Long?> = v1DataStore.data.map { getUserId() }
    override val nickname: Flow<String?> = v1DataStore.data.map { getNickname() }
    override val email: Flow<String?> = emailStore.email

    override suspend fun getUserId(): Long? = v1UserDataStore.getUserId().getOrNull()

    // v1 저장소는 nickname이 non-null이라 null을 빈 문자열로 저장한다
    override suspend fun getNickname(): String? = v1UserDataStore.getUserNickName().getOrNull()?.takeIf { it.isNotEmpty() }

    override suspend fun getEmail(): String? = emailStore.getEmail()

    override suspend fun saveUser(userId: Long, nickname: String?) {
        v1UserDataStore.saveUserInfo(userId, nickname.orEmpty())
    }

    override suspend fun saveNickname(nickname: String) {
        val userId = getUserId() ?: return
        v1UserDataStore.saveUserInfo(userId, nickname)
    }

    override suspend fun saveEmail(email: String) {
        emailStore.saveEmail(email)
    }

    override suspend fun clearUser() {
        v1UserDataStore.clearUserInfo()
        emailStore.clearUser()
    }
}
