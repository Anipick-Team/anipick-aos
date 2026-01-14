package com.jparkbro.network.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.studio.GetStudioInfoResponse
import com.jparkbro.network.util.toResult
import javax.inject.Inject

class RetrofitStudioDataSource @Inject constructor(
    private val studioApi: StudioApi
) : StudioDataSource {
    companion object {
        private const val TAG = "RetrofitStudioDataSource"
    }

    override suspend fun getStudioInfo(studioId: Long, cursor: Cursor?): Result<GetStudioInfoResponse> {
        return studioApi.getStudioInfo(
            studioId = studioId,
            lastId = cursor?.lastId,
            lastValue = cursor?.lastValue
        ).toResult(TAG, "getStudioInfo")
    }
}