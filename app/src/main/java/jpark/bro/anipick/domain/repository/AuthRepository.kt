package jpark.bro.anipick.domain.repository

import android.app.Activity
import jpark.bro.anipick.domain.util.Result
import jpark.bro.anipick.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    val authState: Flow<Result<User>>
//    val isAuthenticated: Flow<Boolean>

    suspend fun signInWithGoogle(): Result<User>
    suspend fun signOut(): Result<Unit>

    suspend fun signInWithKakao(activity: Activity)

//    suspend fun authenticateWithServer(idToken: String): Result<Unit>
//    suspend fun refreshToken(): Result<Unit>
}