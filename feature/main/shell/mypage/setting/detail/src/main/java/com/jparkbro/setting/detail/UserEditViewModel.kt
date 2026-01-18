package com.jparkbro.setting.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.user.UserRepository
import com.jparkbro.model.dto.mypage.useredit.UpdateEmailRequest
import com.jparkbro.model.dto.mypage.useredit.UpdateNicknameRequest
import com.jparkbro.model.dto.mypage.useredit.UpdatePasswordRequest
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.UserEditType
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.snackbar.GlobalSnackbarManager
import com.jparkbro.ui.util.UiText
import com.jparkbro.util.UserDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val globalSnackbarManager: GlobalSnackbarManager,
    private val userDataValidator: UserDataValidator,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val editType = savedStateHandle.get<UserEditType>("editType")

    private val _state = MutableStateFlow(UserEditState(editType = editType ?: UserEditType.NICKNAME))
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<UserEditEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        collectUserInfo()
        validate()
    }

    fun onAction(action: UserEditAction) {
        when (action) {
            UserEditAction.OnEmailChanged -> updateEmail()
            UserEditAction.OnNicknameChanged -> updateNickname()
            UserEditAction.OnPasswordChanged -> updatePassword()
            UserEditAction.OnWithdrawalClicked -> withdrawalDialog()
        }
    }

    private fun collectUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.getUser().collect { user ->
                _state.update {
                    it.copy(
                        nickname = TextFieldState(initialText = user?.nickname ?: ""),
                        email = TextFieldState(initialText = user?.email ?: ""),
                    )
                }
            }
        }
    }

    private fun validate() {
        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow { _state.value.newPassword.text.toString() }
                .collectLatest { password ->
                    val isValid = userDataValidator.isValidPassword(password)

                    _state.update { it.copy(isNewPasswordValid = isValid) }
                }
        }

        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow { _state.value.newEmail.text.toString() }
                .collectLatest { email ->
                    val isValid = userDataValidator.isValidEmail(email)

                    _state.update {
                        it.copy(
                            isNewEmailValid = isValid,
                            emailErrorMessage = when {
                                !isValid.isBlank -> null
                                !isValid.matchesEmailPattern -> {
                                    UiText.StringResource(R.string.error_email_invalid_format)
                                }
                                !isValid.hasMaxLength -> {
                                    UiText.StringResource(R.string.error_email_too_long)
                                }
                                else -> null
                            },
                        )
                    }
                }
        }
    }

    private fun updateEmail() {
        _state.update { it.copy(
            isLoading = true,
            emailErrorMessage = null,
            currentPasswordErrorMessage = null,
        ) }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateEmail(
                request = UpdateEmailRequest(
                    newEmail = _state.value.newEmail.text.toString(),
                    password = _state.value.currentPassword.text.toString()
                )
            ).fold(
                onSuccess = { _eventChannel.send(UserEditEvent.UpdateSuccess) },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> when (exception.errorCode) {
                            102, 103, 109 -> _state.update { it.copy(emailErrorMessage = UiText.DynamicString(exception.errorValue)) }
                            106 -> _state.update { it.copy(currentPasswordErrorMessage = UiText.DynamicString(exception.errorValue)) }
                            else -> {
                                globalSnackbarManager.showSnackbar(
                                    SnackBarData(
                                        text = UiText.StringResource(R.string.snackbar_http_500_error)
                                    )
                                )
                            }
                        }
                        else -> {
                            globalSnackbarManager.showSnackbar(
                                SnackBarData(
                                    text = UiText.StringResource(R.string.snackbar_http_500_error)
                                )
                            )
                        }
                    }
                }
            )
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun updateNickname() {
        _state.update { it.copy(
            isLoading = true,
            nicknameErrorMessage = null,
        ) }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateNickname(
                request = UpdateNicknameRequest(
                    nickname = _state.value.newNickname.text.toString()
                )
            ).fold(
                onSuccess = { _eventChannel.send(UserEditEvent.UpdateSuccess) },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> when (exception.errorCode) {
                            116, 117, 118 -> _state.update { it.copy(nicknameErrorMessage = UiText.DynamicString(exception.errorValue)) }
                            else -> {
                                globalSnackbarManager.showSnackbar(
                                    SnackBarData(
                                        text = UiText.StringResource(R.string.snackbar_http_500_error)
                                    )
                                )
                            }
                        }
                        else -> {
                            globalSnackbarManager.showSnackbar(
                                SnackBarData(
                                    text = UiText.StringResource(R.string.snackbar_http_500_error)
                                )
                            )
                        }
                    }
                }
            )
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun updatePassword() {
        _state.update { it.copy(
            isLoading = true,
            currentPasswordErrorMessage = null,
            newPasswordErrorMessage = null,
            newPasswordConfirmErrorMessage = null,
        ) }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updatePassword(
                request = UpdatePasswordRequest(
                    currentPassword = _state.value.currentPassword.text.toString(),
                    newPassword = _state.value.newPassword.text.toString(),
                    confirmNewPassword = _state.value.newPasswordConfirm.text.toString()
                )
            ).fold(
                onSuccess = { _eventChannel.send(UserEditEvent.UpdateSuccess) },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> when (exception.errorCode) {
                            107 -> _state.update { it.copy(currentPasswordErrorMessage = UiText.DynamicString(exception.errorValue)) }
                            110 -> _state.update { it.copy(newPasswordErrorMessage = UiText.DynamicString(exception.errorValue)) }
                            108 -> _state.update { it.copy(newPasswordConfirmErrorMessage = UiText.DynamicString(exception.errorValue)) }
                            else -> {
                                globalSnackbarManager.showSnackbar(
                                    SnackBarData(
                                        text = UiText.StringResource(R.string.snackbar_http_500_error)
                                    )
                                )
                            }
                        }
                        else -> {
                            globalSnackbarManager.showSnackbar(
                                SnackBarData(
                                    text = UiText.StringResource(R.string.snackbar_http_500_error)
                                )
                            )
                        }
                    }
                }
            )
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun withdrawalDialog() {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                UserEditEvent.ShowDialog(
                    dialogData = DialogData(
                        type = DialogType.CONFIRM,
                        title = UiText.StringResource(R.string.dialog_withdraw_title),
                        subTitle = UiText.StringResource(R.string.dialog_withdraw_subtitle),
                        dismiss = UiText.StringResource(R.string.dialog_withdraw_dismiss),
                        confirm = UiText.StringResource(R.string.dialog_withdraw_confirm),
                        onConfirm = { withdrawal() }
                    )
                )
            )
        }
    }

    private fun withdrawal() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.userWithdrawal(
            ).fold(
                onSuccess = { _eventChannel.send(UserEditEvent.WithdrawalSuccess) },
                onFailure = {
                    globalSnackbarManager.showSnackbar(
                        SnackBarData(
                            text = UiText.StringResource(R.string.snackbar_http_500_error)
                        )
                    )
                }
            )
            _state.update { it.copy(isLoading = false) }
        }
    }
}