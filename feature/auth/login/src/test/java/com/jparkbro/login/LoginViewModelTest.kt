package com.jparkbro.login

import android.app.Activity
import com.jparkbro.domain.GoogleLoginUseCase
import com.jparkbro.domain.KakaoLoginUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var googleLoginUseCase: GoogleLoginUseCase
    private lateinit var kakaoLoginUseCase: KakaoLoginUseCase
    private lateinit var mockActivity: Activity

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        googleLoginUseCase = mockk()
        kakaoLoginUseCase = mockk()
        mockActivity = mockk(relaxed = true)

        viewModel = LoginViewModel(
            googleLoginUseCase = googleLoginUseCase,
            kakaoLoginUseCase = kakaoLoginUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
