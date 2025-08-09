package com.jparkbro.domain

import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.model.common.Result
import com.jparkbro.model.common.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) {
    // Data Store 정보 삭제
    operator fun invoke(): Flow<Result<Unit>> = flow {

        // 1. 토큰 삭제
        userPreferenceRepository.clearAllData()

        /** v1.0.1 논의 내용 */
        // 2. 유저 정보 삭제
        // 2-1. 새로 로그인 시 최근 애니 , 검색 내역 유지를 위해 userId 놔둬야 하는지 ?
        // 2-2. 다른 계정 로그인 시 최근 애니, 검색 내역 삭제 해야 하는지 ?

        // 3. 최근 애니 삭제 ?

        // 4. 검색 내역 삭제 ?

        emit(Unit)
    }.asResult()
}