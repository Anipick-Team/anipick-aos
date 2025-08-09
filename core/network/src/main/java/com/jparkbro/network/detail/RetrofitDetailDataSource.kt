package com.jparkbro.network.detail

import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.detail.DetailActor
import com.jparkbro.model.detail.DetailInfo
import com.jparkbro.model.detail.DetailMyReview
import com.jparkbro.model.detail.DetailSeries
import com.jparkbro.model.detail.ReviewDetailRequest
import com.jparkbro.model.detail.ReviewDetailResponse
import com.jparkbro.model.review.ReviewRating
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import javax.inject.Inject

class RetrofitDetailDataSource @Inject constructor(
    private val detailApi: DetailApi
) : DetailDataSource {
    companion object {
        private const val TAG = "RetrofitDetailDataSource"
    }

    override suspend fun getDetailInfo(animeId: Int): Result<DetailInfo> {
        return detailApi.getDetailInfo(animeId).toResult(TAG, "getDetailInfo")
    }

    override suspend fun getDetailActor(animeId: Int): Result<List<DetailActor>> {
        return detailApi.getDetailActor(animeId).toResult(TAG, "getDetailActor")
    }

    override suspend fun getDetailSeries(animeId: Int): Result<List<DetailSeries>> {
        return detailApi.getDetailSeries(animeId).toResult(TAG, "getDetailSeries")
    }

    override suspend fun getDetailRecommendation(animeId: Int): Result<List<DefaultAnime>> {
        return detailApi.getDetailRecommendation(animeId).toResult(TAG, "getDetailRecommendation")
    }

    override suspend fun getDetailReviews(request: ReviewDetailRequest): Result<ReviewDetailResponse> {
        return detailApi.getDetailReviews(
            animeId = request.animeId,
            sort = request.sort,
            isSpoiler = request.isSpoiler,
            lastValue = request.lastValue,
            lastId = request.lastId,
            size = request.size
        ).toResult(TAG, "getDetailReviews")
    }

    override suspend fun getMyReview(animeId: Int): Result<DetailMyReview> {
        return detailApi.getMyReview(animeId).toResult(TAG, "getMyReview")
    }

    override suspend fun likeAnime(animeId: Int): Result<Unit> {
        return detailApi.setLikeAnime(animeId).toUnitResult(TAG, "likeAnime")
    }

    override suspend fun unLikeAnime(animeId: Int): Result<Unit> {
        return detailApi.setUnLikeAnime(animeId).toUnitResult(TAG, "unLikeAnime")
    }

    override suspend fun createWatchStatus(animeId: Int): Result<Unit> {
        return detailApi.createWatchStatus(animeId).toUnitResult(TAG, "createWatchStatus")
    }

    override suspend fun updateWatchStatus(animeId: Int): Result<Unit> {
        return detailApi.updateWatchStatus(animeId).toUnitResult(TAG, "updateWatchStatus")
    }

    override suspend fun deleteWatchStatus(animeId: Int): Result<Unit> {
        return detailApi.deleteWatchStatus(animeId).toUnitResult(TAG, "deleteWatchStatus")
    }

    override suspend fun createAnimeRating(animeId: Int, request: ReviewRating): Result<Unit> {
        return detailApi.createAnimeRating(animeId, request).toUnitResult(TAG, "deleteAnimeRating")

    }

    override suspend fun updateAnimeRating(reviewId: Int, request: ReviewRating): Result<Unit> {
        return detailApi.updateAnimeRating(reviewId, request).toUnitResult(TAG, "deleteAnimeRating")
    }

    override suspend fun deleteAnimeRating(reviewId: Int): Result<Unit> {
        return detailApi.deleteAnimeRating(reviewId).toUnitResult(TAG, "deleteAnimeRating")
    }
}