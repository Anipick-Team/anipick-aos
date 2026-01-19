package com.jparkbro.setting.main

import com.jparkbro.ui.model.DialogData

interface SettingEvent {
    data object LogoutSuccess : SettingEvent
    data class ShowDialog(val dialogData: DialogData) : SettingEvent
}