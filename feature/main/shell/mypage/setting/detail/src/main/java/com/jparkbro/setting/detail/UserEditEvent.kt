package com.jparkbro.setting.detail

import com.jparkbro.ui.model.DialogData

interface UserEditEvent {
    data object UpdateSuccess : UserEditEvent
    data object WithdrawalSuccess : UserEditEvent
    data class ShowDialog(val dialogData: DialogData) : UserEditEvent
}