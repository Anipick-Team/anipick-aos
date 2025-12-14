package com.jparkbro.register

interface EmailRegisterAction {
    data object OnBackClicked : EmailRegisterAction
    data object OnTogglePasswordVisibility : EmailRegisterAction
    data object OnAllAgreeClicked : EmailRegisterAction
    data object OnAgeVerificationClicked : EmailRegisterAction
    data object OnTermsOfServiceClicked : EmailRegisterAction
    data object OnTermsOfServiceLinkClicked : EmailRegisterAction
    data object OnPrivacyPolicyClicked : EmailRegisterAction
    data object OnPrivacyPolicyLinkClicked : EmailRegisterAction
    data object OnRegisterClicked : EmailRegisterAction
}