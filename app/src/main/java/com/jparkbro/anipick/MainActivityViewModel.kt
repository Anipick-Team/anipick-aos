package com.jparkbro.anipick

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.common.CommonRepository
import com.jparkbro.domain.AutoLoginUseCase
import com.jparkbro.model.common.AppInitDialogType
import com.jparkbro.model.common.AppInitRequest
import com.jparkbro.model.common.MetaData
import com.jparkbro.ui.DialogData
import com.jparkbro.ui.DialogType
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

    private val _dialogData = MutableStateFlow<DialogData?>(null)
    val dialogData = _dialogData.asStateFlow()

    private val _pendingDeepLink = MutableStateFlow<Uri?>(null)
    val pendingDeepLink = _pendingDeepLink.asStateFlow()

    init {
//        checkAppInit()
        getMetaData()
        autoLogin()
    }

    fun setPendingDeepLink(uri: Uri?) {
        Log.d("MainActivityViewModel", "Setting pending deep link: $uri")
        _pendingDeepLink.value = uri
    }

    fun clearPendingDeepLink() {
        Log.d("MainActivityViewModel", "Clearing pending deep link")
        _pendingDeepLink.value = null
    }

    private fun checkAppInit() {
        val appVersion = BuildConfig.APP_VERSION_NAME
        viewModelScope.launch {
            commonRepository.checkAppInit(appVersion).fold(
                onSuccess = {
                    _dialogData.value = DialogData(
                        type = if (it.type === AppInitDialogType.NOTICE) DialogType.ALERT else DialogType.CONFIRM,
                        title = it.title.toString(),
                        dismiss = "취소",
                        confirm = "확인",
                        onDismiss = {
                            when (it.type) {
                                AppInitDialogType.NOTICE -> { _dialogData.value = null }
                                AppInitDialogType.UPDATE -> {
                                    _dialogData.value = null
                                    if (it.isRequiredUpdate) _uiState.value = MainActivityUiState.FinishApp(null)
                                }
                            }
                        },
                        onConfirm = {
                            when (it.type) {
                                AppInitDialogType.NOTICE -> { _dialogData.value = null }
                                AppInitDialogType.UPDATE -> {
                                    _dialogData.value = null
                                    val playStoreUrl = "market://details?id=${BuildConfig.APPLICATION_ID}"
                                    _uiState.value = MainActivityUiState.FinishApp(playStoreUrl)
                                }
                            }
                        },
                        errorMsg = it.content.toString()
                    )
                },
                onFailure = {
                    Log.e("AppInitCheck", "API Error: ${it.message}")
                    _uiState.value = MainActivityUiState.Error(
                        "네트워크 연결을 확인해주세요.\n앱 초기화에 실패했습니다."
                    )
                }
            )
        }
    }

    fun retryAppInit() {
        _uiState.value = MainActivityUiState.Loading
//        checkAppInit()
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
                    }
                )
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Unexpected error in getMetaData", e)
            }
        }
    }

    private fun autoLogin() {
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
    data class Error(val message: String): MainActivityUiState
    data class FinishApp(val storeUrl: String?): MainActivityUiState

    fun shouldKeepSplashScreen() = this is Loading
}