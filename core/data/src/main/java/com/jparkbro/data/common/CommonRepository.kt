package com.jparkbro.data.common

import com.jparkbro.model.common.MetaData

interface CommonRepository {
    suspend fun getMetaData(): Result<MetaData>

    suspend fun clearRecentAnime(): Result<Unit>
}