package com.jparkbro.data.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.jparkbro.data.AuthRepository
import com.jparkbro.data.AuthRepositoryImpl
import com.jparkbro.data.home.HomeRepository
import com.jparkbro.data.home.HomeRepositoryImpl
import com.jparkbro.data.TokenRepositoryImpl
import com.jparkbro.data.UserPreferenceRepositoryImpl
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.common.CommonRepository
import com.jparkbro.data.common.CommonRepositoryImpl
import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.data.detail.DetailRepositoryImpl
import com.jparkbro.data.explore.ExploreRepository
import com.jparkbro.data.explore.ExploreRepositoryImpl
import com.jparkbro.data.mypage.MyPageRepository
import com.jparkbro.data.mypage.MyPageRepositoryImpl
import com.jparkbro.data.ranking.RankingRepository
import com.jparkbro.data.ranking.RankingRepositoryImpl
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.data.review.ReviewRepositoryImpl
import com.jparkbro.data.search.SearchRepository
import com.jparkbro.data.search.SearchRepositoryImpl
import com.jparkbro.data.setting.SettingRepository
import com.jparkbro.data.setting.SettingRepositoryImpl
import com.jparkbro.data.util.image.ImageCompressor
import com.jparkbro.network.mypage.MyPageDataSource
import com.jparkbro.network.repository.TokenRepository
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    internal abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    internal abstract fun bindsUserPreferenceRepository(
        userPreferenceRepositoryImpl: UserPreferenceRepositoryImpl
    ): UserPreferenceRepository

    @Binds
    internal abstract fun bindsHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    internal abstract fun bindsMyPageRepository(
        myPageRepositoryImpl: MyPageRepositoryImpl
    ): MyPageRepository

    @Binds
    internal abstract fun bindsCommonRepository(
        commonRepositoryImpl: CommonRepositoryImpl
    ): CommonRepository

    @Binds
    internal abstract fun bindsRankingRepository(
        rankingRepositoryImpl: RankingRepositoryImpl
    ): RankingRepository

    @Binds
    internal abstract fun bindsDetailRepository(
        detailRepositoryImpl: DetailRepositoryImpl
    ): DetailRepository

    @Binds
    internal abstract fun bindsExploreRepository(
        exploreRepositoryImpl: ExploreRepositoryImpl
    ): ExploreRepository

    @Binds
    internal abstract fun bindsSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    internal abstract fun bindsReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    internal abstract fun bindsSettingRepository(
        settingRepositoryImpl: SettingRepositoryImpl
    ): SettingRepository
}