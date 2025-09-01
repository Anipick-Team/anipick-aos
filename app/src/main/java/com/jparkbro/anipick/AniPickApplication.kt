package com.jparkbro.anipick

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AniPickApplication : Application() {
    val KAKAO_APP_KEY = BuildConfig.KAKAO_APP_KEY

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this@AniPickApplication, KAKAO_APP_KEY)
    }
}