package com.jparkbro.anipick

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.jparkbro.anipick.deeplink.DeepLinkViewModel
import com.jparkbro.anipick.deeplink.parseDeepLink
import com.jparkbro.anipick.firebase.TrackScreenNavigation
import com.jparkbro.anipick.navigation.APNavDisplay
import com.jparkbro.anipick.navigation.AniPickBottomNavigation
import com.jparkbro.anipick.navigation.BOTTOM_NAV_ITEMS
import com.jparkbro.anipick.navigation.TOP_LEVEL_ITEMS
import com.jparkbro.anipick.theme.ThemeViewModel
import com.jparkbro.anipick.update.AppUpdateUiState
import com.jparkbro.anipick.update.UpdateAction
import com.jparkbro.anipick.update.UpdateEvent
import com.jparkbro.anipick.update.UpdateViewModel
import com.jparkbro.auth.api.navigateToLogin
import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickSnackbar
import com.jparkbro.core.designsystem.theme.AniPick_v2Theme
import com.jparkbro.core.model.setting.ThemeMode
import com.jparkbro.core.navigation.Navigator
import com.jparkbro.core.navigation.rememberNavigationState
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.core.ui.effect.ObserveAsEvents
import com.jparkbro.splash.api.SplashNavKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private var backPressedTime: Long = 0
    private val deepLinkViewModel: DeepLinkViewModel by viewModel()
    private val updateViewModel: UpdateViewModel by viewModel()
    private val themeViewModel: ThemeViewModel by viewModel()
    private val appUpdateManager: AppUpdateManager by inject()

    private val updateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        deepLinkViewModel.handleIntent(intent) // 콜드 스타트
        updateViewModel.checkForUpdate()

        /** 종료 확인 콜백 */
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish()
                } else {
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(this@MainActivity, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()
            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            // enableEdgeToEdge 기본값은 기기 다크 모드를 따라가서, 앱에서 고른 모드에 맞춰 상태바/내비게이션 바 아이콘 색을 다시 정한다.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                )
                onDispose {}
            }

            AniPick_v2Theme(darkTheme = darkTheme) {
                val navigationState = rememberNavigationState(
                    startKey = SplashNavKey.Splash,
                    topLevelKeys = TOP_LEVEL_ITEMS.keys,
                )
                val navigator = remember(navigationState) { Navigator(navigationState) }

                TrackScreenNavigation(navigationState = navigationState)

                // 웜 스타트(onNewIntent) 딥링크 처리 - 콜드 스타트는 Splash가 Home 진입 직후 처리한다.
                // 로그인 전(Splash/Auth)에 들어온 딥링크는 남겨 뒀다가 로그인 후 바텀 탭 화면에 진입하면 처리한다.
                val deepLinkState by deepLinkViewModel.state.collectAsStateWithLifecycle()
                LaunchedEffect(deepLinkState.pendingUri, navigationState.currentTopLevelKey) {
                    val uri = deepLinkState.pendingUri ?: return@LaunchedEffect
                    if (navigationState.currentTopLevelKey !in BOTTOM_NAV_ITEMS.keys) return@LaunchedEffect
                    parseDeepLink(uri)?.let(navigator::navigate)
                    deepLinkViewModel.consumeDeepLink()
                }

                val tokenProvider = koinInject<TokenProvider>()
                LaunchedEffect(tokenProvider) {
                    tokenProvider.isLoggedIn
                        .distinctUntilChanged()
                        .drop(1) // 콜드 스타트 시점의 최초 상태는 무시 (Splash가 자체적으로 처리)
                        .filter { isLoggedIn -> !isLoggedIn }
                        .collect {
                            // refreshToken까지 만료되어 세션이 끊긴 경우: 로그인 화면으로 강제 이동
                            navigator.navigateToLogin()
                        }
                }

                val globalSnackbarManager = koinInject<GlobalSnackbarManager>()
                val snackbarMessages by globalSnackbarManager.messages.collectAsStateWithLifecycle()

                val updateState by updateViewModel.state.collectAsStateWithLifecycle()
                ObserveAsEvents(updateViewModel.events) { event ->
                    when (event) {
                        is UpdateEvent.StartUpdateFlow -> appUpdateManager.startUpdateFlowForResult(
                            event.info,
                            updateResultLauncher,
                            AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                        )
                        UpdateEvent.CompleteUpdate -> appUpdateManager.completeUpdate()
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    APNavDisplay(
                        navigationState = navigationState,
                        navigator = navigator,
                        bottomNavigation = {
                            AniPickBottomNavigation(
                                currentKey = navigationState.currentTopLevelKey,
                                onNavigate = { navigator.navigate(it) },
                            )
                        },
                        deepLinkViewModel = deepLinkViewModel,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    snackbarMessages.firstOrNull()?.let { message ->
                        AniPickSnackbar(
                            message = message,
                            onDismiss = { globalSnackbarManager.dismissCurrent() },
                        )
                    }

                    when (updateState.appUpdate) {
                        is AppUpdateUiState.UpdateAvailable -> {
                            AniPickDialog(
                                title = "업데이트 안내",
                                message = "새로운 버전이 있어요. 지금 업데이트할까요?",
                                onDismissRequest = { updateViewModel.onAction(UpdateAction.OnUpdateDismissed) },
                                confirmText = "업데이트",
                                onConfirm = { updateViewModel.onAction(UpdateAction.OnUpdateConfirmed) },
                                dismissText = "나중에",
                                onDismiss = { updateViewModel.onAction(UpdateAction.OnUpdateDismissed) },
                            )
                        }
                        AppUpdateUiState.ReadyToInstall -> {
                            AniPickDialog(
                                title = "설치 준비 완료",
                                message = "업데이트 다운로드가 끝났어요. 지금 재시작해서 설치할까요?",
                                onDismissRequest = { updateViewModel.onAction(UpdateAction.OnInstallDismissed) },
                                confirmText = "재시작",
                                onConfirm = { updateViewModel.onAction(UpdateAction.OnInstallConfirmed) },
                                dismissText = "나중에",
                                onDismiss = { updateViewModel.onAction(UpdateAction.OnInstallDismissed) },
                            )
                        }
                        AppUpdateUiState.Idle -> Unit
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // getIntent()가 이후에도 최신 intent를 보게 갱신
        deepLinkViewModel.handleIntent(intent) // 웜 스타트 (launchMode singleTask 전제)
    }

    override fun onResume() {
        super.onResume()
        updateViewModel.checkDownloadedUpdate()
    }
}
