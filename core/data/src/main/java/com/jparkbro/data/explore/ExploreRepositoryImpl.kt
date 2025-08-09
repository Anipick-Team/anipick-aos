package com.jparkbro.data.explore

import com.jparkbro.model.explore.ExploreRequest
import com.jparkbro.model.explore.ExploreResponse
import com.jparkbro.network.explore.ExploreDataSource
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val exploreDataSource: ExploreDataSource
) : ExploreRepository {
    override suspend fun exploreAnime(request: ExploreRequest): Result<ExploreResponse> {
        return exploreDataSource.exploreAnime(request)
    }
}