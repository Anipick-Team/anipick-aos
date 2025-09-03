package com.jparkbro.network.mypage

import com.jparkbro.model.mypage.MyPageResponse
import com.jparkbro.model.mypage.MyReviewsRequest
import com.jparkbro.model.mypage.MyReviewsResponse
import com.jparkbro.model.mypage.ProfileImgRequest
import com.jparkbro.model.mypage.ProfileImgResponse
import com.jparkbro.model.mypage.UserContentResponse

interface MyPageDataSource {
    suspend fun getMyPageInfo(): Result<MyPageResponse>
    suspend fun getMyProfileImage(url: String): Result<ByteArray>

    suspend fun getWatchList(type: String, lastId: Int?,): Result<UserContentResponse>
    suspend fun getWatching(type: String, lastId: Int?,): Result<UserContentResponse>
    suspend fun getFinished(type: String, lastId: Int?,): Result<UserContentResponse>
    suspend fun getLikedAnimes(lastId: Int?,): Result<UserContentResponse>
    suspend fun getLikedPersons(lastId: Int?,): Result<UserContentResponse>

    suspend fun getMyReviews(request: MyReviewsRequest): Result<MyReviewsResponse>

    suspend fun editProfileImg(request: ProfileImgRequest): Result<ProfileImgResponse>
}