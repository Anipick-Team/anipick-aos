package com.jparkbro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import com.jparkbro.core.datastore.RecentAnimeDataStore as V2RecentAnimeDataStore

/**
 * v2 [V2RecentAnimeDataStore]를 v1 [RecentAnimeDataStore]에 맡긴다. 아직 v1인 애니 상세 화면이 저장한 최근 본 애니를
 * v2 Home의 "최근 본 애니 기반 추천"이 그대로 읽는다. 애니 상세(Catalog)를 v2로 바꾸면 v2 RecentAnimeDataStoreImpl로 되돌린다.
 */
class V1BackedRecentAnimeDataStore(
    private val v1RecentAnimeDataStore: RecentAnimeDataStore,
    v1DataStore: DataStore<Preferences>,
) : V2RecentAnimeDataStore {

    override val recentAnimeId: Flow<Long?> = v1DataStore.data.map { getRecentAnimeId() }.distinctUntilChanged()

    override suspend fun getRecentAnimeId(): Long? = v1RecentAnimeDataStore.loadRecentAnime().getOrNull()

    override suspend fun saveRecentAnimeId(animeId: Long) {
        v1RecentAnimeDataStore.saveRecentAnime(animeId)
    }

    override suspend fun clearRecentAnimeId() {
        v1RecentAnimeDataStore.clearRecentAnime()
    }
}
