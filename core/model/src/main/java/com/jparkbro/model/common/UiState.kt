package com.jparkbro.model.common

sealed interface UiState {
    data object Loading : UiState
    data object Success : UiState
    data object Error : UiState
}