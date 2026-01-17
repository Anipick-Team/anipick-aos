package com.jparkbro.data.di

import com.jparkbro.data.AuthRepository
import com.jparkbro.data.AuthRepositoryImpl
import com.jparkbro.data.TokenRepositoryImpl
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.UserPreferenceRepositoryImpl
import com.jparkbro.data.actor.ActorRepository
import com.jparkbro.data.actor.ActorRepositoryImpl
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.anime.AnimeRepositoryImpl
import com.jparkbro.data.common.CommonRepository
import com.jparkbro.data.common.CommonRepositoryImpl
import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.data.detail.DetailRepositoryImpl
import com.jparkbro.data.explore.ExploreRepository
import com.jparkbro.data.explore.ExploreRepositoryImpl
import com.jparkbro.data.home.HomeRepository
import com.jparkbro.data.home.HomeRepositoryImpl
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
import com.jparkbro.data.studio.StudioRepository
import com.jparkbro.data.studio.StudioRepositoryImpl
import com.jparkbro.data.user.UserRepository
import com.jparkbro.data.user.UserRepositoryImpl
import com.jparkbro.network.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindsTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    internal abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserPreferenceRepository(
        userPreferenceRepositoryImpl: UserPreferenceRepositoryImpl
    ): UserPreferenceRepository

    @Binds
    @Singleton
    internal abstract fun bindsHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    internal abstract fun bindsMyPageRepository(
        myPageRepositoryImpl: MyPageRepositoryImpl
    ): MyPageRepository

    @Binds
    @Singleton
    internal abstract fun bindsCommonRepository(
        commonRepositoryImpl: CommonRepositoryImpl
    ): CommonRepository

    @Binds
    @Singleton
    internal abstract fun bindsRankingRepository(
        rankingRepositoryImpl: RankingRepositoryImpl
    ): RankingRepository

    @Binds
    @Singleton
    internal abstract fun bindsDetailRepository(
        detailRepositoryImpl: DetailRepositoryImpl
    ): DetailRepository

    @Binds
    @Singleton
    internal abstract fun bindsExploreRepository(
        exploreRepositoryImpl: ExploreRepositoryImpl
    ): ExploreRepository

    @Binds
    @Singleton
    internal abstract fun bindsSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    internal abstract fun bindsReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    @Singleton
    internal abstract fun bindsSettingRepository(
        settingRepositoryImpl: SettingRepositoryImpl
    ): SettingRepository

    @Binds
    @Singleton
    internal abstract fun bindsAnimeRepository(
        animeRepositoryImpl: AnimeRepositoryImpl
    ): AnimeRepository

    @Binds
    @Singleton
    internal abstract fun bindsActorRepository(
        actorRepositoryImpl: ActorRepositoryImpl
    ): ActorRepository

    @Binds
    @Singleton
    internal abstract fun bindsStudioRepository(
        studioRepositoryImpl: StudioRepositoryImpl
    ): StudioRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}