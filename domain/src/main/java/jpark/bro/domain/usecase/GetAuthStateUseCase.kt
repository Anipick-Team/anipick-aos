package jpark.bro.domain.usecase

import jpark.bro.domain.model.User
import jpark.bro.domain.repository.AuthRepository
import jpark.bro.domain.util.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): StateFlow<ApiResult<User>> {
        return authRepository.authState.stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Lazily,
            initialValue = ApiResult.Loading
        )
    }
}