package com.jparkbro.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jparkbro.datastore.JwtTokenDataStore
import com.jparkbro.datastore.JwtTokenDataStoreImpl
import com.jparkbro.datastore.RecentAnimeDataStore
import com.jparkbro.datastore.RecentAnimeDataStoreImpl
import com.jparkbro.datastore.SearchDataStore
import com.jparkbro.datastore.SearchDataStoreImpl
import com.jparkbro.datastore.UserDataStore
import com.jparkbro.datastore.UserDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "anipick_preferences")
private val Context.jwtDataStore: DataStore<Preferences> by preferencesDataStore(name = "jwt_tokens_secure")

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    internal abstract fun bindsJwtTokenDataStore(
        jwtTokenDataStoreImpl: JwtTokenDataStoreImpl
    ): JwtTokenDataStore

    @Binds
    internal abstract fun bindsUserDataStore(
        userDataStoreImpl: UserDataStoreImpl
    ): UserDataStore

    @Binds
    internal abstract fun bindsRecentAnimeDataStore(
        recentAnimeDataStoreImpl: RecentAnimeDataStoreImpl
    ): RecentAnimeDataStore

    @Binds
    internal abstract fun bindsSearchDataStore(
        searchDataStoreImpl: SearchDataStoreImpl
    ): SearchDataStore

    companion object {
        @Provides
        @Singleton
        fun providePreferencesDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> {
            return context.dataStore
        }
        
        @Provides
        @Singleton
        @Named("jwt")
        fun provideJwtDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> {
            return context.jwtDataStore
        }
    }
}