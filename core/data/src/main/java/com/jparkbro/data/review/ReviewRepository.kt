package com.jparkbro.data.review

import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.detail.DetailMyReview
import com.jparkbro.model.dto.home.detail.ListDataResult
import com.jparkbro.model.dto.info.GetInfoReviewsRequest
import com.jparkbro.model.dto.info.GetInfoReviewsResponse
import com.jparkbro.model.dto.info.GetInfoReviewsResult
import com.jparkbro.model.dto.info.ReviewRatingRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.model.dto.review.SaveMyReviewRequest
import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.model.review.ReportReviewRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ReviewRepository {

    /** Home Recent Reviews - 홈 화면용 (전체 최신 리뷰) */
    val recentReviews: StateFlow<List<Review>>
    suspend fun refreshRecentReviews() : Result<Unit>

    /** Home Detail Recent Reviews - 홈 리뷰 리스트 상세 */
    val detailRecentReviews: StateFlow<ListDataResult?>
    suspend fun loadDetailRecentReviews(cursor: Cursor? = null) : Result<Unit>

    /** Animation Detail Reviews - 애니 상세 화면용 (특정 애니의 모든 리뷰) */
    fun getAnimeReviews(animeId: Long): Flow<GetInfoReviewsResult?>
    suspend fun loadAnimeReviews(animeId: Long, request: GetInfoReviewsRequest): Result<Unit>

    /** Animation Detail My Review - 애니 상세 화면용 (내가 해당 애니에 작성한 리뷰) */
    fun getAnimeDetailMyReview(animeId: Long): Flow<Review?>
    suspend fun loadAnimeDetailMyReview(animeId: Long): Result<Unit>
    suspend fun updateAnimeRating(animeId: Long, request: ReviewRatingRequest): Result<Unit>

    /** Review Form */
    suspend fun getReviewFormAnimeReview(animeId: Long): Result<Review>
    suspend fun saveMyReview(animeId: Long, request: SaveMyReviewRequest): Result<Unit>

    /** User Content */
    suspend fun loadUserContentReviews(request: GetUserContentRequest): Result<Unit>
    suspend fun invalidateUserContent()


    /** My Reviews - 마이페이지용 (내가 작성한 모든 리뷰 목록) */
    val myReviews: StateFlow<List<Review>>
    suspend fun refreshMyReviews(cursor: Cursor?)

    suspend fun getMyReview(animeId: Long): Result<MyReview>
    suspend fun editMyReview(animeId: Long, request: EditMyReviewRequest): Result<Unit>
    suspend fun updateReviewLike(action: ApiAction, reviewId: Long, animeId: Long): Result<Unit>
    suspend fun deleteReview(reviewId: Long): Result<Unit>
    suspend fun reportReview(reviewId: Long, request: ReportReviewRequest): Result<Unit>
    suspend fun blockUser(userId: Long): Result<Unit>
}