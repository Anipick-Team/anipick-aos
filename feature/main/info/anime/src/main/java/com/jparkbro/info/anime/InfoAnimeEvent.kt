package com.jparkbro.info.anime

import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData

interface InfoAnimeEvent {
    data class ShowDialog(val dialogData: DialogData) : InfoAnimeEvent
    data class ShowSnackBar(val snackBarData: SnackBarData) : InfoAnimeEvent
}