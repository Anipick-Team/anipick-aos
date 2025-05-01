package jpark.bro.anipick.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jpark.bro.data.common.ActivityProviderImpl
import jpark.bro.domain.common.ActivityProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityProviderModule {

    @Binds
    abstract fun bindActivityProvider (
        activityProviderImpl: ActivityProviderImpl
    ): ActivityProvider
}