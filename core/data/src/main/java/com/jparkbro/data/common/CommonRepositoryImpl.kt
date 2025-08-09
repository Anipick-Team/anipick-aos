package com.jparkbro.data.common

import com.jparkbro.datastore.RecentAnimeDataStore
import com.jparkbro.model.common.MetaData
import com.jparkbro.network.common.CommonDataSource
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonDataSource: CommonDataSource,
    private val recentAnimeDataStore: RecentAnimeDataStore,
) : CommonRepository {

    override suspend fun getMetaData(): Result<MetaData> {
        return commonDataSource.getMetaData()
    }

    override suspend fun clearRecentAnime(): Result<Unit> {
        return recentAnimeDataStore.clearRecentAnime()
    }
}