package com.jparkbro.register

interface EmailRegisterAction {
    data object OnBackClick : EmailRegisterAction
    data object OnTogglePasswordVisibility : EmailRegisterAction
    data object OnAllAgreeClick : EmailRegisterAction
    data object OnAgeVerificationClick : EmailRegisterAction
    data object OnTermsOfServiceClick : EmailRegisterAction
    data object OnTermsOfServiceLinkClick : EmailRegisterAction
    data object OnPrivacyPolicyClick : EmailRegisterAction
    data object OnPrivacyPolicyLinkClick : EmailRegisterAction
    data object OnRegisterClick : EmailRegisterAction
}