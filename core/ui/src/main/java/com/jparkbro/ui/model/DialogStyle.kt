package com.jparkbro.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.AniPickPrimary

data class DialogStyle(
    val titleColor: Color = AniPickBlack,
    val titleAlign: TextAlign = TextAlign.Center,
    val subTitleColor: Color = AniPickGray500,
    val subTitleAlign: TextAlign = TextAlign.Center,
    val buttonColor: Color = AniPickPrimary,
)
