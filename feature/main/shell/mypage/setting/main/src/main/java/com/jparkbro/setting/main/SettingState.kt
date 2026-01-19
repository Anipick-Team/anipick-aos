package com.jparkbro.setting.main

import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.common.UiState

data class SettingState(
    val uiState: UiState = UiState.Loading,

    /* API 통신 데이터 */
    val nickname: String? = "AniPick",
    val email: String? = null,
    val provider: LoginProvider? = null,
)
