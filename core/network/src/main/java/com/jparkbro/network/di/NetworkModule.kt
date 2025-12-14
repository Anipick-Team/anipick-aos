package com.jparkbro.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jparkbro.network.auth.AuthApi
import com.jparkbro.network.auth.AuthDataSource
import com.jparkbro.network.auth.RetrofitAuthDataSource
import com.jparkbro.network.common.CommonApi
import com.jparkbro.network.common.CommonDataSource
import com.jparkbro.network.common.RetrofitCommonDataSource
import com.jparkbro.network.detail.DetailApi
import com.jparkbro.network.detail.DetailDataSource
import com.jparkbro.network.detail.RetrofitDetailDataSource
import com.jparkbro.network.explore.ExploreApi
import com.jparkbro.network.explore.ExploreDataSource
import com.jparkbro.network.explore.RetrofitExploreDataSource
import com.jparkbro.network.home.HomeApi
import com.jparkbro.network.home.HomeDataSource
import com.jparkbro.network.home.RetrofitHomeDataSource
import com.jparkbro.network.interceptor.AuthInterceptor
import com.jparkbro.network.mypage.MyPageApi
import com.jparkbro.network.mypage.MyPageDataSource
import com.jparkbro.network.mypage.RetrofitMyPageDataSource
import com.jparkbro.network.ranking.RankingApi
import com.jparkbro.network.ranking.RankingDataSource
import com.jparkbro.network.ranking.RetrofitRankingDataSource
import com.jparkbro.network.retrofit.ApiConstants
import com.jparkbro.network.review.RetrofitReviewDataSource
import com.jparkbro.network.review.ReviewApi
import com.jparkbro.network.review.ReviewDataSource
import com.jparkbro.network.search.RetrofitSearchDataSource
import com.jparkbro.network.search.SearchApi
import com.jparkbro.network.search.SearchDataSource
import com.jparkbro.network.setting.RetrofitSettingDataSource
import com.jparkbro.network.setting.SettingApi
import com.jparkbro.network.setting.SettingDataSource
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
    fun provideMyPageApi(retrofit: Retrofit): MyPageApi {
        return retrofit.create(MyPageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageDataSource(
        myPageApi: MyPageApi
    ): MyPageDataSource {
        return RetrofitMyPageDataSource(myPageApi)
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
    fun provideRankingApi(retrofit: Retrofit): RankingApi {
        return retrofit.create(RankingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRankingDataSource(rankingApi: RankingApi): RankingDataSource {
        return RetrofitRankingDataSource(rankingApi)
    }

    @Provides
    @Singleton
    fun provideDetailApi(retrofit: Retrofit): DetailApi {
        return retrofit.create(DetailApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailDataSource(detailApi: DetailApi): DetailDataSource {
        return RetrofitDetailDataSource(detailApi)
    }

    @Provides
    @Singleton
    fun provideExploreApi(retrofit: Retrofit): ExploreApi {
        return retrofit.create(ExploreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExploreDataSource(exploreApi: ExploreApi): ExploreDataSource {
        return RetrofitExploreDataSource(exploreApi)
    }

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchDataSource(searchApi: SearchApi): SearchDataSource {
        return RetrofitSearchDataSource(searchApi)
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
    fun provideSettingApi(retrofit: Retrofit): SettingApi {
        return retrofit.create(SettingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSettingDataSource(settingApi: SettingApi): SettingDataSource {
        return RetrofitSettingDataSource(settingApi)
    }
}