package com.jparkbro.data.ranking

import android.util.Log
import com.jparkbro.model.ranking.RankingRequest
import com.jparkbro.model.ranking.RankingResponse
import com.jparkbro.model.ranking.RankingType
import com.jparkbro.network.ranking.RankingDataSource
import javax.inject.Inject

class RankingRepositoryImpl @Inject constructor(
    private val rankingDataSource: RankingDataSource
) : RankingRepository {

    override suspend fun getAnimesRank(request: RankingRequest): Result<RankingResponse> {
        return when (request.type) {
            RankingType.REAL_TIME -> rankingDataSource.getRealTimeRanking(request)
            RankingType.YEAR_SEASON -> rankingDataSource.getYearSeasonRanking(request)
            RankingType.ALL_TIME -> rankingDataSource.getAllTimeRanking(request)
        }
    }
}