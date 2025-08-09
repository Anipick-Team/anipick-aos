package com.jparkbro.data.home

import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.model.home.ContentType
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.model.home.HomeDetailResponse
import com.jparkbro.model.home.HomeRecommendResponse
import com.jparkbro.model.home.HomeReviewItem
import com.jparkbro.model.home.UpcomingSeasonItems

interface HomeRepository {
    suspend fun getTrendItems(): Result<List<DefaultAnime>>
    suspend fun getRecommendItems(): Result<HomeRecommendResponse>
    suspend fun getRecentRecommendItems(animeId: Int): Result<HomeRecommendResponse>
    suspend fun getRecentReviews(): Result<List<HomeReviewItem>>
    suspend fun getUpcomingSeasonItems(): Result<UpcomingSeasonItems>
    suspend fun getComingSoonItems(): Result<List<ComingSoonItem>>

    suspend fun getDetailData(type: ContentType, request: HomeDetailRequest): Result<HomeDetailResponse>
}