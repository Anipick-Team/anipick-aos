package com.jparkbro.network.user

import com.jparkbro.model.dto.mypage.main.GetUserInfoResponse
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageResponse
import com.jparkbro.model.dto.mypage.setting.UserResponse
import com.jparkbro.model.dto.mypage.useredit.UpdateEmailRequest
import com.jparkbro.model.dto.mypage.useredit.UpdateNicknameRequest
import com.jparkbro.model.dto.mypage.useredit.UpdatePasswordRequest
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Url

interface UserApi {
    @GET(ApiConstants.MY_PAGE)
    suspend fun getUserInfo(
    ): Response<ApiResponse<GetUserInfoResponse>>

    @GET
    suspend fun getUserProfileImage(
        @Url url: String
    ): ResponseBody

    @Multipart
    @POST(ApiConstants.EDIT_PROFILE_IMG)
    suspend fun updateProfileImage(
        @Part profileImageFile: MultipartBody.Part
    ): Response<ApiResponse<UpdateProfileImageResponse>>

    @GET(ApiConstants.GET_USER)
    suspend fun loadUser(
    ): Response<ApiResponse<UserResponse>>

    @PATCH(ApiConstants.EDIT_NICKNAME)
    suspend fun updateNickname(
        @Body request: UpdateNicknameRequest
    ): Response<ApiResponse<Unit>>

    @PUT(ApiConstants.EDIT_EMAIL)
    suspend fun updateEmail(
        @Body request: UpdateEmailRequest
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.EDIT_PASSWORD)
    suspend fun updatePassword(
        @Body request: UpdatePasswordRequest
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.USER_WITHDRAWAL)
    suspend fun userWithdrawal(
    ): Response<ApiResponse<Unit>>
}