package com.jparkbro.datastore

interface RecentAnimeDataStore {
    suspend fun saveRecentAnime(animeId: Int): Result<Unit>
    suspend fun loadRecentAnime(): Result<Int>

    suspend fun clearRecentAnime(): Result<Unit>
}