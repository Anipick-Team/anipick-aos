package com.jparkbro.network.ranking

import com.jparkbro.model.ranking.RankingRequest
import com.jparkbro.model.ranking.RankingResponse

interface RankingDataSource {
    suspend fun getRealTimeRanking(request: RankingRequest): Result<RankingResponse>
    suspend fun getYearSeasonRanking(request: RankingRequest): Result<RankingResponse>
    suspend fun getAllTimeRanking(request: RankingRequest): Result<RankingResponse>
}