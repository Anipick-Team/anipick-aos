package com.jparkbro.data.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResponse
import com.jparkbro.model.dto.search.GetSearchResultResult
import com.jparkbro.model.dto.studio.GetStudioInfoResult
import com.jparkbro.model.search.SearchResultResponse

interface StudioRepository {
    suspend fun getStudioInfo(studioId: Long, cursor: Cursor?): Result<GetStudioInfoResult>

    /** Search */
    suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResult>
}