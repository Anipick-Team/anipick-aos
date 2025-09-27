package com.jparkbro.anipick

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.common.CommonRepository
import com.jparkbro.domain.AutoLoginUseCase
import com.jparkbro.model.common.MetaData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    private val autoLoginUseCase: AutoLoginUseCase,
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
            try {
                commonRepository.getMetaData().fold(
                    onSuccess = { metaData ->
                        Log.d("MainActivityViewModel", "Successfully loaded meta data")
                        _metaData.value = metaData
                    },
                    onFailure = { exception ->
                        Log.e("MainActivityViewModel", "Failed to load meta data", exception)
                        // 메타데이터 로드 실패는 앱 사용에 치명적이지 않으므로 기본값 유지
                    }
                )
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Unexpected error in getMetaData", e)
            }
        }
    }

    private fun autoLogin() {
        _uiState.value = MainActivityUiState.Loading
        
        viewModelScope.launch {
            try {
                autoLoginUseCase().collect { result ->
                    result.fold(
                        onSuccess = { isAutoLogin ->
                            Log.d("MainActivityViewModel", "Auto login result: $isAutoLogin")
                            _uiState.value = MainActivityUiState.Success(isAutoLogin)
                        },
                        onFailure = { exception ->
                            Log.e("MainActivityViewModel", "Auto login failed", exception)
                            // 자동 로그인 실패 시 로그인 화면으로 이동 (false)
                            _uiState.value = MainActivityUiState.Success(false)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Unexpected error in autoLogin", e)
                _uiState.value = MainActivityUiState.Success(false)
            }
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading: MainActivityUiState
    data class Success(val isAutoLogin: Boolean): MainActivityUiState

    fun shouldKeepSplashScreen() = this is Loading
}