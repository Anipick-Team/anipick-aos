package jpark.bro.findpassword

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jpark.bro.ui.util.extension.filterKorean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(

) : ViewModel() {

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _passwordConfirm = MutableStateFlow<String>("")
    val passwordConfirm: StateFlow<String> = _passwordConfirm

    private val _isPasswordVisibility = MutableStateFlow<Boolean>(false)
    val isPasswordVisibility: StateFlow<Boolean> = _isPasswordVisibility

    private val _isConfirmVisibility = MutableStateFlow<Boolean>(false)
    val isConfirmVisibility: StateFlow<Boolean> = _isConfirmVisibility

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

}