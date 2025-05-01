package jpark.bro.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EmailSignupViewModel @Inject constructor(

) : ViewModel() {
//    var isAgeVerified by remember { mutableStateOf(false) }
//    var isTermsOfServiceAccepted by remember { mutableStateOf(false) }
//    var isPrivacyPolicyAccepted by remember { mutableStateOf(false) }
//    var isAllAgreed by remember { mutableStateOf(false) }

    private val _emailText = MutableStateFlow<String>("")
    val emailText: StateFlow<String> = _emailText

    private val _passwordText = MutableStateFlow<String>("")
    val passwordText: StateFlow<String> = _passwordText

    private val _isVisibility = MutableStateFlow<Boolean>(false)
    val isVisibility: StateFlow<Boolean> = _isVisibility

    private val _isPasswordValid = MutableStateFlow<Boolean>(false)
    val isPasswordValid: StateFlow<Boolean> = _isPasswordValid

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