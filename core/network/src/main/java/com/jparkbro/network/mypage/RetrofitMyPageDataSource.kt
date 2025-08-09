package com.jparkbro.network.mypage

import com.jparkbro.model.mypage.MyPageResponse
import com.jparkbro.model.mypage.MyReviewsRequest
import com.jparkbro.model.mypage.MyReviewsResponse
import com.jparkbro.model.mypage.ProfileImgRequest
import com.jparkbro.model.mypage.ProfileImgResponse
import com.jparkbro.model.mypage.UserContentResponse
import com.jparkbro.network.util.toResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RetrofitMyPageDataSource @Inject constructor(
    private val myPageApi: MyPageApi
) : MyPageDataSource {
    companion object {
        private const val TAG = "RetrofitMyPageDataSource"
    }

    override suspend fun getMyPageInfo(): Result<MyPageResponse> {
        return myPageApi.getMyPageInfo().toResult(TAG, "getMyPageInfo")
    }

    override suspend fun getWatchList(type: String, lastId: Int?): Result<UserContentResponse> {
        return myPageApi.getWatchList(type, lastId).toResult(TAG, "getWatchList")
    }

    override suspend fun getWatching(type: String, lastId: Int?): Result<UserContentResponse> {
        return myPageApi.getWatching(type, lastId).toResult(TAG, "getWatching")
    }

    override suspend fun getFinished(type: String, lastId: Int?): Result<UserContentResponse> {
        return myPageApi.getFinished(type, lastId).toResult(TAG, "getFinished")
    }

    override suspend fun getLikedAnimes(lastId: Int?): Result<UserContentResponse> {
        return myPageApi.getLikedAnimes(lastId).toResult(TAG, "getLikedAnimes")
    }

    override suspend fun getLikedPersons(lastId: Int?): Result<UserContentResponse> {
        return myPageApi.getLikedPersons(lastId).toResult(TAG, "getLikedPersons")
    }

    override suspend fun getMyReviews(request: MyReviewsRequest): Result<MyReviewsResponse> {
        return myPageApi.getMyReviews(
            lastId = request.lastId,
            lastLikeCount = request.lastLikeCount,
            lastRating = request.lastRating,
            sort = request.sort,
            reviewOnly = request.reviewOnly,
            size = request.size,
        ).toResult(TAG, "getMyReviews")
    }

    override suspend fun editProfileImg(request: ProfileImgRequest): Result<ProfileImgResponse> {
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

        return myPageApi.editProfileImg(profileImageFile = profileImageFile).toResult(TAG, "editProfileImg")
    }
}