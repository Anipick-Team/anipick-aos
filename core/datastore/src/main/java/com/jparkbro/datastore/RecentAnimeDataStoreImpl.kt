package com.jparkbro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RecentAnimeDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : RecentAnimeDataStore {
    companion object {
        private val RECENT_ANIME = intPreferencesKey("anime_id")
    }

    override suspend fun saveRecentAnime(animeId: Int): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[RECENT_ANIME] = animeId
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadRecentAnime(): Result<Int> {
        return try {
            val nickname = dataStore.data.first()[RECENT_ANIME]
            if (nickname != null) {
                Result.success(nickname)
            } else {
                Result.success(-1) // 미추천 처리
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