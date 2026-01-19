package com.jparkbro.datastore

interface RecentAnimeDataStore {
    suspend fun saveRecentAnime(animeId: Long): Result<Unit>
    suspend fun loadRecentAnime(): Result<Long>

    suspend fun clearRecentAnime(): Result<Unit>
}