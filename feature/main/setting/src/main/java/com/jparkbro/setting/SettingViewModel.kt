package com.jparkbro.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.setting.SettingRepository
import com.jparkbro.domain.LogoutUseCase
import com.jparkbro.domain.UpdateUserUseCase
import com.jparkbro.model.common.Result
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

    private val _uiState = MutableStateFlow(SettingUiState.Loading)
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
            settingRepository.getUserInfo().fold(
                onSuccess = {
                    _userInfo.value = it
                },
                onFailure = {
                    // TODO
                }
            )
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
                when (result) {
                    is Result.Loading -> {}
                    is Result.Error -> { onResult(false) }
                    is Result.Success<Unit> -> { onResult(true) }
                }
            }
        }
    }

    fun logout(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            logoutUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Error -> onResult(false)
                    is Result.Success<Unit> -> onResult(true)
                }
            }
        }
    }
}

sealed interface SettingUiState {
    data object Loading: SettingUiState
    data class Success(val data: String): SettingUiState
}