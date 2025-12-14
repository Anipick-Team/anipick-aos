package com.jparkbro.verification

import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData

interface PasswordVerificationEvent {
    data class VerificationSuccess(val email: String) : PasswordVerificationEvent
    data class VerificationWithDialog(val dialogData: DialogData) : PasswordVerificationEvent
    data class VerificationWithSnackBar(val snackBarData: SnackBarData) : PasswordVerificationEvent
}