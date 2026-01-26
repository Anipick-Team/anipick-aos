package com.jparkbro.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jparkbro.network.actor.ActorApi
import com.jparkbro.network.actor.ActorDataSource
import com.jparkbro.network.actor.RetrofitActorDataSource
import com.jparkbro.network.anime.AnimeApi
import com.jparkbro.network.anime.AnimeDataSource
import com.jparkbro.network.anime.RetrofitAnimeDataSource
import com.jparkbro.network.auth.AuthApi
import com.jparkbro.network.auth.AuthDataSource
import com.jparkbro.network.auth.RetrofitAuthDataSource
import com.jparkbro.network.common.CommonApi
import com.jparkbro.network.common.CommonDataSource
import com.jparkbro.network.common.RetrofitCommonDataSource
import com.jparkbro.network.home.HomeApi
import com.jparkbro.network.home.HomeDataSource
import com.jparkbro.network.home.RetrofitHomeDataSource
import com.jparkbro.network.interceptor.AuthInterceptor
import com.jparkbro.network.retrofit.ApiConstants
import com.jparkbro.network.review.RetrofitReviewDataSource
import com.jparkbro.network.review.ReviewApi
import com.jparkbro.network.review.ReviewDataSource
import com.jparkbro.network.studio.RetrofitStudioDataSource
import com.jparkbro.network.studio.StudioApi
import com.jparkbro.network.studio.StudioDataSource
import com.jparkbro.network.user.RetrofitUserDataSource
import com.jparkbro.network.user.UserApi
import com.jparkbro.network.user.UserDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            prettyPrint = false
            isLenient = true
            encodeDefaults = true
        }
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // token 자동 추가
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthDataSource(
        authApi: AuthApi
    ): AuthDataSource {
        return RetrofitAuthDataSource(authApi)
    }

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeDataSource(
        homeApi: HomeApi
    ): HomeDataSource {
        return RetrofitHomeDataSource(homeApi)
    }

    @Provides
    @Singleton
    fun provideCommonApi(retrofit: Retrofit): CommonApi {
        return retrofit.create(CommonApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommonDataSource(commonApi: CommonApi): CommonDataSource {
        return RetrofitCommonDataSource(commonApi)
    }

    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi {
        return retrofit.create(ReviewApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewDataSource(reviewApi: ReviewApi): ReviewDataSource {
        return RetrofitReviewDataSource(reviewApi)
    }

    @Provides
    @Singleton
    fun provideAnimeApi(retrofit: Retrofit): AnimeApi {
        return retrofit.create(AnimeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAnimeDataSource(animeApi: AnimeApi): AnimeDataSource {
        return RetrofitAnimeDataSource(animeApi)
    }

    @Provides
    @Singleton
    fun provideActorApi(retrofit: Retrofit): ActorApi {
        return retrofit.create(ActorApi::class.java)
    }

    @Provides
    @Singleton
    fun provideActorDataSource(actorApi: ActorApi): ActorDataSource {
        return RetrofitActorDataSource(actorApi)
    }

    @Provides
    @Singleton
    fun provideStudioApi(retrofit: Retrofit): StudioApi {
        return retrofit.create(StudioApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStudioDataSource(studioApi: StudioApi): StudioDataSource {
        return RetrofitStudioDataSource(studioApi)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserDataSource(userApi: UserApi): UserDataSource {
        return RetrofitUserDataSource(userApi)
    }
}