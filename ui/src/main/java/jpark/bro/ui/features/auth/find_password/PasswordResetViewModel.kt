package jpark.bro.ui.features.auth.find_password

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(

) : ViewModel() {

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _passwordConfirm = MutableStateFlow("")
    val passwordConfirm: StateFlow<String> = _passwordConfirm

    private val _isPasswordVisibility = MutableStateFlow<Boolean>(false)
    val isPasswordVisibility: StateFlow<Boolean> = _isPasswordVisibility

    private val _isConfirmVisibility = MutableStateFlow<Boolean>(false)
    val isConfirmVisibility: StateFlow<Boolean> = _isConfirmVisibility

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        _passwordConfirm.value = passwordConfirm
    }

    fun togglePasswordVisibility() {
        _isPasswordVisibility.value = !_isPasswordVisibility.value
    }

    fun toggleConfirmVisibility() {
        _isConfirmVisibility.value = !_isConfirmVisibility.value
    }

}