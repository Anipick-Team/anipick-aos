package com.jparkbro.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jparkbro.datastore.JwtTokenDataStore
import com.jparkbro.datastore.JwtTokenDataStoreImpl
import com.jparkbro.datastore.RecentAnimeDataStore
import com.jparkbro.datastore.RecentAnimeDataStoreImpl
import com.jparkbro.datastore.UserDataStore
import com.jparkbro.datastore.UserDataStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "anipick_preferences")
private val Context.jwtDataStore: DataStore<Preferences> by preferencesDataStore(name = "jwt_tokens_secure")

val dataStoreModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }
    single<DataStore<Preferences>>(named("jwt")) { androidContext().jwtDataStore }

    single<JwtTokenDataStore> { JwtTokenDataStoreImpl(get(named("jwt"))) }
    singleOf(::UserDataStoreImpl) { bind<UserDataStore>() }
    singleOf(::RecentAnimeDataStoreImpl) { bind<RecentAnimeDataStore>() }
}
