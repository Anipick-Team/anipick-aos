package jpark.bro.anipick.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EmailLoginViewModel @Inject constructor(

) : ViewModel() {

    private val _emailText = MutableStateFlow<String>("")
    val emailText: StateFlow<String> = _emailText

    private val _passwordText = MutableStateFlow<String>("")
    val passwordText: StateFlow<String> = _passwordText

    private val _isVisibility = MutableStateFlow<Boolean>(false)
    val isVisibility: StateFlow<Boolean> = _isVisibility

    fun updateEmail(email: String) {
        _emailText.value = email
    }

    fun updatePassword(password: String) {
        _passwordText.value = password
    }

    fun togglePasswordVisibility() {
        _isVisibility.value = !_isVisibility.value
    }

}