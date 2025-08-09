package com.jparkbro.anipick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.common.CommonRepository
import com.jparkbro.domain.AutoLoginUserCase
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    private val autoLoginUserCase: AutoLoginUserCase,
) : ViewModel() {

    private val _metaData = MutableStateFlow(MetaData())
    val metaData = _metaData.asStateFlow()

    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getMetaData()
        autoLogin()
    }

    private fun getMetaData() {
        viewModelScope.launch {
            commonRepository.getMetaData().fold(
                onSuccess = {
                    _metaData.value = it
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }

    private fun autoLogin() {
        viewModelScope.launch {
            autoLoginUserCase().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.value = MainActivityUiState.Loading
                    is Result.Success<Boolean> -> _uiState.value = MainActivityUiState.Success(result.data)
                    is Result.Error -> {}
                }
            }
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading: MainActivityUiState
    data class Success(val isAutoLogin: Boolean): MainActivityUiState

    fun shouldKeepSplashScreen() = this is Loading
}