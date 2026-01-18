package com.jparkbro.mypage.detail

import com.jparkbro.ui.model.DialogData

interface UserContentEvent {
    data class ShowDialog(val dialogData: DialogData) : UserContentEvent
}