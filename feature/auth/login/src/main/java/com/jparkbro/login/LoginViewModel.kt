package com.jparkbro.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.domain.GoogleLoginUseCase
import com.jparkbro.domain.KakaoLoginUseCase
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.R
import com.jparkbro.ui.util.UiText
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<LoginEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnKakaoLoginClick -> kakaoLogin(action.activity)
            is LoginAction.OnGoogleLoginClick -> googleLogin(action.activity)
            is LoginAction.OnDialogDismiss -> dismissDialog()
            else -> Unit
        }
    }

    private fun kakaoLogin(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            kakaoLoginUseCase(activity).collect { result ->
                result.fold(
                    onSuccess = { reviewCompletedYn ->
                        _eventChannel.send(LoginEvent.LoginSuccess(reviewCompletedYn))
                    },
                    onFailure = { exception ->
                        if (exception is ApiException) {
                            when (exception.errorCode) {
                                132 -> { showWithdrawnAccountDialog() }
                                133 -> { showDuplicateEmailDialog() }
                            }
                        } else {
                            showSnackBar(
                                SnackBarData(text = UiText.StringResource(R.string.snackbar_login_failed))
                            )
                        }
                    }
                )
            }
        }
    }

    private fun googleLogin(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            googleLoginUseCase(activity).collect { result ->
                result.fold(
                    onSuccess = { reviewCompletedYn ->
                        _eventChannel.send(LoginEvent.LoginSuccess(reviewCompletedYn))
                    },
                    onFailure = { exception ->
                        if (exception is ApiException) {
                            when (exception.errorCode) {
                                132 -> { showWithdrawnAccountDialog() }
                                133 -> { showDuplicateEmailDialog() }
                            }
                        } else {
                            showSnackBar(
                                SnackBarData(
                                    text = UiText.StringResource(R.string.snackbar_login_failed),
                                    onDismiss = { dismissSnackBar() }
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    private fun showWithdrawnAccountDialog() {
        _uiState.update {
            it.copy(
                dialogData = DialogData(
                    type = DialogType.ALERT,
                    title = UiText.StringResource(R.string.dialog_account_withdrawn),
                    subTitle = UiText.StringResource(R.string.dialog_account_withdrawn_message),
                    confirm = UiText.StringResource(R.string.dialog_dismiss),
                    onConfirm = { dismissDialog() }
                )
            )
        }
    }

    private fun showDuplicateEmailDialog() {
        _uiState.update {
            it.copy(
                dialogData = DialogData(
                    type = DialogType.CONFIRM,
                    title = UiText.StringResource(R.string.dialog_email_exists),
                    subTitle = UiText.StringResource(R.string.dialog_email_exists_message),
                    dismiss = UiText.StringResource(R.string.dialog_dismiss),
                    confirm = UiText.StringResource(R.string.dialog_email_login),
                    onDismiss = { dismissDialog() },
                )
            )
        }
    }

    private fun showSnackBar(snackBarData: SnackBarData) {
        _uiState.update {
            it.copy(
                snackBarQueue = it.snackBarQueue + snackBarData
            )
        }
    }

    private fun dismissSnackBar() {
        _uiState.update {
            it.copy(
                snackBarQueue = it.snackBarQueue.drop(1)
            )
        }
    }

    private fun dismissDialog() {
        _uiState.update {
            it.copy(dialogData = null)
        }
    }
}