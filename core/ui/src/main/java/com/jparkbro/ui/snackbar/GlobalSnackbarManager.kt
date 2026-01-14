package com.jparkbro.ui.snackbar

import com.jparkbro.ui.model.SnackBarData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalSnackbarManager @Inject constructor() {

    private val _snackbarEvents = MutableSharedFlow<SnackBarData>()
    val snackbarEvents = _snackbarEvents.asSharedFlow()

    suspend fun showSnackbar(data: SnackBarData) {
        _snackbarEvents.emit(data)
    }
}
