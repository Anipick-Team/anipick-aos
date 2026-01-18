package com.jparkbro.setting.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.UserEditType
import com.jparkbro.ui.util.UiText
import com.jparkbro.util.EmailValidationState
import com.jparkbro.util.PasswordValidationState

data class UserEditState(
    val uiState: UiState = UiState.Loading,
    val editType: UserEditType,
    val newNickname: TextFieldState = TextFieldState(),
    val newEmail: TextFieldState = TextFieldState(),
    val isNewEmailValid: EmailValidationState = EmailValidationState(),
    val currentPassword: TextFieldState = TextFieldState(),
    val newPassword: TextFieldState = TextFieldState(),
    val isNewPasswordValid: PasswordValidationState = PasswordValidationState(),
    val newPasswordConfirm: TextFieldState = TextFieldState(),
    val nicknameErrorMessage: UiText? = null,
    val emailErrorMessage: UiText? = null,
    val currentPasswordErrorMessage: UiText? = null,
    val newPasswordErrorMessage: UiText? = null,
    val newPasswordConfirmErrorMessage: UiText? = null,

    /* API 통신 로딩 */
    val isLoading: Boolean = false,

    /* API 통신 데이터 */
    val nickname: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
) {
    val isChangeNicknameEnabled: Boolean
        get() = !isLoading &&
                newNickname.text.isNotBlank()

    val isChangeEmailEnabled: Boolean
        get() = !isLoading &&
                currentPassword.text.isNotBlank() &&
                newEmail.text.isNotBlank() &&
                isNewEmailValid.isValidEmail

    val isChangePasswordEnabled: Boolean
        get() = !isLoading &&
                currentPassword.text.isNotBlank() &&
                newPassword.text.toString() == newPasswordConfirm.text.toString() &&
                isNewPasswordValid.isValidPassword
}
