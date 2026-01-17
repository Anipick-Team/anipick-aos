package com.jparkbro.network.user

import com.jparkbro.model.dto.mypage.main.GetUserInfoResponse
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
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
}