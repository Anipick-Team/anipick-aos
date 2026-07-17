package com.jparkbro.anipick

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 인앱 업데이트 관련 SharedPreferences 관리 클래스
 */
class UpdatePreference(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "app_update"
        private const val KEY_LAST_UPDATE_CHECK = "last_update_check"
        private const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L
    }

    /**
     * 업데이트 확인 여부 판단
     * @param daysInterval 확인 간격 (일 단위), 기본값 1일
     * @return true면 업데이트 확인 필요, false면 아직 확인할 시간이 아님
     */
    fun shouldCheckUpdate(daysInterval: Int = 1): Boolean {
        val lastCheck = prefs.getLong(KEY_LAST_UPDATE_CHECK, 0)
        val now = System.currentTimeMillis()
        val intervalMillis = daysInterval * ONE_DAY_MILLIS

        return (now - lastCheck) > intervalMillis
    }

    /**
     * 마지막 업데이트 확인 시간 저장
     */
    fun saveUpdateCheckTime() {
        prefs.edit {
            putLong(KEY_LAST_UPDATE_CHECK, System.currentTimeMillis())
        }
    }

    /**
     * 업데이트 확인 시간 초기화 (테스트용)
     */
    fun clearUpdateCheckTime() {
        prefs.edit {
            remove(KEY_LAST_UPDATE_CHECK)
        }
    }
}
