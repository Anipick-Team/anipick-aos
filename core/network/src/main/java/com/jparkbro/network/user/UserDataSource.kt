package com.jparkbro.network.user

import com.jparkbro.model.dto.mypage.main.GetUserInfoResponse
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageRequest
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageResponse

interface UserDataSource {
    suspend fun getUserInfo(): Result<GetUserInfoResponse>
    suspend fun getUserProfileImage(url: String): Result<ByteArray>

    suspend fun updateProfileImage(request: UpdateProfileImageRequest): Result<UpdateProfileImageResponse>
}