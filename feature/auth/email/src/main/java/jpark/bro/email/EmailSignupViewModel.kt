package jpark.bro.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpark.bro.ui.util.EmailValidator
import jpark.bro.ui.util.PasswordValidator
import jpark.bro.ui.util.extension.filterKorean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailSignupViewModel @Inject constructor(

) : ViewModel() {
    private val _emailText = MutableStateFlow<String>("")
    val emailText: StateFlow<String> = _emailText

    private val _passwordText = MutableStateFlow<String>("")
    val passwordText: StateFlow<String> = _passwordText

    private val _isVisibility = MutableStateFlow<Boolean>(false)
    val isVisibility: StateFlow<Boolean> = _isVisibility

    private val _emailErrorText = MutableStateFlow<String?>(null)
    val emailErrorText: StateFlow<String?> = _emailErrorText

    private val _isPasswordValid = MutableStateFlow<Boolean>(false)
    val isPasswordValid: StateFlow<Boolean> = _isPasswordValid

    fun updateEmail(email: String) {
        _emailText.value = email
    }

    fun updatePassword(password: String) {
        val filteredValue = password.filterKorean()
        _passwordText.value = filteredValue
        _isPasswordValid.value = PasswordValidator.validate(filteredValue)
    }

    fun togglePasswordVisibility() {
        _isVisibility.value = !_isVisibility.value
    }

    // Verification
    private val _isAgeVerified = MutableStateFlow<Boolean>(false)
    val isAgeVerified: StateFlow<Boolean> = _isAgeVerified

    private val _isTermsOfServiceAccepted = MutableStateFlow<Boolean>(false)
    val isTermsOfServiceAccepted: StateFlow<Boolean> = _isTermsOfServiceAccepted

    private val _isPrivacyPolicyAccepted = MutableStateFlow<Boolean>(false)
    val isPrivacyPolicyAccepted: StateFlow<Boolean> = _isPrivacyPolicyAccepted

    private val _isAllAgreed = MutableStateFlow<Boolean>(false)
    val isAllAgreed: StateFlow<Boolean> = _isAllAgreed

    fun updateAgreementStatus(type: AgreementType, isAgreed: Boolean) {
        when(type) {
            AgreementType.ALL -> {
                _isAllAgreed.value = isAgreed
                _isAgeVerified.value = isAgreed
                _isTermsOfServiceAccepted.value = isAgreed
                _isPrivacyPolicyAccepted.value = isAgreed
            }
            AgreementType.PRIVACY_POLICY -> {
                _isPrivacyPolicyAccepted.value = isAgreed
            }
            AgreementType.TERMS_OF_SERVICE -> {
                _isTermsOfServiceAccepted.value = isAgreed
            }
            AgreementType.AGE_VERIFICATION -> {
                _isAgeVerified.value = isAgreed
            }
        }
        checkAllAgreements()
    }

    private fun checkAllAgreements() {
        _isAllAgreed.value = _isAgeVerified.value && _isTermsOfServiceAccepted.value && _isPrivacyPolicyAccepted.value
    }

    // 회원가입 버튼 활성화
    fun canProceedSignup(): Boolean {
        return _isAllAgreed.value && _isPasswordValid.value
    }

    // 버튼 클릭시, 이메일 중복체크 및 가입 진행
    fun signup(): Boolean {
        val isEmailValid = EmailValidator.validate(_emailText.value)
        _emailErrorText.value = EmailValidator.getErrorMessage(_emailText.value)

        if (isEmailValid) {
            viewModelScope.launch {
                // TODO 회원가입 API 추가

            }
            return true
        } else {
            return false
        }
    }
}

enum class AgreementType {
    ALL,
    PRIVACY_POLICY,
    TERMS_OF_SERVICE,
    AGE_VERIFICATION,
}