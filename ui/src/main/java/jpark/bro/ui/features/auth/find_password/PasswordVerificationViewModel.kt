package jpark.bro.ui.features.auth.find_password

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PasswordVerificationViewModel @Inject constructor(

) : ViewModel() {

    private val _emailText = MutableStateFlow<String>("")
    val emailText: StateFlow<String> = _emailText

    private val _verificationCodeText = MutableStateFlow<String>("")
    val verificationCodeText: StateFlow<String> = _verificationCodeText

    fun updateEmail(email: String) {
        _emailText.value = email
    }

    fun updateVerificationCode(verificationCode: String) {
        _verificationCodeText.value = verificationCode
    }
}