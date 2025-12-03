package com.jparkbro.anipick

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jparkbro.anipick.navigation.APBottomNavigation
import com.jparkbro.anipick.navigation.APNavHost
import com.jparkbro.anipick.navigation.BottomDestination
import com.jparkbro.detail.navigation.navigateToAnimeDetail
import com.jparkbro.home.navigation.Home
import com.jparkbro.login.navigation.Login
import com.jparkbro.ui.APAlertDialog
import com.jparkbro.ui.APConfirmDialog
import com.jparkbro.ui.DialogType
import com.jparkbro.ui.theme.AniPickTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        // Deep link 처리
        intent?.data?.let { uri ->
            Log.d("MainActivity", "onCreate - Deep link received: $uri")
            viewModel.setPendingDeepLink(uri)
        }

        setContent {
            AniPickTheme {
                val metaData by viewModel.metaData.collectAsState()
                val uiState by viewModel.uiState.collectAsState()
                val dialogData by viewModel.dialogData.collectAsState()
                val pendingDeepLink by viewModel.pendingDeepLink.collectAsState()

                Log.d("MainActivity", "Current UI State: $uiState")
                Log.d("MainActivity", "Pending deep link: $pendingDeepLink")

                when (uiState) {
                    is MainActivityUiState.Loading -> {
                        Log.d("MainActivity", "UI State: Loading")
                    }
                    is MainActivityUiState.FinishApp -> {
                        Log.d("MainActivity", "UI State: FinishApp")
                        LaunchedEffect(Unit) {
                            val storeUrl = (uiState as MainActivityUiState.FinishApp).storeUrl
                            storeUrl?.let {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                                    startActivity(intent)
                                } catch (e: Exception) {
                                    Log.e("MainActivity", "Failed to open store: ${e.message}")
                                }
                            }
                            finish()
                        }
                    }
                    is MainActivityUiState.Success -> {
                        val isAutoLogin = (uiState as MainActivityUiState.Success).isAutoLogin
                        val startDestination = if (isAutoLogin) Home else Login

                        Log.d("MainActivity", "UI State: Success, isAutoLogin=$isAutoLogin")

                        val bottomDestinations: List<BottomDestination> = BottomDestination.entries
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        // 자동 로그인 완료 후 Deep link 처리
                        Log.d("MainActivity", "Setting up LaunchedEffect for deep link, pendingDeepLink=$pendingDeepLink, isAutoLogin=$isAutoLogin")
                        LaunchedEffect(pendingDeepLink, isAutoLogin) {
                            Log.d("MainActivity", "LaunchedEffect triggered! pendingDeepLink=$pendingDeepLink, isAutoLogin=$isAutoLogin")
                            pendingDeepLink?.let { uri ->
                                if (isAutoLogin) {
                                    Log.d("MainActivity", "Auto login completed, waiting for token to be ready...")
                                    // 토큰이 완전히 로드되고 API 호출에 사용 가능해질 때까지 잠시 대기
                                    kotlinx.coroutines.delay(300)
                                    Log.d("MainActivity", "Processing deep link: $uri")
                                    handleDeepLink(navController, uri)
                                    viewModel.clearPendingDeepLink()
                                } else {
                                    Log.d("MainActivity", "Deep link pending - waiting for login: $uri")
                                    // 자동 로그인 실패 시, 로그인 화면으로 이동
                                    // 로그인 성공 후 다시 처리하기 위해 pending 상태 유지
                                }
                            }
                        }

                        APNavHost(
                            navController = navController,
                            startDestination = startDestination,
                            metaData = metaData,
                            bottomNav = {
                                APBottomNavigation(
                                    items = bottomDestinations,
                                    navController = navController,
                                    currentDestination = currentDestination
                                )
                            }
                        )
                    }
                    is MainActivityUiState.Error -> {
                        val errorMessage = (uiState as MainActivityUiState.Error).message

                        APConfirmDialog(
                            title = "앱 초기화 실패",
                            subTitle = errorMessage,
                            dismiss = "종료",
                            confirm = "재시도",
                            onDismiss = {
                                finish()
                            },
                            onConfirm = {
                                viewModel.retryAppInit()
                            }
                        )
                    }
                }

                dialogData?.let {
                    when (it.type) {
                        DialogType.ALERT -> {
                            APAlertDialog(
                                title = it.title,
                                errorMsg = it.errorMsg,
                                dismiss = it.confirm,
                                onDismiss = it.onConfirm
                            )
                        }
                        DialogType.CONFIRM -> {
                            APConfirmDialog(
                                title = it.title,
                                subTitle = it.subTitle,
                                content = it.content,
                                dismiss = it.dismiss,
                                confirm = it.confirm,
                                onDismiss = it.onDismiss,
                                onConfirm = it.onConfirm
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // 앱이 이미 실행 중일 때 새로운 Deep link 처리
        intent.data?.let { uri ->
            Log.d("MainActivity", "onNewIntent - Deep link received: $uri")
            viewModel.setPendingDeepLink(uri)
        }
    }

    /**
     * Deep link URI를 파싱하여 적절한 화면으로 이동
     * 지원하는 형식:
     * - https://anipick.p-e.kr/app/anime/detail/{animeId}
     */
    private fun handleDeepLink(navController: NavController, uri: Uri) {
        try {
            Log.d("MainActivity", "Handling deep link: $uri")

            val pathSegments = uri.pathSegments

            when {
                // https://anipick.p-e.kr/app/anime/detail/123 형식
                uri.scheme in listOf("http", "https") &&
                pathSegments.getOrNull(0) == "app" &&
                pathSegments.getOrNull(1) == "anime" &&
                pathSegments.getOrNull(2) == "detail" -> {
                    val animeId = pathSegments.getOrNull(3)?.toIntOrNull()
                    animeId?.let {
                        Log.d("MainActivity", "Navigating to anime detail: $it")
                        navController.navigateToAnimeDetail(it)
                    } ?: Log.e("MainActivity", "Invalid anime ID in deep link: $uri")
                }
                else -> {
                    Log.e("MainActivity", "Unsupported deep link format: $uri")
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error handling deep link: $uri", e)
        }
    }
}