package com.jparkbro.data.di

import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.UserPreferenceRepositoryImpl
import com.jparkbro.data.auth.AuthRepository
import com.jparkbro.data.auth.AuthRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/** v1 소셜 로그인(GoogleLoginUseCase·KakaoLoginUseCase) 경로에 필요한 것만 남아 있다. */
val dataModule = module {
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::UserPreferenceRepositoryImpl) { bind<UserPreferenceRepository>() }
}
