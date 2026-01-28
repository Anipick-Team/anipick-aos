package com.jparkbro.network.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResponse
import com.jparkbro.model.dto.studio.GetStudioInfoResponse
import com.jparkbro.network.util.safeApiCall
import javax.inject.Inject

class RetrofitStudioDataSource @Inject constructor(
    private val studioApi: StudioApi
) : StudioDataSource {
    companion object {
        private const val TAG = "RetrofitStudioDataSource"
    }

    override suspend fun getStudioInfo(studioId: Long, cursor: Cursor?): Result<GetStudioInfoResponse> {
        return safeApiCall(TAG, "getStudioInfo") {
            studioApi.getStudioInfo(
                studioId = studioId,
                lastId = cursor?.lastId,
                lastValue = cursor?.lastValue
            )
        }
    }

    override suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResponse> {
        return safeApiCall(TAG, "getSearchResult") {
            studioApi.getSearchResult(
                query = request.query,
                lastId = request.lastId,
                size = request.size,
            )
        }
    }
}
