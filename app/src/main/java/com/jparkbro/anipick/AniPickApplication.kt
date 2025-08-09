package com.jparkbro.anipick

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AniPickApplication : Application() {
    val NATIVE_APP_KEY = "0342f72c46d8653e8a501fe5704a540c" // 공통

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this@AniPickApplication, NATIVE_APP_KEY)
    }
}