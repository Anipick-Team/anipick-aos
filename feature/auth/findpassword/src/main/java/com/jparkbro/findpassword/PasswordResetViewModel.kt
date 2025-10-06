package com.jparkbro.findpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.AuthRepository
import com.jparkbro.model.auth.ResetPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import com.jparkbro.ui.util.extension.filterKorean
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = PasswordResetViewModel.Factory::class)
class PasswordResetViewModel @AssistedInject constructor(
    private val authRepository: AuthRepository,
    @Assisted val email: String,
) : ViewModel() {

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordConfirm = MutableStateFlow<String>("")
    val passwordConfirm: StateFlow<String> = _passwordConfirm.asStateFlow()

    private val _isPasswordVisibility = MutableStateFlow<Boolean>(false)
    val isPasswordVisibility: StateFlow<Boolean> = _isPasswordVisibility.asStateFlow()

    private val _isConfirmVisibility = MutableStateFlow<Boolean>(false)
    val isConfirmVisibility: StateFlow<Boolean> = _isConfirmVisibility.asStateFlow()

    fun updatePassword(password: String) {
        _password.value = password.filterKorean()
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        _passwordConfirm.value = passwordConfirm.filterKorean()
    }

    fun togglePasswordVisibility() {
        _isPasswordVisibility.value = !_isPasswordVisibility.value
    }

    fun toggleConfirmVisibility() {
        _isConfirmVisibility.value = !_isConfirmVisibility.value
    }

    fun resetPassword(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.resetPassword(
                ResetPassword(
                    email = email,
                    newPassword = _password.value,
                    checkNewPassword = _passwordConfirm.value,
                )
            ).fold(
                onSuccess = {
                    onResult(true)
                },
                onFailure = {
                    onResult(false)
                }
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            email: String,
        ): PasswordResetViewModel
    }
}