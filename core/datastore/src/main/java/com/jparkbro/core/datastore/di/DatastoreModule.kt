package com.jparkbro.core.datastore.di

import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.datastore.AppSettingDataStore
import com.jparkbro.core.datastore.AppSettingDataStoreImpl
import com.jparkbro.core.datastore.RecentAnimeDataStore
import com.jparkbro.core.datastore.RecentSearchDataStore
import com.jparkbro.core.datastore.RecentSearchDataStoreImpl
import com.jparkbro.core.datastore.UserDataStore
import com.jparkbro.core.datastore.UserDataStoreImpl
import com.jparkbro.datastore.JwtTokenProvider
import com.jparkbro.datastore.V1BackedRecentAnimeDataStore
import com.jparkbro.datastore.V1BackedUserDataStore
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val datastoreModule = module {
    // 토큰은 v1 JwtTokenDataStore와 공유한다 (com.jparkbro.datastore.JwtTokenProvider 참고)
    single<TokenProvider> { JwtTokenProvider(get(), get(named("jwt"))) }
    // 최근 본 애니는 v1 RecentAnimeDataStore와 공유한다 (com.jparkbro.datastore.V1BackedRecentAnimeDataStore 참고)
    single<RecentAnimeDataStore> { V1BackedRecentAnimeDataStore(get(), get()) }
    singleOf(::RecentSearchDataStoreImpl) { bind<RecentSearchDataStore>() }
    singleOf(::AppSettingDataStoreImpl) { bind<AppSettingDataStore>() }
    // userId/nickname은 v1 UserDataStore와 공유한다 (com.jparkbro.datastore.V1BackedUserDataStore 참고)
    single<UserDataStore> { V1BackedUserDataStore(get(), get(), UserDataStoreImpl(get())) }
}
