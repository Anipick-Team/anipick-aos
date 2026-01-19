package com.jparkbro.data.studio

import com.jparkbro.model.common.Cursor
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
}