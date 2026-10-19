package com.jparkbro.core.data.setting

import com.jparkbro.core.datastore.AppSettingDataStore
import com.jparkbro.core.model.setting.ThemeMode
import kotlinx.coroutines.flow.Flow

class AppSettingRepositoryImpl(
    private val appSettingDataStore: AppSettingDataStore,
) : AppSettingRepository {

    override val themeMode: Flow<ThemeMode> = appSettingDataStore.themeMode

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        appSettingDataStore.saveThemeMode(themeMode)
    }
}
