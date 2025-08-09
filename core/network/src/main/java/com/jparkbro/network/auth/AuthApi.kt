package com.jparkbro.network.auth

import com.jparkbro.model.auth.AuthResponse
import com.jparkbro.model.auth.AuthToken
import com.jparkbro.model.auth.EmailLoginRequest
import com.jparkbro.model.auth.PreferenceResponse
import com.jparkbro.model.auth.RatedAnime
import com.jparkbro.model.auth.RequestCode
import com.jparkbro.model.auth.ResetPassword
import com.jparkbro.model.auth.SignupRequest
import com.jparkbro.model.auth.SocialLoginRequest
import com.jparkbro.model.auth.VerifyCode
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @POST(ApiConstants.SOCIAL_LOGIN)
    suspend fun socialLogin(
        @Path("provider") provider: String,
        @Body request: SocialLoginRequest,
    ): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.EMAIL_SIGNUP)
    suspend fun emailSignup(
        @Body request: SignupRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.EMAIL_LOGIN)
    suspend fun emailLogin(
        @Body request: EmailLoginRequest
    ): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.REQUEST_CODE)
    suspend fun requestResetCode(
        @Body request: RequestCode
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.VERIFY_CODE)
    suspend fun verifyResetCode(
        @Body request: VerifyCode
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.RESET_PASSWORD)
    suspend fun resetPassword(
        @Body request: ResetPassword
    ): Response<ApiResponse<Unit>>

    @GET(ApiConstants.SEARCH_ANIMES)
    suspend fun exploreOrSearch(
        @Query("query") query: String?,
        @Query("year") year: String?,
        @Query("season") season: Int?,
        @Query("genres") genres: Int?,
        @Query("lastId") lastId: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<PreferenceResponse>>

    @POST(ApiConstants.REVIEWS_BULK)
    suspend fun submitReviews(
        @Body request: List<RatedAnime>
    ): Response<ApiResponse<Unit>>
}