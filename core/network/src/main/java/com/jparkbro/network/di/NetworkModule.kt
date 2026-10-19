package com.jparkbro.network.di

import com.jparkbro.network.auth.AuthDataSource
import com.jparkbro.network.auth.KtorAuthDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * v1 소셜 로그인 서버 호출(`POST oauth/{provider}/callback`)만 남아 있다.
 * 토큰 없이 부르는 API라 v2 HttpClient(인증 플러그인 포함)와 따로 둔다. JSON 설정은 v1 그대로다.
 */
val networkModule = module {
    single(named("v1")) {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            prettyPrint = false
            isLenient = true
            encodeDefaults = true
        }
    }

    single(named("v1")) {
        HttpClient(CIO) {
            expectSuccess = false
            install(HttpTimeout) {
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
        }
    }

    single<AuthDataSource> { KtorAuthDataSource(get(named("v1")), get(named("v1"))) }
}
