package com.jparkbro.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.domain.EmailSignupUseCase
import com.jparkbro.model.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import com.jparkbro.ui.util.EmailValidator
import com.jparkbro.ui.util.PasswordValidator
import com.jparkbro.ui.util.extension.filterKorean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EmailSignupViewModel @Inject constructor(
    private val emailSignupUseCase: EmailSignupUseCase
) : ViewModel() {
    private val _emailText = MutableStateFlow<String>("")
    val emailText: StateFlow<String> = _emailText.asStateFlow()

    private val _passwordText = MutableStateFlow<String>("")
    val passwordText: StateFlow<String> = _passwordText.asStateFlow()

    private val _isVisibility = MutableStateFlow<Boolean>(false)
    val isVisibility: StateFlow<Boolean> = _isVisibility.asStateFlow()

    private val _isPasswordValid = MutableStateFlow<Boolean>(false)
    val isPasswordValid: StateFlow<Boolean> = _isPasswordValid.asStateFlow()

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

    private val _signupUiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val signupUiState: StateFlow<SignupUiState> = _signupUiState.asStateFlow()

    // 버튼 클릭시, 이메일 중복체크 및 가입 진행
    fun signup() {
        val emailErrorMessage = EmailValidator.getErrorMessage(_emailText.value)

        if (emailErrorMessage == null) {
            _signupUiState.value = SignupUiState.Loading

            viewModelScope.launch {
                emailSignupUseCase(
                    email = _emailText.value,
                    password = _passwordText.value,
                    termsAndConditions = _isAllAgreed.value,
                ).collect { result ->
                    _signupUiState.value = when (result) {
                        is Result.Success -> SignupUiState.Success
                        is Result.Error -> SignupUiState.Error(result.exception.message ?: "서버와 연결이 실패했습니다.")
                        is Result.Loading -> SignupUiState.Loading
                    }
                }
            }
        } else {
            _signupUiState.value = SignupUiState.Error(emailErrorMessage)
        }
    }
}

enum class AgreementType {
    ALL,
    PRIVACY_POLICY,
    TERMS_OF_SERVICE,
    AGE_VERIFICATION,
}

sealed interface SignupUiState {
    data object Idle : SignupUiState
    data object Success : SignupUiState // 200, 통신 후 에러, email valid
    data object Loading : SignupUiState
    data class Error(val message: String) : SignupUiState // TODO 서버 에러 (alert ?)
}