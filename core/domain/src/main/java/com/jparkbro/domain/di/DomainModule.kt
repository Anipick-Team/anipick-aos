package com.jparkbro.domain.di

import com.jparkbro.domain.GoogleLoginUseCase
import com.jparkbro.domain.KakaoLoginUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GoogleLoginUseCase)
    factoryOf(::KakaoLoginUseCase)
}
