package com.jparkbro.data.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResponse
import com.jparkbro.model.dto.search.GetSearchResultResult
import com.jparkbro.model.dto.search.toResult
import com.jparkbro.model.dto.studio.GetStudioInfoResult
import com.jparkbro.model.dto.studio.toResult
import com.jparkbro.network.studio.StudioDataSource
import javax.inject.Inject

class StudioRepositoryImpl @Inject constructor(
    private val studioDataSource: StudioDataSource
) : StudioRepository {

    override suspend fun getStudioInfo(studioId: Long, cursor: Cursor?): Result<GetStudioInfoResult> {
        return studioDataSource.getStudioInfo(studioId, cursor).map { it.toResult() }
    }

    override suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResult> {
        return studioDataSource.getSearchResult(request)
            .map { it.toResult() }
    }
}