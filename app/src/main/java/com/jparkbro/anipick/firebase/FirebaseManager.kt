package com.jparkbro.anipick.firebase

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.jparkbro.anipick.BuildConfig

class FirebaseManager(private val context: Context) {

    private val analytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    private val crashlytics: FirebaseCrashlytics by lazy {
        FirebaseCrashlytics.getInstance()
    }

    private val performance: FirebasePerformance by lazy {
        FirebasePerformance.getInstance()
    }

    fun initialize() {
        setupAnalytics()
        setupCrashlytics()
        setupPerformance()
    }

    private fun setupAnalytics() {
        analytics.apply {
            setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Analytics: 디버그 모드 - 수집 비활성화")
            } else {
                Log.d(TAG, "Analytics: 릴리즈 모드 - 수집 활성화")
            }
        }
    }

    private fun setupCrashlytics() {
        crashlytics.apply {
            setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

            setCustomKey("app_version", BuildConfig.VERSION_NAME)
            setCustomKey("build_type", if (BuildConfig.DEBUG) "debug" else "release")

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Crashlytics: 디버그 모드 - 수집 비활성화")
            } else {
                Log.d(TAG, "Crashlytics: 릴리즈 모드 - 수집 활성화")
            }
        }
    }

    private fun setupPerformance() {
        performance.apply {
            isPerformanceCollectionEnabled = !BuildConfig.DEBUG

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Performance: 디버그 모드 - 수집 비활성화")
            } else {
                Log.d(TAG, "Performance: 릴리즈 모드 - 수집 활성화")
            }
        }
    }

    fun logScreenView(screenName: String, screenClass: String) {
        if (BuildConfig.DEBUG) return

        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        })
    }

    fun setUserId(userId: String?) {
        if (BuildConfig.DEBUG) return

        analytics.setUserId(userId)
        crashlytics.setUserId(userId ?: "anonymous")
    }

    fun setUserProperty(key: String, value: String) {
        if (BuildConfig.DEBUG) return

        analytics.setUserProperty(key, value)
        crashlytics.setCustomKey(key, value)
    }

    fun logCrashlyticsMessage(message: String) {
        crashlytics.log(message)
    }

    fun logNonFatalError(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    companion object {
        private const val TAG = "FirebaseManager"

        @Volatile
        private var instance: FirebaseManager? = null

        fun getInstance(context: Context): FirebaseManager {
            return instance ?: synchronized(this) {
                instance ?: FirebaseManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}
