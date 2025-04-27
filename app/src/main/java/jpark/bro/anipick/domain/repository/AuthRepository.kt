package jpark.bro.anipick.domain.repository

import jpark.bro.anipick.domain.util.Result
import jpark.bro.anipick.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    val authState: Flow<Result<User>>
//    val isAuthenticated: Flow<Boolean>

    suspend fun signInWithGoogle(): Result<User>
    suspend fun signOut(): Result<Unit>

//    suspend fun authenticateWithServer(idToken: String): Result<Unit>
//    suspend fun refreshToken(): Result<Unit>
}