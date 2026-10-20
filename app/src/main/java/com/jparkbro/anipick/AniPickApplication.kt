package com.jparkbro.anipick

import android.app.Application
import com.jparkbro.anipick.di.appModule
import com.jparkbro.anipick.firebase.FirebaseManager
import com.jparkbro.auth.impl.di.authModule
import com.jparkbro.catalog.impl.di.catalogModule
import com.jparkbro.community.impl.di.communityModule
import com.jparkbro.core.data.di.dataModule as v2DataModule
import com.jparkbro.core.datastore.di.datastoreModule
import com.jparkbro.core.network.di.networkModule as v2NetworkModule
import com.jparkbro.core.ui.di.uiModule
import com.jparkbro.datastore.di.dataStoreModule
import com.jparkbro.explore.impl.di.exploreModule
import com.jparkbro.home.impl.di.homeModule
import com.jparkbro.mypage.impl.di.myPageModule
import com.jparkbro.network.di.networkModule
import com.jparkbro.ranking.impl.di.rankingModule
import com.jparkbro.review.impl.di.reviewModule
import com.jparkbro.search.impl.di.searchModule
import com.jparkbro.splash.impl.di.splashModule
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AniPickApplication : Application() {
    val KAKAO_APP_KEY = BuildConfig.KAKAO_APP_KEY

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        KakaoSdk.init(this@AniPickApplication, KAKAO_APP_KEY)

        startKoin {
            androidContext(this@AniPickApplication)

            modules(
                appModule,

                // v2 core (v1 core와 공존, 화면을 v2로 바꾸면서 v1 쪽을 걷어낸다)
                v2NetworkModule,
                datastoreModule,
                v2DataModule,
                uiModule,

                splashModule,
                authModule,
                homeModule,
                rankingModule,
                exploreModule,
                myPageModule,
                reviewModule,
                searchModule,
                catalogModule,
                communityModule,

                networkModule,
                dataStoreModule,
            )
        }

        FirebaseManager.getInstance(this).initialize()
    }
}