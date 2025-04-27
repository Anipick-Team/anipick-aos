package jpark.bro.anipick.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jpark.bro.anipick.domain.repository.AuthRepository
import jpark.bro.anipick.domain.usecase.AuthenticateWithServerUseCase
import jpark.bro.anipick.domain.usecase.CheckAuthStatusUseCase
import jpark.bro.anipick.domain.usecase.GetAuthStateUseCase
import jpark.bro.anipick.domain.usecase.RefreshTokenUseCase
import jpark.bro.anipick.domain.usecase.SignInWithGoogleUseCase
import jpark.bro.anipick.domain.usecase.SignOutUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSignInWithGoogleUseCase(
        authRepository: AuthRepository
    ): SignInWithGoogleUseCase {
        return SignInWithGoogleUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideGetAuthStateUseCase(
        authRepository: AuthRepository
    ): GetAuthStateUseCase {
        return GetAuthStateUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        authRepository: AuthRepository
    ): SignOutUseCase {
        return SignOutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideAuthenticateWithServerUseCase(
        authRepository: AuthRepository
    ): AuthenticateWithServerUseCase {
        return AuthenticateWithServerUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideRefreshTokenUseCase(
        authRepository: AuthRepository
    ): RefreshTokenUseCase {
        return RefreshTokenUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideCheckAuthStatusUseCase(
        authRepository: AuthRepository
    ): CheckAuthStatusUseCase {
        return CheckAuthStatusUseCase(authRepository)
    }
}