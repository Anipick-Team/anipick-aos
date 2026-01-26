package com.jparkbro.data.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResult
import com.jparkbro.model.dto.studio.GetStudioInfoResult

interface StudioRepository {
    suspend fun getStudioInfo(studioId: Long, cursor: Cursor?): Result<GetStudioInfoResult>

    /** Search */
    suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResult>
}