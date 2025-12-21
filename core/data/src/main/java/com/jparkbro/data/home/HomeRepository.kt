package com.jparkbro.data.home

import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.dto.home.detail.ListDataResult
import com.jparkbro.model.dto.home.main.NextQuarterAnimesResult
import com.jparkbro.model.dto.home.main.RecommendedAnimesResult
import com.jparkbro.model.enum.HomeDetailType
import com.jparkbro.model.home.HomeDetailRequest

interface HomeRepository {
    suspend fun getTrendItems(): Result<List<Anime>>
    suspend fun getRecommendItems(): Result<RecommendedAnimesResult>
    suspend fun getRecentRecommendItems(animeId: Int): Result<RecommendedAnimesResult>
    suspend fun getRecentReviews(): Result<List<Review>>
    suspend fun getNextQuarterAnimes(): Result<NextQuarterAnimesResult>
    suspend fun getComingSoonItems(): Result<List<Anime>>

    suspend fun getDetailData(type: HomeDetailType, request: HomeDetailRequest): Result<ListDataResult>
}