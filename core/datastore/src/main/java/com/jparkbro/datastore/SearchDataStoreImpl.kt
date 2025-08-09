package com.jparkbro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SearchDataStore {
    companion object {
        private val SEARCH_KEYWORDS_KEY = stringPreferencesKey("search_keywords")
        private const val MAX_SEARCH_KEYWORDS = 10
    }

    override suspend fun saveSearchKeyword(keyWord: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                val currentKeywords = preferences[SEARCH_KEYWORDS_KEY]
                    ?.split(",")
                    ?.filter { it.isNotBlank() }
                    ?.toMutableList()
                    ?: mutableListOf()

                currentKeywords.remove(keyWord)

                if (currentKeywords.size >= MAX_SEARCH_KEYWORDS) {
                    currentKeywords.removeAt(currentKeywords.size - 1)
                }

                currentKeywords.add(0, keyWord)

                preferences[SEARCH_KEYWORDS_KEY] = currentKeywords.joinToString(",")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadSearchKeyword(): Result<List<String>> {
        return try {
            val keywords = dataStore.data
                .map { preferences ->
                    preferences[SEARCH_KEYWORDS_KEY]
                        ?.split(",")
                        ?.filter { it.isNotBlank() }
                        ?: emptyList()
                }
                .first()

            Result.success(keywords)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSearchKeyword(keyWord: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                val currentKeywords = preferences[SEARCH_KEYWORDS_KEY]
                    ?.split(",")
                    ?.filter { it.isNotBlank() }
                    ?.toMutableList()
                    ?: mutableListOf()

                currentKeywords.remove(keyWord)

                preferences[SEARCH_KEYWORDS_KEY] = currentKeywords.joinToString(",")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences.remove(SEARCH_KEYWORDS_KEY)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}