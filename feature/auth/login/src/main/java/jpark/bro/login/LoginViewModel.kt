package jpark.bro.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpark.bro.domain.GoogleLoginUseCase
import jpark.bro.domain.KakaoLoginUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val kakaoLoginUseCase: KakaoLoginUseCase,
) : ViewModel() {

    fun signInWithGoogle(activity: Activity) {
        socialLoginProceed {
            googleLoginUseCase(activity)
        }
    }

    fun signInWithKakao(activity: Activity) {
        socialLoginProceed {
            kakaoLoginUseCase(activity)
        }
    }

    private fun socialLoginProceed(socialLogin: suspend () -> Result<Boolean>) {
        viewModelScope.launch {
            // TODO UIState Lording

            try {
                val result = socialLogin()
            } catch (e: Exception) {
                // TODO Error
            }
        }
    }
}