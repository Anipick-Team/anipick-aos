package com.jparkbro.mypage.impl.setting.main

import com.jparkbro.core.model.setting.ThemeMode
import com.jparkbro.mypage.api.SettingDetailType

sealed interface SettingMainAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : SettingMainAction

    data object OnBackClick : Navigation
    data class OnEditProfileClick(val type: SettingDetailType) : Navigation
    data object OnContactClick : Navigation
    data object OnTermsClick : Navigation
    data object OnPrivacyPolicyClick : Navigation
    data object OnOpenSourceLicenseClick : Navigation
    data object OnNoticeClick : Navigation
    data object OnThemeModeClick : SettingMainAction
    data class OnThemeModeSelect(val themeMode: ThemeMode) : SettingMainAction
    data object OnThemeModeDialogDismiss : SettingMainAction
    data object OnLogoutClick : SettingMainAction
    data object OnLogoutConfirm : SettingMainAction
    data object OnLogoutDialogDismiss : SettingMainAction
}
