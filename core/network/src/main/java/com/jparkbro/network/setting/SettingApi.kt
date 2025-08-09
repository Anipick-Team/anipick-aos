package com.jparkbro.network.setting

import com.jparkbro.model.setting.UpdateEmail
import com.jparkbro.model.setting.UpdateNickname
import com.jparkbro.model.setting.UpdatePassword
import com.jparkbro.model.setting.UserInfo
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT

interface SettingApi {
    @GET(ApiConstants.GET_USER_INFO)
    suspend fun getUserInfo(
    ): Response<ApiResponse<UserInfo>>

    @PATCH(ApiConstants.EDIT_NICKNAME)
    suspend fun editNickname(
        @Body request: UpdateNickname
    ): Response<ApiResponse<Unit>>

    @PUT(ApiConstants.EDIT_EMAIL)
    suspend fun editEmail(
        @Body request: UpdateEmail
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.EDIT_PASSWORD)
    suspend fun editPassword(
        @Body request: UpdatePassword
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.USER_WITHDRAWAL)
    suspend fun userWithdrawal(
    ): Response<ApiResponse<Unit>>
}