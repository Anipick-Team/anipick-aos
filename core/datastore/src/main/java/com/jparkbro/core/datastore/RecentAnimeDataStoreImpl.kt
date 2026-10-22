package com.jparkbro.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/** v1 키 그대로다. */
private object RecentAnimeKeys {
    val RECENT_ANIME_ID = longPreferencesKey("anime_id")
}

class RecentAnimeDataStoreImpl(
    private val context: Context,
) : RecentAnimeDataStore {
    // 같은 파일의 사용자 정보가 바뀔 때는 다시 내보내지 않는다
    override val recentAnimeId: Flow<Long?> =
        context.anipickDataStore.data.map { it[RecentAnimeKeys.RECENT_ANIME_ID] }.distinctUntilChanged()

    override suspend fun getRecentAnimeId(): Long? = recentAnimeId.first()

    override suspend fun saveRecentAnimeId(animeId: Long) {
        context.anipickDataStore.edit { prefs ->
            prefs[RecentAnimeKeys.RECENT_ANIME_ID] = animeId
        }
    }

    override suspend fun clearRecentAnimeId() {
        context.anipickDataStore.edit { it.remove(RecentAnimeKeys.RECENT_ANIME_ID) }
    }
}
