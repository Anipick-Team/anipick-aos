package jpark.bro.anipick.domain.usecase

import jpark.bro.anipick.domain.model.User
import jpark.bro.anipick.domain.repository.AuthRepository
import jpark.bro.anipick.domain.util.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): StateFlow<Result<User>> {
        return authRepository.authState.stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Lazily,
            initialValue = Result.Loading
        )
    }
}