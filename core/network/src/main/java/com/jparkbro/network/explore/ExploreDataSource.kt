package com.jparkbro.network.explore

import com.jparkbro.model.explore.ExploreRequest
import com.jparkbro.model.explore.ExploreResponse

interface ExploreDataSource {
    suspend fun exploreAnime(
        request: ExploreRequest
    ): Result<ExploreResponse>
}