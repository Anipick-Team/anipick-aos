package com.jparkbro.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jparkbro.core.model.setting.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appSettingDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_setting_preferences")

private object AppSettingKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
}

class AppSettingDataStoreImpl(
    private val context: Context,
) : AppSettingDataStore {

    /** 저장된 값이 없거나 알 수 없는 값이면 [ThemeMode.SYSTEM]. */
    override val themeMode: Flow<ThemeMode> = context.appSettingDataStore.data.map { prefs ->
        prefs[AppSettingKeys.THEME_MODE]
            ?.let { name -> ThemeMode.entries.find { it.name == name } }
            ?: ThemeMode.SYSTEM
    }

    override suspend fun saveThemeMode(themeMode: ThemeMode) {
        context.appSettingDataStore.edit { prefs -> prefs[AppSettingKeys.THEME_MODE] = themeMode.name }
    }
}
