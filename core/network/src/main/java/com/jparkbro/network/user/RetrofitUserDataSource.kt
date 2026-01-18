package com.jparkbro.network.user

import com.jparkbro.model.dto.mypage.main.GetUserInfoResponse
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageRequest
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageResponse
import com.jparkbro.model.dto.mypage.setting.UserResponse
import com.jparkbro.model.dto.mypage.useredit.UpdateEmailRequest
import com.jparkbro.model.dto.mypage.useredit.UpdateNicknameRequest
import com.jparkbro.model.dto.mypage.useredit.UpdatePasswordRequest
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RetrofitUserDataSource @Inject constructor(
    private val userApi: UserApi
) : UserDataSource {
    companion object {
        private const val TAG = "RetrofitUserDataSource"
    }

    override suspend fun getUserInfo(): Result<GetUserInfoResponse> {
        return userApi.getUserInfo().toResult(TAG, "getUserInfo")
    }

    override suspend fun getUserProfileImage(url: String): Result<ByteArray> {
        return try {
            val responseBody = userApi.getUserProfileImage(url)
            val bytes = responseBody.bytes()
            Result.success(bytes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfileImage(request: UpdateProfileImageRequest): Result<UpdateProfileImageResponse> {
        val imageData = request.imageData ?: return Result.failure(
            IllegalArgumentException("Image data is null")
        )
        val requestBody = imageData.toRequestBody(
            contentType = request.mimeType?.toMediaTypeOrNull() ?: "image/jpeg".toMediaTypeOrNull()
        )
        val profileImageFile = MultipartBody.Part.createFormData(
            name = request.name,
            filename = request.filename ?: "profile_image.jpg",
            body = requestBody
        )

        return userApi.updateProfileImage(profileImageFile = profileImageFile).toResult(TAG, "updateProfileImage")
    }

    override suspend fun loadUser(): Result<UserResponse> {
        return userApi.loadUser().toResult(TAG, "loadUser")
    }

    override suspend fun updateNickname(request: UpdateNicknameRequest): Result<Unit> {
        return userApi.updateNickname(request).toUnitResult(TAG, "updateNickname")
    }

    override suspend fun updateEmail(request: UpdateEmailRequest): Result<Unit> {
        return userApi.updateEmail(request).toUnitResult(TAG, "updateEmail")

    }

    override suspend fun updatePassword(request: UpdatePasswordRequest): Result<Unit> {
        return userApi.updatePassword(request).toUnitResult(TAG, "updatePassword")

    }

    override suspend fun userWithdrawal(): Result<Unit> {
        return userApi.userWithdrawal().toUnitResult(TAG, "userWithdrawal")
    }
}