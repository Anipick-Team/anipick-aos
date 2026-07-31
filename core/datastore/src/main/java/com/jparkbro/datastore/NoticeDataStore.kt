package com.jparkbro.datastore

interface NoticeDataStore {
    suspend fun hasSeenNotice(): Boolean
    suspend fun setNoticeSeen()
}
