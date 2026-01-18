package com.jparkbro.network.user

import com.jparkbro.model.dto.mypage.main.GetUserInfoResponse
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageRequest
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageResponse
import com.jparkbro.model.dto.mypage.setting.UserResponse
import com.jparkbro.model.dto.mypage.useredit.UpdateEmailRequest
import com.jparkbro.model.dto.mypage.useredit.UpdateNicknameRequest
import com.jparkbro.model.dto.mypage.useredit.UpdatePasswordRequest

interface UserDataSource {
    suspend fun getUserInfo(): Result<GetUserInfoResponse>
    suspend fun getUserProfileImage(url: String): Result<ByteArray>

    suspend fun updateProfileImage(request: UpdateProfileImageRequest): Result<UpdateProfileImageResponse>

    suspend fun loadUser(): Result<UserResponse>

    suspend fun updateNickname(request: UpdateNicknameRequest): Result<Unit>
    suspend fun updateEmail(request: UpdateEmailRequest): Result<Unit>
    suspend fun updatePassword(request: UpdatePasswordRequest): Result<Unit>
    suspend fun userWithdrawal(): Result<Unit>

}