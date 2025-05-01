package jpark.bro.data.model

sealed class SignInState {
    object Idle: SignInState()
    object Loading: SignInState()
    data class Success(val userData: UserData, val idToken: String) : SignInState()
    data class Error(val message: String) : SignInState()
}