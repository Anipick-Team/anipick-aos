package com.jparkbro.network.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.studio.GetStudioInfoResponse

interface StudioDataSource {
    suspend fun getStudioInfo(studioId: Long, cursor: Cursor?): Result<GetStudioInfoResponse>
}