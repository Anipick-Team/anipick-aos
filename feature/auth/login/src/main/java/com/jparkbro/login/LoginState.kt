package com.jparkbro.login

import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData

data class LoginState(
    val snackBarQueue: List<SnackBarData> = emptyList(),
    val dialogData: DialogData? = null
)
