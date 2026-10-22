package com.jparkbro.core.datastore.di

import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.datastore.AppSettingDataStore
import com.jparkbro.core.datastore.AppSettingDataStoreImpl
import com.jparkbro.core.datastore.RecentAnimeDataStore
import com.jparkbro.core.datastore.RecentAnimeDataStoreImpl
import com.jparkbro.core.datastore.RecentSearchDataStore
import com.jparkbro.core.datastore.RecentSearchDataStoreImpl
import com.jparkbro.core.datastore.TokenDataStore
import com.jparkbro.core.datastore.UserDataStore
import com.jparkbro.core.datastore.UserDataStoreImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val datastoreModule = module {
    singleOf(::TokenDataStore) { bind<TokenProvider>() }
    singleOf(::UserDataStoreImpl) { bind<UserDataStore>() }
    singleOf(::RecentAnimeDataStoreImpl) { bind<RecentAnimeDataStore>() }
    singleOf(::RecentSearchDataStoreImpl) { bind<RecentSearchDataStore>() }
    singleOf(::AppSettingDataStoreImpl) { bind<AppSettingDataStore>() }
}
