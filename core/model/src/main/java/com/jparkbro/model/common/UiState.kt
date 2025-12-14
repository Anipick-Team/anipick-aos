package com.jparkbro.model.common

sealed interface UiState {
    data object Loading : UiState
    data object Success : UiState
    data object Error : UiState
}

// TODO Error 타입으로 변경