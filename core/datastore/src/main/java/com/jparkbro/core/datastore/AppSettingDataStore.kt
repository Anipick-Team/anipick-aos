package com.jparkbro.core.datastore

import com.jparkbro.core.model.setting.ThemeMode
import kotlinx.coroutines.flow.Flow

/** 로그인 여부와 상관없는 기기 단위 앱 설정을 저장한다. 로그아웃해도 지우지 않는다. */
interface AppSettingDataStore {
    val themeMode: Flow<ThemeMode>

    suspend fun saveThemeMode(themeMode: ThemeMode)
}
