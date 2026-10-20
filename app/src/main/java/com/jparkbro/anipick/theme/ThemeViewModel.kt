package com.jparkbro.anipick.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.data.setting.AppSettingRepository
import com.jparkbro.core.model.setting.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/** 앱 전체에 적용할 화면 모드. 설정 화면에서 바꾸면 바로 반영된다. */
class ThemeViewModel(
    appSettingRepository: AppSettingRepository,
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = appSettingRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)
}
