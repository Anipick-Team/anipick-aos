package com.jparkbro.data.common

import com.jparkbro.model.common.AppInitResponse
import com.jparkbro.model.common.MetaData

interface CommonRepository {
    suspend fun checkAppInit(userAppVersion: String): Result<AppInitResponse>

    suspend fun getMetaData(): Result<MetaData>

    suspend fun clearRecentAnime(): Result<Unit>
}