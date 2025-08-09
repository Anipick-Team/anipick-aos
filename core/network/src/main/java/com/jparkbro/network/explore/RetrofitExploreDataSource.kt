package com.jparkbro.network.explore

import com.jparkbro.model.explore.ExploreRequest
import com.jparkbro.model.explore.ExploreResponse
import com.jparkbro.network.util.toResult
import javax.inject.Inject

class RetrofitExploreDataSource @Inject constructor(
    private val exploreApi: ExploreApi
) : ExploreDataSource {
    companion object {
        private const val TAG = "RetrofitHomeDataSource"
    }

    override suspend fun exploreAnime(request: ExploreRequest): Result<ExploreResponse> {
        return exploreApi.exploreAnime(
            year = request.year,
            season = request.season,
            genres = request.genres,
            type = request.type,
            sort = request.sort,
            lastId = request.lastId,
            size = 18,
            genreOp = request.genreOp,
            lastValue = request.lastValue
        ).toResult(TAG, "exploreAnime")
    }
}