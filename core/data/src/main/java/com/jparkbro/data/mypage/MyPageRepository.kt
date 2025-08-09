package com.jparkbro.data.mypage

import android.net.Uri
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.model.mypage.MyPageResponse
import com.jparkbro.model.mypage.MyReviewsRequest
import com.jparkbro.model.mypage.MyReviewsResponse
import com.jparkbro.model.mypage.ProfileImgResponse
import com.jparkbro.model.mypage.UserContentResponse

interface MyPageRepository {
    suspend fun getMyPageInfo(): Result<MyPageResponse>

    suspend fun getUserContents(type: ContentType, lastId: Int? = null): Result<UserContentResponse>

    suspend fun getMyReviews(request: MyReviewsRequest): Result<MyReviewsResponse>

    suspend fun editProfileImg(contentUri: Uri): Result<ProfileImgResponse>
}