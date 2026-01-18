package com.jparkbro.setting.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.user.UserRepository
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.DialogType
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.snackbar.GlobalSnackbarManager
import com.jparkbro.ui.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val globalSnackbarManager: GlobalSnackbarManager,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<SettingEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        initDataLoad()
        collectUserInfo()
    }

    fun onAction(action: SettingAction) {
        when (action) {
            SettingAction.OnRetryClicked -> retry()
            SettingAction.OnLogoutClicked -> logoutDialog()
        }
    }

    private fun collectUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.getUser().collect { user ->
                _state.update {
                    it.copy(
                        nickname = user?.nickname,
                        email = user?.email,
                        provider = user?.provider ?: LoginProvider.LOCAL,
                    )
                }
            }
        }
    }

    private fun initDataLoad() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.loadUser()
                .fold(
                    onSuccess = { _state.update { it.copy(uiState = UiState.Success) } },
                    onFailure = { _state.update { it.copy(uiState = UiState.Error) } }
                )
        }
    }

    private fun logoutDialog() {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                SettingEvent.ShowDialog(
                    dialogData = DialogData(
                        type = DialogType.CONFIRM,
                        title = UiText.StringResource(R.string.dialog_logout_title),
                        subTitle = UiText.StringResource(R.string.dialog_logout_subtitle),
                        dismiss = UiText.StringResource(R.string.dialog_logout_dismiss),
                        confirm = UiText.StringResource(R.string.dialog_logout_confirm),
                        onConfirm = { logout() }
                    )
                )
            )
        }
    }

    private fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.loadUser()
                .fold(
                    onSuccess = { _eventChannel.send(SettingEvent.LogoutSuccess) },
                    onFailure = {
                        globalSnackbarManager.showSnackbar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_http_500_error)
                            )
                        )
                    }
                )
        }
    }

    private fun retry() {
        _state.update { it.copy(uiState = UiState.Loading) }

        initDataLoad()
    }
}