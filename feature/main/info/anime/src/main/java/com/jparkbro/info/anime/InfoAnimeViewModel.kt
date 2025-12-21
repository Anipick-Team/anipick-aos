package com.jparkbro.info.anime

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InfoAnimeViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(InfoAnimeState())
    val state = _state.asStateFlow()

    private val _eventChannel = MutableSharedFlow<InfoAnimeEvent>()
    val events = _eventChannel.asSharedFlow()

    init {

    }

    fun onAction(action: InfoAnimeAction) {

    }

}