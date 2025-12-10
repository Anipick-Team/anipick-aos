package com.jparkbro.ui.model

import com.jparkbro.ui.util.UiText

data class SnackBarData(
    val text: UiText,
    val duration: Long = 2000,
    val onDismiss: () -> Unit = {},
)