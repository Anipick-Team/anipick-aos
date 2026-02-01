package com.jparkbro.data.review

import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.user.UserRepository
import com.jparkbro.model.enum.ApiAction
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.common.review.toReview
import com.jparkbro.model.dto.home.detail.ListDataResult
import com.jparkbro.model.dto.home.detail.toResult
import com.jparkbro.model.dto.info.GetInfoReviewsRequest
import com.jparkbro.model.dto.info.GetInfoReviewsResult
import com.jparkbro.model.dto.info.ReviewRatingRequest
import com.jparkbro.model.dto.info.toResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.model.dto.mypage.usercontent.toResult
import com.jparkbro.model.dto.review.SaveMyReviewRequest
import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.network.home.HomeDataSource
import com.jparkbro.network.review.ReviewDataSource
import com.jparkbro.network.user.UserDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource,
    private val homeDataSource: HomeDataSource,
    private val userRepository: UserRepository,
    private val animeRepository: AnimeRepository,
    private val userDataSource: UserDataSource,
) : ReviewRepository {

    /** Home Recent Reviews - 홈 화면용 (전체 데이터 제공) */
    private val _recentReviews = MutableStateFlow<List<Review>>(emptyList())
    override val recentReviews = _recentReviews.asStateFlow()
    override suspend fun refreshRecentReviews(): Result<Unit> {
        return homeDataSource.getRecentReviews()
            .fold(
                onSuccess = { reviews ->
                    _recentReviews.update { reviews.map { it.toReview() } }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    /** Home Detail Recent Reviews - 홈 리뷰 리스트 상세 */
    private val _detailRecentReviews = MutableStateFlow<ListDataResult?>(null)
    override val detailRecentReviews = _detailRecentReviews.asStateFlow()
    override suspend fun loadDetailRecentReviews(cursor: Cursor?): Result<Unit> {
        val isInitialLoad = cursor == null

        return homeDataSource.getDetailRecentReviews(
            request = HomeDetailRequest(lastId = cursor?.lastId)
        ).fold(
            onSuccess = { response ->
                val reviewsWithImages = response.reviews.map { review ->
                    val url = review.profileImageUrl
                    val id = url?.substringAfterLast("/")?.toLongOrNull()
                    if (id != null && id != -1L) {
                        val imageBytes = userDataSource.getUserProfileImage(url).getOrNull()
                        review.copy(profileImageByteArray = imageBytes)
                    } else {
                        review
                    }
                }
                val updatedResponse = response.copy(reviews = reviewsWithImages)

                _detailRecentReviews.update { current ->
                    if (isInitialLoad) {
                        updatedResponse.toResult()
                    } else {
                        current?.copy(
                            reviews = current.reviews + updatedResponse.toResult().reviews,
                            cursor = updatedResponse.toResult().cursor
                        ) ?: updatedResponse.toResult()
                    }
                }
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )
    }

    /** Animation Detail Reviews - 애니 상세 화면용 */
    private val animeReviewsCache = MutableStateFlow<Map<Long, GetInfoReviewsResult>>(emptyMap())
    override fun getAnimeReviews(animeId: Long): Flow<GetInfoReviewsResult?> {
        return animeReviewsCache.map { cache ->
            cache[animeId]
        }
    }
    override suspend fun loadAnimeReviews(animeId: Long, request: GetInfoReviewsRequest): Result<Unit> {
        val isInitialLoad = request.lastId == null

        return reviewDataSource.getAnimeDetailReviews(request = request)
            .fold(
                onSuccess = { response ->
                    val reviewsWithImages = response.reviews.map { review ->
                        val url = review.profileImageUrl
                        val id = url?.substringAfterLast("/")?.toLongOrNull()
                        if (id != null && id != -1L) {
                            val imageBytes = userDataSource.getUserProfileImage(url).getOrNull()
                            review.copy(profileImageByteArray = imageBytes)
                        } else {
                            review
                        }
                    }
                    val updatedResponse = response.copy(reviews = reviewsWithImages)

                    animeReviewsCache.update { cache ->
                        val current = cache[animeId]
                        val result = updatedResponse.toResult()
                        val newData = if (isInitialLoad || current == null) {
                            // 초기 로드: 기존 데이터 대체
                            result
                        } else {
                            // 추가 로드: 기존 리뷰에 새 리뷰 추가
                            current.copy(reviews = current.reviews + result.reviews)
                        }
                        cache + (animeId to newData)
                    }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    /** Animation Detail My Review - 애니 상세 화면용 (내가 해당 애니에 작성한 리뷰) */
    private val animeDetailMyReviewCache = MutableStateFlow<Map<Long, Review>>(emptyMap())
    override fun getAnimeDetailMyReview(animeId: Long): Flow<Review?> {
        return animeDetailMyReviewCache.map { cache ->
            cache[animeId]
        }
    }
    override suspend fun loadAnimeDetailMyReview(animeId: Long): Result<Unit> {
        return reviewDataSource.getAnimeDetailMyReview(animeId)
            .fold(
                onSuccess = { dto ->
                    animeDetailMyReviewCache.update { cache ->
                        cache + (animeId to dto.toReview())
                    }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    override suspend fun updateAnimeRating(animeId: Long, request: ReviewRatingRequest): Result<Unit> {
        val currentReview = animeDetailMyReviewCache.value[animeId]
        val currentRating = currentReview?.rating ?: 0f

        val result = when {
            currentRating == 0f && request.rating > 0f -> {
                reviewDataSource.createAnimeRating(animeId, request)
            }
            request.rating == 0f -> {
                reviewDataSource.deleteAnimeRating(currentReview?.reviewId ?: 0)
            }
            else -> {
                reviewDataSource.updateAnimeRating(currentReview?.reviewId ?: 0, request)
            }
        }

        return result.fold(
            onSuccess = {
                loadAnimeDetailMyReview(animeId)
                animeRepository.loadDetailInfo(animeId)
                userRepository.refreshUserInfo()
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )
    }

    /** Review Form */
    override suspend fun getReviewFormAnimeReview(animeId: Long): Result<Review> {
        return reviewDataSource.getReviewFormAnimeReview(animeId).map { it.toReview() }
    }
    override suspend fun saveMyReview(animeId: Long, request: SaveMyReviewRequest): Result<Unit> {
        return reviewDataSource.updateMyReview(animeId, request)
            .onSuccess { refreshReviewRelatedData(animeId) }
            .map { Unit }
    }

    /** User Content */
    override suspend fun loadUserContentReviews(request: GetUserContentRequest): Result<Unit> {
        return reviewDataSource.loadUserContentReviews(request)
            .map { it.toResult() }
            .fold(
                onSuccess = { result ->
                    userRepository.userContentCache.update { cache ->
                        cache.copy(
                            contentType = request.contentType,
                            reviewSort = request.sort,
                            reviews = if (request.lastId == null) result.reviews else cache.reviews + result.reviews,
                            cursor = result.cursor,
                            count = result.count,
                        )
                    }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    override suspend fun invalidateUserContent() {
        val currentType = userRepository.userContentCache.value.contentType ?: return
        val currentSort = userRepository.userContentCache.value.reviewSort ?: ReviewSortType.LATEST
        userRepository.userContentCache.update { GetUserContentResult(contentType = currentType, reviewSort = currentSort) }
        loadUserContentReviews(GetUserContentRequest(contentType = currentType, sort = currentSort))
    }

    override suspend fun updateReviewLike(action: ApiAction, reviewId: Long, animeId: Long): Result<Unit> {
        val result = when (action) {
            ApiAction.CREATE -> reviewDataSource.likedReview(reviewId)
            ApiAction.DELETE -> reviewDataSource.unLikedReview(reviewId)
            ApiAction.UPDATE -> Result.failure(IllegalArgumentException("좋아요는 UPDATE 액션을 지원하지 않습니다"))
        }

        if (result.isSuccess) {
            val isLiked = action == ApiAction.CREATE
            val likeCountDelta = if (isLiked) 1 else -1

            // animeDetailMyReviewCache 업데이트
            animeDetailMyReviewCache.update { cache ->
                val myReview = cache[animeId]
                if (myReview != null && myReview.reviewId == reviewId) {
                    cache + (animeId to myReview.copy(
                        isLiked = isLiked,
                        likeCount = (myReview.likeCount ?: 0) + likeCountDelta
                    ))
                } else {
                    cache
                }
            }

            // animeReviewsCache 업데이트
            animeReviewsCache.update { cache ->
                val reviewsResult = cache[animeId]
                if (reviewsResult != null) {
                    cache + (animeId to reviewsResult.copy(
                        reviews = reviewsResult.reviews.map { review ->
                            if (review.reviewId == reviewId) {
                                review.copy(
                                    isLiked = isLiked,
                                    likeCount = (review.likeCount ?: 0) + likeCountDelta
                                )
                            } else {
                                review
                            }
                        }
                    ))
                } else {
                    cache
                }
            }

            // userContentCache 업데이트
            userRepository.userContentCache.update { cache ->
                cache.copy(
                    reviews = cache.reviews.map { review ->
                        if (review.reviewId == reviewId) {
                            review.copy(
                                isLiked = isLiked,
                                likeCount = (review.likeCount ?: 0) + likeCountDelta
                            )
                        } else {
                            review
                        }
                    }
                )
            }

            _detailRecentReviews.update { cache ->
                cache?.copy(
                    reviews = cache.reviews.map { review ->
                        if (review.reviewId == reviewId) {
                            review.copy(
                                isLiked = isLiked,
                                likeCount = (review.likeCount ?: 0) + likeCountDelta
                            )
                        } else {
                            review
                        }
                    }
                )
            }
        }

        return result
    }

    override suspend fun deleteReview(reviewId: Long, animeId: Long): Result<Unit> {
        return reviewDataSource.deleteReview(reviewId)
            .onSuccess { refreshReviewRelatedData(animeId) }
            .map { Unit }
    }

    override suspend fun reportReview(reviewId: Long, animeId: Long, request: ReportReviewRequest): Result<Unit> {
        return reviewDataSource.reportReview(reviewId, request)
            .onSuccess { refreshReviewRelatedData(animeId) }
            .map { Unit }
    }

    override suspend fun blockUser(userId: Long, animeId: Long): Result<Unit> {
        return reviewDataSource.blockUser(userId)
            .onSuccess { refreshReviewRelatedData(animeId) }
            .map { Unit }
    }

    private suspend fun refreshReviewRelatedData(animeId: Long) {
        refreshRecentReviews()
        loadDetailRecentReviews()
        loadAnimeDetailMyReview(animeId)
        animeRepository.loadDetailInfo(animeId)
        loadAnimeReviews(animeId, GetInfoReviewsRequest(animeId = animeId))
        invalidateUserContent()
    }
}