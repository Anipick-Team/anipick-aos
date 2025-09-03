package com.jparkbro.network.mypage

import com.jparkbro.model.mypage.MyPageResponse
import com.jparkbro.model.mypage.MyReviewsResponse
import com.jparkbro.model.mypage.ProfileImgResponse
import com.jparkbro.model.mypage.UserContentResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

interface MyPageApi {
    @GET(ApiConstants.MY_PAGE)
    suspend fun getMyPageInfo(
    ): Response<ApiResponse<MyPageResponse>>

    @GET
    suspend fun getMyProfileImage(
        @Url url: String
    ): ResponseBody

    @GET(ApiConstants.WATCH_LIST)
    suspend fun getWatchList(
        @Query("status") status: String,
        @Query("lastId") lastId: Int?,
    ): Response<ApiResponse<UserContentResponse>>

    @GET(ApiConstants.WATCHING)
    suspend fun getWatching(
        @Query("status") status: String,
        @Query("lastId") lastId: Int?,
    ): Response<ApiResponse<UserContentResponse>>

    @GET(ApiConstants.FINISHED)
    suspend fun getFinished(
        @Query("status") status: String,
        @Query("lastId") lastId: Int?,
    ): Response<ApiResponse<UserContentResponse>>

    @GET(ApiConstants.LIKE_ANIMES)
    suspend fun getLikedAnimes(
        @Query("lastId") lastId: Int?,
    ): Response<ApiResponse<UserContentResponse>>

    @GET(ApiConstants.LIKE_PERSONS)
    suspend fun getLikedPersons(
        @Query("lastId") lastId: Int?,
    ): Response<ApiResponse<UserContentResponse>>

    @GET(ApiConstants.RATED)
    suspend fun getMyReviews(
        @Query("lastId") lastId: Int?,
        @Query("lastLikeCount") lastLikeCount: String?,
        @Query("lastRating") lastRating: String?,
        @Query("sort") sort: String,
        @Query("reviewOnly") reviewOnly: Boolean,
        @Query("size") size: Int?
    ): Response<ApiResponse<MyReviewsResponse>>

    @Multipart
    @POST(ApiConstants.EDIT_PROFILE_IMG)
    suspend fun editProfileImg(
        @Part profileImageFile: MultipartBody.Part
    ): Response<ApiResponse<ProfileImgResponse>>
}