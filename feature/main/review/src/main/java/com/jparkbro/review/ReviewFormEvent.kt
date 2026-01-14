package com.jparkbro.review

import com.jparkbro.ui.model.SnackBarData

sealed interface ReviewFormEvent {
    data object NavigateBack : ReviewFormEvent
}