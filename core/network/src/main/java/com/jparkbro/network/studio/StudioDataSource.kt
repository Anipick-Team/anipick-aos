package com.jparkbro.network.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResponse
import com.jparkbro.model.dto.studio.GetStudioInfoResponse

interface StudioDataSource {
    suspend fun getStudioInfo(studioId: Long, cursor: Cursor?): Result<GetStudioInfoResponse>

    /** Search */
    suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResponse>
}