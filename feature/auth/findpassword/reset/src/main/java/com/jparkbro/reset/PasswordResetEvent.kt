package com.jparkbro.reset

import com.jparkbro.ui.model.SnackBarData

interface PasswordResetEvent {
    data object PasswordChangeSuccess : PasswordResetEvent
    data class PasswordChangeFailWithSnackBar(val snackBarData: SnackBarData) : PasswordResetEvent
}