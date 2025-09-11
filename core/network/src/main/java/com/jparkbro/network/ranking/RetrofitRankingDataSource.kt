package com.jparkbro.network.ranking

import android.util.Log
import com.jparkbro.model.ranking.RankingRequest
import com.jparkbro.model.ranking.RankingResponse
import com.jparkbro.network.util.toResult
import retrofit2.HttpException
import javax.inject.Inject

class RetrofitRankingDataSource @Inject constructor(
    private val rankingApi: RankingApi
) : RankingDataSource {
    companion object {
        private const val TAG = "RetrofitRankingDataSource"
    }

    override suspend fun getRealTimeRanking(request: RankingRequest): Result<RankingResponse> {
        return rankingApi.getRealTimeRanking(
            genre = if (request.genre?.id != -1) request.genre?.name else null,
            lastId = request.lastId,
            lastValue = request.lastValue,
            size = request.size,
        ).toResult(TAG, "getRealTimeRanking")
    }

    override suspend fun getYearSeasonRanking(request: RankingRequest): Result<RankingResponse> {
        return rankingApi.getYearSeasonRanking(
            year = if (request.year == "전체년도") null else request.year,
            season = when (request.season) {
                "1분기" -> 1
                "2분기" -> 2
                "3분기" -> 3
                "4분기" -> 4
                else -> null
            },
            genre = if (request.genre?.id != -1) request.genre?.name else null,
            lastId = request.lastId,
            lastRank = request.lastRank,
            size = request.size,
        ).toResult(TAG, "getYearSeasonRanking")
    }

    override suspend fun getAllTimeRanking(request: RankingRequest): Result<RankingResponse> {
        return rankingApi.getAllTimeRanking(
            genre = if (request.genre?.id != -1) request.genre?.name else null,
            lastId = request.lastId,
            lastRank = request.lastRank,
            size = request.size,
        ).toResult(TAG, "getAllTimeRanking")
    }
}