package com.jparkbro.register

import com.jparkbro.ui.model.SnackBarData

interface EmailRegisterEvent {
    data object RegisterSuccess : EmailRegisterEvent
    data class RegisterFailWithSnackBar(val snackBarData: SnackBarData) : EmailRegisterEvent
}