package com.jparkbro.data.detail

import com.jparkbro.datastore.RecentAnimeDataStore
import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.common.WatchStatus
import com.jparkbro.model.detail.ActorDetailResponse
import com.jparkbro.model.detail.AnimeActorsResponse
import com.jparkbro.model.detail.DetailActor
import com.jparkbro.model.detail.DetailInfo
import com.jparkbro.model.detail.DetailMyReview
import com.jparkbro.model.detail.DetailSeries
import com.jparkbro.model.detail.DetailStudio
import com.jparkbro.model.detail.ReviewDetailRequest
import com.jparkbro.model.detail.ReviewDetailResponse
import com.jparkbro.model.review.ReviewRating
import com.jparkbro.network.detail.DetailDataSource
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val detailDataSource: DetailDataSource,
    private val recentAnimeDataStore: RecentAnimeDataStore,
) : DetailRepository {

    override suspend fun getDetailInfo(animeId: Int): Result<DetailInfo> {
        return detailDataSource.getDetailInfo(animeId)
    }

    override suspend fun getDetailActor(animeId: Int): Result<List<DetailActor>> {
        return detailDataSource.getDetailActor(animeId)
    }

    override suspend fun getDetailSeries(animeId: Int): Result<List<DetailSeries>> {
        return detailDataSource.getDetailSeries(animeId)
    }

    override suspend fun getDetailRecommendation(animeId: Int): Result<List<DefaultAnime>> {
        return detailDataSource.getDetailRecommendation(animeId)
    }

    override suspend fun getDetailReviews(request: ReviewDetailRequest): Result<ReviewDetailResponse> {
        return detailDataSource.getDetailReviews(request)
    }

    override suspend fun getMyReview(animeId: Int): Result<DetailMyReview> {
        return detailDataSource.getMyReview(animeId)
    }

    override suspend fun saveRecentAnime(animeId: Int): Result<Unit> {
        return recentAnimeDataStore.saveRecentAnime(animeId)
    }

    override suspend fun loadRecentAnime(): Result<Int> {
        return recentAnimeDataStore.loadRecentAnime()
    }

    override suspend fun setLikeAnime(action: ApiAction, animeId: Int): Result<Unit> {
        return when (action) {
            ApiAction.CREATE -> detailDataSource.likeAnime(animeId)
            ApiAction.DELETE -> detailDataSource.unLikeAnime(animeId)
            ApiAction.UPDATE -> Result.failure(IllegalArgumentException("좋아요는 UPDATE 액션을 지원하지 않습니다"))
        }
    }

    override suspend fun setWatchStatus(action: ApiAction, animeId: Int, status: WatchStatus): Result<Unit> {
        return when (action) {
            ApiAction.CREATE -> detailDataSource.createWatchStatus(animeId, status)
            ApiAction.UPDATE -> detailDataSource.updateWatchStatus(animeId, status)
            ApiAction.DELETE -> detailDataSource.deleteWatchStatus(animeId)
        }
    }

    override suspend fun setAnimeRating(action: ApiAction, animeId: Int, reviewId: Int, request: ReviewRating): Result<Unit> {
        return when (action) {
            ApiAction.CREATE -> detailDataSource.createAnimeRating(animeId, request)
            ApiAction.UPDATE -> detailDataSource.updateAnimeRating(reviewId, request)
            ApiAction.DELETE -> detailDataSource.deleteAnimeRating(reviewId)
        }
    }

    override suspend fun getStudioInfo(studioId: Int, cursor: Cursor?): Result<DetailStudio> {
        return detailDataSource.getStudioInfo(studioId, cursor)
    }

    override suspend fun getAnimeActors(animeId: Int, cursor: Cursor?): Result<AnimeActorsResponse> {
        return detailDataSource.getAnimeActors(animeId, cursor)
    }

    override suspend fun getActorInfo(personId: Int, cursor: Cursor?): Result<ActorDetailResponse> {
        return detailDataSource.getActorInfo(personId, cursor)
    }

    override suspend fun setLikeActor(action: ApiAction, personId: Int): Result<Unit> {
        return when(action) {
            ApiAction.CREATE -> detailDataSource.likeActor(personId)
            ApiAction.DELETE -> detailDataSource.unLikeActor(personId)
            ApiAction.UPDATE -> Result.failure(IllegalArgumentException("좋아요는 UPDATE 액션을 지원하지 않습니다"))
        }
    }
}