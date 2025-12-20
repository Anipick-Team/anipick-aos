package com.jparkbro.home.detail

import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData

interface HomeDetailEvent {
    data class DataLoadFailed(val msg: String) : HomeDetailEvent
    data class ShowDialog(val dialogData: DialogData) : HomeDetailEvent
    data class ShowSnackBar(val snackBarData: SnackBarData) : HomeDetailEvent
}