package com.jparkbro.data.detail

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

interface DetailRepository {
    suspend fun getDetailInfo(animeId: Int): Result<DetailInfo>
    suspend fun getDetailActor(animeId: Int): Result<List<DetailActor>>
    suspend fun getDetailSeries(animeId: Int): Result<List<DetailSeries>>
    suspend fun getDetailRecommendation(animeId: Int): Result<List<DefaultAnime>>
    suspend fun getDetailReviews(request: ReviewDetailRequest): Result<ReviewDetailResponse>
    suspend fun getMyReview(animeId: Int): Result<DetailMyReview>

    suspend fun saveRecentAnime(animeId: Int): Result<Unit>
    suspend fun loadRecentAnime(): Result<Int>

    suspend fun setLikeAnime(action: ApiAction, animeId: Int): Result<Unit>
    suspend fun setWatchStatus(action: ApiAction, animeId: Int, status: WatchStatus): Result<Unit>
    suspend fun setAnimeRating(action: ApiAction, animeId: Int, reviewId: Int, request: ReviewRating): Result<Unit>

    suspend fun getStudioInfo(studioId: Int, cursor: Cursor?): Result<DetailStudio>
    suspend fun getAnimeActors(animeId: Int, cursor: Cursor?): Result<AnimeActorsResponse>
    suspend fun getActorInfo(personId: Int, cursor: Cursor?): Result<ActorDetailResponse>
    suspend fun setLikeActor(action: ApiAction, personId: Int): Result<Unit>
}