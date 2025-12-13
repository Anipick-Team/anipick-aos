package com.jparkbro.login

import android.app.Activity
import app.cash.turbine.test
import com.jparkbro.domain.GoogleLoginUseCase
import com.jparkbro.domain.KakaoLoginUseCase
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.components.DialogType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

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

    @Test
    fun `초기 상태는 Idle이다`() = runTest {
        // Given & When
        val initialState = viewModel.uiState.value

        // Then
        assertEquals(LoginUiState.Idle, initialState)
    }

    @Test
    fun `Google 로그인 성공 시 Success 상태로 변경되고 reviewCompletedYn이 true이다`() = runTest {
        // Given
        val reviewCompleted = true
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.success(reviewCompleted))

        // When
        viewModel.uiState.test {
            assertEquals(LoginUiState.Idle, awaitItem())

            viewModel.signInWithGoogle(mockActivity)

            assertEquals(LoginUiState.Loading, awaitItem())
            val successState = awaitItem() as LoginUiState.Success
            assertEquals(reviewCompleted, successState.reviewCompletedYn)
        }
    }

    @Test
    fun `Google 로그인 성공 시 Success 상태로 변경되고 reviewCompletedYn이 false이다`() = runTest {
        // Given
        val reviewCompleted = false
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.success(reviewCompleted))

        // When
        viewModel.uiState.test {
            assertEquals(LoginUiState.Idle, awaitItem())

            viewModel.signInWithGoogle(mockActivity)

            assertEquals(LoginUiState.Loading, awaitItem())
            val successState = awaitItem() as LoginUiState.Success
            assertEquals(reviewCompleted, successState.reviewCompletedYn)
        }
    }

    @Test
    fun `Kakao 로그인 성공 시 Success 상태로 변경되고 reviewCompletedYn이 true이다`() = runTest {
        // Given
        val reviewCompleted = true
        coEvery { kakaoLoginUseCase(any()) } returns flowOf(Result.success(reviewCompleted))

        // When
        viewModel.uiState.test {
            assertEquals(LoginUiState.Idle, awaitItem())

            viewModel.signInWithKakao(mockActivity)

            assertEquals(LoginUiState.Loading, awaitItem())
            val successState = awaitItem() as LoginUiState.Success
            assertEquals(reviewCompleted, successState.reviewCompletedYn)
        }
    }

    @Test
    fun `Kakao 로그인 성공 시 Success 상태로 변경되고 reviewCompletedYn이 false이다`() = runTest {
        // Given
        val reviewCompleted = false
        coEvery { kakaoLoginUseCase(any()) } returns flowOf(Result.success(reviewCompleted))

        // When
        viewModel.uiState.test {
            assertEquals(LoginUiState.Idle, awaitItem())

            viewModel.signInWithKakao(mockActivity)

            assertEquals(LoginUiState.Loading, awaitItem())
            val successState = awaitItem() as LoginUiState.Success
            assertEquals(reviewCompleted, successState.reviewCompletedYn)
        }
    }

    @Test
    fun `로그인 시작 시 Loading 상태로 변경된다`() = runTest {
        // Given
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.success(true))

        // When
        viewModel.uiState.test {
            assertEquals(LoginUiState.Idle, awaitItem())

            viewModel.signInWithGoogle(mockActivity)

            val loadingState = awaitItem()
            assertEquals(LoginUiState.Loading, loadingState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ApiException errorCode 132(탈퇴된 계정) 발생 시 Error 상태로 변경되고 탈퇴된 계정 다이얼로그를 표시한다`() = runTest {
        // Given
        val exception = ApiException(errorCode = 132, errorValue = "탈퇴된 계정")
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.failure(exception))

        // When
        viewModel.signInWithGoogle(mockActivity)

        // Then
        assertEquals(LoginUiState.Error, viewModel.uiState.value)

        val dialog = viewModel.showDialog.value
        assertNotNull(dialog)
        assertEquals(DialogType.ALERT, dialog?.type)
        assertEquals("탈퇴된 계정입니다.", dialog?.title)
        assertEquals("닫기", dialog?.dismiss)
        assertNotNull(dialog?.errorMsg)
    }

    @Test
    fun `ApiException errorCode 133(중복 이메일) 발생 시 Error 상태로 변경되고 중복 이메일 다이얼로그를 표시한다`() = runTest {
        // Given
        val exception = ApiException(errorCode = 133, errorValue = "중복 이메일")
        coEvery { kakaoLoginUseCase(any()) } returns flowOf(Result.failure(exception))

        // When
        viewModel.signInWithKakao(mockActivity)

        // Then
        assertEquals(LoginUiState.Error, viewModel.uiState.value)

        val dialog = viewModel.showDialog.value
        assertNotNull(dialog)
        assertEquals(DialogType.CONFIRM, dialog?.type)
        assertEquals("이미 가입된 이메일 주소입니다.", dialog?.title)
        assertEquals("이메일 로그인을 시도해주세요.", dialog?.subTitle)
        assertEquals("닫기", dialog?.dismiss)
        assertEquals("이메일 로그인", dialog?.confirm)
    }

    @Test
    fun `일반 Exception 발생 시 Error 상태로 변경되고 SnackBar를 표시한다`() = runTest {
        // Given
        val exception = RuntimeException("일반 에러")
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.failure(exception))

        // When
        viewModel.signInWithGoogle(mockActivity)

        // Then
        assertEquals(LoginUiState.Error, viewModel.uiState.value)

        val snackBarQueue = viewModel.snackBarQueue.value
        assertEquals(1, snackBarQueue.size)
        assertNotNull(snackBarQueue.firstOrNull())
    }

    @Test
    fun `다이얼로그 onDismiss 호출 시 showDialog가 null로 변경된다`() = runTest {
        // Given
        val exception = ApiException(errorCode = 132, errorValue = "탈퇴된 계정")
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.failure(exception))

        // When
        viewModel.signInWithGoogle(mockActivity)

        // Then
        val dialog = viewModel.showDialog.value
        assertNotNull(dialog)

        // When - onDismiss 호출
        dialog?.onDismiss?.invoke()

        // Then
        assertNull(viewModel.showDialog.value)
    }

    @Test
    fun `SnackBar 여러 개 추가 시 큐에 순서대로 쌓인다`() = runTest {
        // Given
        val exception1 = RuntimeException("에러 1")
        val exception2 = RuntimeException("에러 2")

        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.failure(exception1))
        coEvery { kakaoLoginUseCase(any()) } returns flowOf(Result.failure(exception2))

        // When
        viewModel.signInWithGoogle(mockActivity)
        viewModel.signInWithKakao(mockActivity)

        // Then
        val snackBarQueue = viewModel.snackBarQueue.value
        assertEquals(2, snackBarQueue.size)
    }

    @Test
    fun `SnackBar dismiss 호출 시 큐에서 첫 번째 항목이 제거된다`() = runTest {
        // Given
        val exception1 = RuntimeException("에러 1")
        val exception2 = RuntimeException("에러 2")

        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.failure(exception1))
        coEvery { kakaoLoginUseCase(any()) } returns flowOf(Result.failure(exception2))

        viewModel.signInWithGoogle(mockActivity)
        viewModel.signInWithKakao(mockActivity)

        assertEquals(2, viewModel.snackBarQueue.value.size)

        // When - 첫 번째 SnackBar dismiss
        viewModel.snackBarQueue.value.firstOrNull()?.onDismiss?.invoke()

        // Then
        assertEquals(1, viewModel.snackBarQueue.value.size)
    }

    @Test
    fun `로그인 성공 후 다시 로그인 시도 시 Loading 상태로 변경된다`() = runTest {
        // Given - 첫 번째 로그인 성공
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.success(true))
        viewModel.signInWithGoogle(mockActivity)
        assertEquals(LoginUiState.Success(true), viewModel.uiState.value)

        // When - 두 번째 로그인 시도
        coEvery { kakaoLoginUseCase(any()) } returns flowOf(Result.success(false))

        viewModel.uiState.test {
            assertEquals(LoginUiState.Success(true), awaitItem())

            viewModel.signInWithKakao(mockActivity)

            assertEquals(LoginUiState.Loading, awaitItem())
            assertEquals(LoginUiState.Success(false), awaitItem())
        }
    }

    @Test
    fun `로그인 실패 후 재시도 시 정상 동작한다`() = runTest {
        // Given - 첫 번째 로그인 실패
        val exception = RuntimeException("네트워크 에러")
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.failure(exception))
        viewModel.signInWithGoogle(mockActivity)
        assertEquals(LoginUiState.Error, viewModel.uiState.value)

        // When - 재시도 성공
        coEvery { googleLoginUseCase(any()) } returns flowOf(Result.success(true))

        viewModel.uiState.test {
            assertEquals(LoginUiState.Error, awaitItem())

            viewModel.signInWithGoogle(mockActivity)

            assertEquals(LoginUiState.Loading, awaitItem())
            assertEquals(LoginUiState.Success(true), awaitItem())
        }
    }
}
