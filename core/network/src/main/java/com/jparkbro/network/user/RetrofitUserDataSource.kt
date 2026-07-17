package com.jparkbro.network.user

import android.util.Log
import com.jparkbro.model.dto.mypage.main.GetUserInfoResponse
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageRequest
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageResponse
import com.jparkbro.model.dto.mypage.setting.UserResponse
import com.jparkbro.model.dto.mypage.useredit.UpdateEmailRequest
import com.jparkbro.model.dto.mypage.useredit.UpdateNicknameRequest
import com.jparkbro.model.dto.mypage.useredit.UpdatePasswordRequest
import com.jparkbro.model.exception.NetworkException
import com.jparkbro.network.util.safeApiCall
import com.jparkbro.network.util.safeApiCallUnit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class RetrofitUserDataSource @Inject constructor(
    private val userApi: UserApi
) : UserDataSource {
    companion object {
        private const val TAG = "RetrofitUserDataSource"
    }

    override suspend fun getUserInfo(): Result<GetUserInfoResponse> {
        return safeApiCall(TAG, "getUserInfo") { userApi.getUserInfo() }
    }

    override suspend fun getUserProfileImage(url: String): Result<ByteArray> {
        return try {
            val responseBody = userApi.getUserProfileImage(url)
            val bytes = responseBody.bytes()
            Result.success(bytes)
        } catch (e: IOException) {
            Log.e(TAG, "getUserProfileImage() network error", e)
            Result.failure(NetworkException())
        } catch (e: Exception) {
            Log.e(TAG, "getUserProfileImage() exception", e)
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

        return safeApiCall(TAG, "updateProfileImage") { userApi.updateProfileImage(profileImageFile = profileImageFile) }
    }

    override suspend fun loadUser(): Result<UserResponse> {
        return safeApiCall(TAG, "loadUser") { userApi.loadUser() }
    }

    override suspend fun updateNickname(request: UpdateNicknameRequest): Result<Unit> {
        return safeApiCallUnit(TAG, "updateNickname") { userApi.updateNickname(request) }
    }

    override suspend fun updateEmail(request: UpdateEmailRequest): Result<Unit> {
        return safeApiCallUnit(TAG, "updateEmail") { userApi.updateEmail(request) }
    }

    override suspend fun updatePassword(request: UpdatePasswordRequest): Result<Unit> {
        return safeApiCallUnit(TAG, "updatePassword") { userApi.updatePassword(request) }
    }

    override suspend fun userWithdrawal(): Result<Unit> {
        return safeApiCallUnit(TAG, "userWithdrawal") { userApi.userWithdrawal() }
    }
}
