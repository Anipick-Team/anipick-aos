package com.jparkbro.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.setting.SettingRepository
import com.jparkbro.domain.LogoutUseCase
import com.jparkbro.domain.UpdateUserUseCase
import com.jparkbro.model.setting.ProfileEditType
import com.jparkbro.model.setting.UpdateUserRequest
import com.jparkbro.model.setting.UserInfo
import com.jparkbro.ui.DialogData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingUiState>(SettingUiState.Loading)
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo = _userInfo.asStateFlow()

    private val _dialogData = MutableStateFlow<DialogData?>(null)
    val dialogData = _dialogData.asStateFlow()

    fun updateDialogData(data: DialogData? = null) {
        _dialogData.value = data
    }

    private val _newNickname = MutableStateFlow("")
    val newNickname = _newNickname.asStateFlow()

    fun updateNewNickname(nickname: String) {
        _newNickname.value = nickname
    }

    private val _newEmail = MutableStateFlow("")
    val newEmail = _newEmail.asStateFlow()

    fun updateNewEmail(email: String) {
        _newEmail.value = email
    }

    private val _currentPassword = MutableStateFlow("")
    val currentPassword = _currentPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private val _newPasswordConfirm = MutableStateFlow("")
    val newPasswordConfirm = _newPasswordConfirm.asStateFlow()

    fun updateCurrentPassword(password: String) {
        _currentPassword.value = password
    }

    fun updateNewPassword(password: String) {
        _newPassword.value = password
    }

    fun updateNewPasswordConfirm(password: String) {
        _newPasswordConfirm.value = password
    }

    init {
        getUserInfo()
        clearNewTextFiled()
    }

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                settingRepository.getUserInfo().fold(
                    onSuccess = { userInfo ->
                        Log.d("SettingViewModel", "Successfully loaded user info")
                        _userInfo.value = userInfo
                        _uiState.value = SettingUiState.Success("사용자 정보 로드 완료")
                    },
                    onFailure = { exception ->
                        Log.e("SettingViewModel", "Failed to load user info", exception)
                        _uiState.value = SettingUiState.Error(exception.message ?: "사용자 정보를 불러오는데 실패했습니다")
                    }
                )
            } catch (e: Exception) {
                Log.e("SettingViewModel", "Unexpected error in getUserInfo", e)
                _uiState.value = SettingUiState.Error(e.message ?: "예상치 못한 오류가 발생했습니다")
            }
        }
    }

    fun clearNewTextFiled() {
        _newNickname.value = ""
        _newEmail.value = ""
        _currentPassword.value = ""
        _newPassword.value = ""
        _newPasswordConfirm.value = ""
    }

    fun updateUserInfo(type: ProfileEditType, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                updateUserUseCase(
                    type = type,
                    request = UpdateUserRequest(
                        nickname = _newNickname.value,
                        email = _newEmail.value,
                        currentPassword = _currentPassword.value,
                        newPassword = _newPassword.value,
                        newPasswordConfirm = _newPasswordConfirm.value
                    )
                ).collect { result ->
                    result.fold(
                        onSuccess = {
                            Log.d("SettingViewModel", "Successfully updated user info: $type")
                            clearNewTextFiled()
                            getUserInfo() // 업데이트 후 정보 새로고침
                            onResult(true)
                        },
                        onFailure = { exception ->
                            Log.e("SettingViewModel", "Failed to update user info: $type", exception)
                            onResult(false)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("SettingViewModel", "Unexpected error in updateUserInfo", e)
                onResult(false)
            }
        }
    }

    fun logout(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                logoutUseCase().collect { result ->
                    result.fold(
                        onSuccess = {
                            Log.d("SettingViewModel", "Successfully logged out")
                            onResult(true)
                        },
                        onFailure = { exception ->
                            Log.e("SettingViewModel", "Failed to logout", exception)
                            onResult(false)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("SettingViewModel", "Unexpected error in logout", e)
                onResult(false)
            }
        }
    }
}

sealed interface SettingUiState {
    data object Loading: SettingUiState
    data class Success(val data: String): SettingUiState
    data class Error(val message: String): SettingUiState
}