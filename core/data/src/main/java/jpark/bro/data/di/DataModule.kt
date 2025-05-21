package jpark.bro.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jpark.bro.data.AuthRepository
import jpark.bro.data.DefaultAuthRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsAuthRepository(
        authRepository: DefaultAuthRepository,
    ): AuthRepository

}