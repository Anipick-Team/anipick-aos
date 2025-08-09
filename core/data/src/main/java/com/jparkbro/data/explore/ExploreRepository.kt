package com.jparkbro.data.explore

import com.jparkbro.model.explore.ExploreRequest
import com.jparkbro.model.explore.ExploreResponse

interface ExploreRepository {
    suspend fun exploreAnime(request: ExploreRequest): Result<ExploreResponse>
}