package com.jparkbro.core.data.setting

import com.jparkbro.core.model.setting.ThemeMode
import kotlinx.coroutines.flow.Flow

/** 기기 단위 앱 설정(화면 모드 등)을 읽고 쓰는 인터페이스 */
interface AppSettingRepository {
    /** 화면 모드 - 저장된 값이 없으면 [ThemeMode.SYSTEM]. */
    val themeMode: Flow<ThemeMode>

    suspend fun setThemeMode(themeMode: ThemeMode)
}
