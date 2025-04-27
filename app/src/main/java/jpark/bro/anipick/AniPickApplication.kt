package jpark.bro.anipick

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AniPickApplication : Application() {
    val NATIVE_APP_KEY = "7a6a62132dbfb3253408fdac727f5596"

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this@AniPickApplication, NATIVE_APP_KEY)
    }
}