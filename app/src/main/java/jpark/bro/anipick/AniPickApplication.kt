package jpark.bro.anipick

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AniPickApplication : Application() {
    val NATIVE_APP_KEY = "7a6a62132dbfb3253408fdac727f5596" // 테스트
//    val NATIVE_APP_KEY = "0342f72c46d8653e8a501fe5704a540c" // 공통

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this@AniPickApplication, NATIVE_APP_KEY)
    }
}