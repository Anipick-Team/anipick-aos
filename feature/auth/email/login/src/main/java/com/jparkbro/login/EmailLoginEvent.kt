package com.jparkbro.login

import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData

sealed interface EmailLoginEvent {
    data class LoginSuccess(val reviewCompletedYn: Boolean) : EmailLoginEvent
    data class LoginFailWithDialog(val dialogData: DialogData) : EmailLoginEvent
    data class LoginFailWithSnackBar(val snackBarData: SnackBarData) : EmailLoginEvent
}