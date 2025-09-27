package com.jparkbro.domain

import com.jparkbro.data.mypage.MyPageRepository
import com.jparkbro.model.mypage.MyPageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageInfoUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    operator fun invoke(): Flow<Result<MyPageResponse>> = flow {
        try {
            myPageRepository.getMyPageInfo().fold(
                onSuccess = { myPageResponse ->
                    val profileImageBytes = myPageRepository.getMyProfileImage(myPageResponse.profileImageUrl).getOrNull()
                    
                    val updatedResponse = myPageResponse.copy(profileImageBytes = profileImageBytes)
                    emit(Result.success(updatedResponse))
                },
                onFailure = { exception ->
                    emit(Result.failure(exception))
                }
            )
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}