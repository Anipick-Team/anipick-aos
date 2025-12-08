package com.jparkbro.ui.model

import androidx.compose.runtime.Composable
import com.jparkbro.model.enum.DialogType
import com.jparkbro.ui.util.UiText

data class DialogData(
    val type: DialogType = DialogType.CONFIRM,
    val title: UiText? = null,
    val subTitle: UiText? = null,
    val content: @Composable (() -> Unit)? = null,
    val dismiss: UiText? = null,
    val confirm: UiText? = null,
    val onDismiss: () -> Unit = {},
    val onConfirm: () -> Unit = {}
)