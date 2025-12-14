package com.jparkbro.reset

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.AuthRepository
import com.jparkbro.model.auth.ResetPassword
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.R
import com.jparkbro.ui.model.SnackBarData
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
class PasswordResetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _email: String = savedStateHandle.get<String>("email") ?: ""

    private val _state = MutableStateFlow(PasswordResetState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<PasswordResetEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        validatePassword()
    }

    fun onAction(action: PasswordResetAction) {
        when (action) {
            PasswordResetAction.OnTogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            PasswordResetAction.OnTogglePasswordConfirmVisibility -> {
                _state.update {
                    it.copy(
                        isConfirmPasswordVisible = !it.isConfirmPasswordVisible
                    )
                }
            }

            PasswordResetAction.OnCompleteClicked -> {
                changePassword()
            }

            else -> Unit
        }
    }

    private fun changePassword() {
        _state.update {
            it.copy(
                isChangeIng = true,
                isChangeEnabled = false
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.resetPassword(
                ResetPassword(
                    email = _email,
                    newPassword = _state.value.password.text.toString(),
                    checkNewPassword = _state.value.confirmPassword.text.toString(),
                )
            ).fold(
                onSuccess = {
                    _eventChannel.send(PasswordResetEvent.PasswordChangeSuccess)
                },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> {
                            _state.update {
                                it.copy(
                                    confirmPasswordErrorMessage = UiText.DynamicString(exception.errorValue)
                                )
                            }
                        }
                        else -> {
                            showSnackBar(
                                SnackBarData(
                                    text = UiText.StringResource(R.string.snackbar_http_500_error),
                                )
                            )
                        }
                    }
                }
            )

            _state.update {
                it.copy(
                    isChangeIng = false,
                    isChangeEnabled = it.isPasswordValid.isValidPassword && it.confirmPassword.text.toString().isNotBlank() && !it.isChangeIng
                )
            }
        }
    }

    private fun validatePassword() {
        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow { _state.value.password.text.toString() }
                .collectLatest { password ->
                    val isValid = userDataValidator.isValidPassword(password)

                    _state.update {
                        it.copy(
                            isPasswordValid = isValid,
                            isChangeEnabled = isValid.isValidPassword && it.confirmPassword.text.toString().isNotBlank() && !it.isChangeIng
                        )
                    }
                }
        }

        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow { _state.value.confirmPassword.text.toString() }
                .collectLatest { confirmPassword ->
                    _state.update {
                        it.copy(
                            isChangeEnabled = it.isPasswordValid.isValidPassword && confirmPassword.isNotBlank() && !it.isChangeIng
                        )
                    }
                }

        }
    }

    private fun showSnackBar(snackBarData: SnackBarData) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                PasswordResetEvent.PasswordChangeFailWithSnackBar(
                    snackBarData = snackBarData,
                )
            )
        }
    }
}