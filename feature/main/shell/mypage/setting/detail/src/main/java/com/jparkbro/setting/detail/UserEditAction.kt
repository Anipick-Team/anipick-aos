package com.jparkbro.setting.detail

interface UserEditAction {
    data object NavigateBack : UserEditAction
    data object OnNicknameChanged : UserEditAction
    data object OnEmailChanged : UserEditAction
    data object OnPasswordChanged : UserEditAction
    data object OnWithdrawalClicked : UserEditAction
}