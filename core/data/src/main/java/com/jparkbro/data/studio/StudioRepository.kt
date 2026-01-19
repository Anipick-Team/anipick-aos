package com.jparkbro.data.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.studio.GetStudioInfoResult

interface StudioRepository {
    suspend fun getStudioInfo(studioId: Long, cursor: Cursor?): Result<GetStudioInfoResult>
}