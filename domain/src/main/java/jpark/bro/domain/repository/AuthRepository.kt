package jpark.bro.domain.repository

import jpark.bro.domain.common.ActivityProvider
import jpark.bro.domain.model.User
import jpark.bro.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    val authState: Flow<ApiResult<User>>

    suspend fun signInWithGoogle(): ApiResult<User>
    suspend fun signOut(): ApiResult<Unit>

    suspend fun signInWithKakao(activityProvider: ActivityProvider)
}