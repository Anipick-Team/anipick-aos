package com.jparkbro.setting.main

import com.jparkbro.model.enum.UserEditType

interface SettingAction {
    data object OnRetryClicked : SettingAction
    data object NavigateBack : SettingAction
    data class NavigateToUserEditForm(val editType: UserEditType) : SettingAction
    data object OnLogoutClicked : SettingAction
    data object NavigateToCustomerSupport : SettingAction
    data object NavigateToServiceTerms : SettingAction
    data object NavigateToPrivacyPolicy : SettingAction
    data object NavigateToOpenSourceLicense : SettingAction
    data object NavigateToNotice : SettingAction
}