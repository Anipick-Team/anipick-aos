package com.jparkbro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NoticeDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : NoticeDataStore {

    companion object {
        private val NOTICE_SEEN_KEY = booleanPreferencesKey("notice_seen")
    }

    override suspend fun hasSeenNotice(): Boolean {
        return dataStore.data.first()[NOTICE_SEEN_KEY] ?: false
    }

    override suspend fun setNoticeSeen() {
        dataStore.edit { preferences ->
            preferences[NOTICE_SEEN_KEY] = true
        }
    }
}
