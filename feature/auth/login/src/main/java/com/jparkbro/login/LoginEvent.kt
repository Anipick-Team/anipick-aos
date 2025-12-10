package com.jparkbro.login

import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData

sealed interface LoginEvent {
    data class LoginSuccess(val reviewCompletedYn: Boolean) : LoginEvent
    data class LoginFailWithDialog(val dialogData: DialogData) : LoginEvent
    data class LoginFailWithSnackBar(val snackBarData: SnackBarData) : LoginEvent
}