package com.jparkbro.network.ranking

import android.util.Log
import com.jparkbro.model.ranking.RankingRequest
import com.jparkbro.model.ranking.RankingResponse
import retrofit2.HttpException
import javax.inject.Inject

class RetrofitRankingDataSource @Inject constructor(
    private val rankingApi: RankingApi
) : RankingDataSource {
    companion object {
        private const val TAG = "RetrofitRankingDataSource"
    }

    override suspend fun getRealTimeRanking(request: RankingRequest): Result<RankingResponse> {
        Log.d(TAG, "getRealTimeRanking() called")

        return try {
            val response = rankingApi.getRealTimeRanking(
                genre = if (request.genre?.id != -1) request.genre?.id else null,
                lastId = request.lastId,
                size = request.size,
            )
            Log.d(TAG, "getRealTimeRanking() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "getRealTimeRanking() apiResponse - result: ${apiResponse?.result}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "getRealTimeRanking() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure(HttpException(response))
                }

                apiResponse?.value == "success" && apiResponse.result != null -> {
                    Log.d(TAG, "getRealTimeRanking() success - result: ${apiResponse.result}")
                    Result.success(apiResponse.result)
                }

                // value = fail, result == null
                else -> {
                    Log.e(TAG, "getRealTimeRanking() API error - code: ${apiResponse?.code}")
                    Result.failure(Exception("${apiResponse?.code}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getRealTimeRanking() exception", e)
            Result.failure(e)
        }
    }

    override suspend fun getYearSeasonRanking(request: RankingRequest): Result<RankingResponse> {
        Log.d(TAG, "getYearSeasonRanking() called")

        return try {
            val response = rankingApi.getYearSeasonRanking(
                year = if (request.year == "전체년도") null else request.year,
                season = when (request.season) {
                    "1분기" -> 1
                    "2분기" -> 2
                    "3분기" -> 3
                    "4분기" -> 4
                    else -> null
                },
                genre = if (request.genre?.id != -1) request.genre?.id else null,
                lastId = request.lastId,
                size = request.size,
            )
            Log.d(TAG, "getYearSeasonRanking() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "getYearSeasonRanking() apiResponse - result: ${apiResponse?.result}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "getYearSeasonRanking() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure(HttpException(response))
                }

                apiResponse?.value == "success" && apiResponse.result != null -> {
                    Log.d(TAG, "getYearSeasonRanking() success - result: ${apiResponse.result}")
                    Result.success(apiResponse.result)
                }

                // value = fail, result == null
                else -> {
                    Log.e(TAG, "getYearSeasonRanking() API error - code: ${apiResponse?.code}")
                    Result.failure(Exception("${apiResponse?.code}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getYearSeasonRanking() exception", e)
            Result.failure(e)
        }
    }

    override suspend fun getAllTimeRanking(request: RankingRequest): Result<RankingResponse> {
        Log.d(TAG, "getAllTimeRanking() called")

        return try {
            val response = rankingApi.getAllTimeRanking(
                genre = if (request.genre?.id != -1) request.genre?.id else null,
                lastId = request.lastId,
                size = request.size,
            )
            Log.d(TAG, "getAllTimeRanking() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "getAllTimeRanking() apiResponse - result: ${apiResponse?.result}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "getAllTimeRanking() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure(HttpException(response))
                }

                apiResponse?.value == "success" && apiResponse.result != null -> {
                    Log.d(TAG, "getAllTimeRanking() success - result: ${apiResponse.result}")
                    Result.success(apiResponse.result)
                }

                // value = fail, result == null
                else -> {
                    Log.e(TAG, "getAllTimeRanking() API error - code: ${apiResponse?.code}")
                    Result.failure(Exception("${apiResponse?.code}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAllTimeRanking() exception", e)
            Result.failure(e)
        }
    }
}