package com.jparkbro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RecentAnimeDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : RecentAnimeDataStore {
    companion object {
        private val RECENT_ANIME = longPreferencesKey("anime_id")
    }

    override suspend fun saveRecentAnime(animeId: Long): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[RECENT_ANIME] = animeId
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadRecentAnime(): Result<Long> {
        return try {
            val animeId = dataStore.data.first()[RECENT_ANIME]
            if (animeId != null) {
                Result.success(animeId)
            } else {
                Result.success(-1L) // 미추천 처리
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearRecentAnime(): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences.remove(RECENT_ANIME)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}