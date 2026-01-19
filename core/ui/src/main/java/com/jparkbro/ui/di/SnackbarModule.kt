package com.jparkbro.ui.di

import com.jparkbro.ui.snackbar.GlobalSnackbarManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SnackbarModule {

    @Provides
    @Singleton
    fun provideGlobalSnackbarManager(): GlobalSnackbarManager {
        return GlobalSnackbarManager()
    }
}
