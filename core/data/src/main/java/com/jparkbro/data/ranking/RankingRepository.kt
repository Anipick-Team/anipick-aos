package com.jparkbro.data.ranking

import com.jparkbro.model.ranking.RankingRequest
import com.jparkbro.model.ranking.RankingResponse

interface RankingRepository {
    suspend fun getAnimesRank(request: RankingRequest): Result<RankingResponse>
}