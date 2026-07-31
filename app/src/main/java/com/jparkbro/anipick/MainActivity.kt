package com.jparkbro.anipick

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.jparkbro.anipick.firebase.TrackScreenNavigation
import com.jparkbro.anipick.navigation.APBottomNavigation
import com.jparkbro.anipick.navigation.APNavHost
import com.jparkbro.anipick.navigation.BottomDestination
import com.jparkbro.home.main.navigation.Home
import com.jparkbro.info.anime.navigation.navigateToInfoAnime
import com.jparkbro.login.navigation.Login
import com.jparkbro.model.enum.DialogType
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APReportReasonDialog
import com.jparkbro.ui.components.APSnackBar
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.DialogStyle
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.snackbar.GlobalSnackbarManager
import com.jparkbro.ui.theme.AniPickTheme
import com.jparkbro.ui.util.UiText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var globalSnackbarManager: GlobalSnackbarManager

    private val viewModel: MainActivityViewModel by viewModels()
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private val updatePreference by lazy { UpdatePreference(this) }

    private var backPressedTime: Long = 0

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            Log.d("MainActivity", "Update flow failed! Result code: ${result.resultCode}")
        }
    }

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()

                if (totalBytesToDownload > 0) {
                    val progress = (bytesDownloaded * 100 / totalBytesToDownload).toInt()
                    Log.d("MainActivity", "Downloading update: $progress%")
                } else {
                    Log.d("MainActivity", "Downloading update: size not yet known")
                }
            }

            InstallStatus.DOWNLOADED -> {
                Log.d("MainActivity", "Update downloaded, ready to install")
                // UI에서 Snackbar 표시를 위해 상태 업데이트
                viewModel.setUpdateDownloaded(true)
            }

            InstallStatus.INSTALLED -> {
                Log.d("MainActivity", "Update installed")
                viewModel.setUpdateDownloaded(false)
            }

            InstallStatus.FAILED -> {
                Log.e("MainActivity", "Update failed")
                viewModel.setUpdateDownloaded(false)
            }

            else -> {
                Log.d("MainActivity", "Install status: ${state.installStatus()}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkForAppUpdate()

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        // Deep link 처리
        intent?.data?.let { uri ->
            Log.d("MainActivity", "onCreate - Deep link received: $uri")
            viewModel.setPendingDeepLink(uri)
        }

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
            AniPickTheme {
                val metaData by viewModel.metaData.collectAsState()
                val uiState by viewModel.uiState.collectAsState()
                val dialogData by viewModel.dialogData.collectAsState()
                val pendingDeepLink by viewModel.pendingDeepLink.collectAsState()
                val updateDownloaded by viewModel.updateDownloaded.collectAsState()
                val showNoticeDialog by viewModel.showNoticeDialog.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                // GlobalSnackbarManager 이벤트 수신 (리스트 스택 방식)
                var snackBarDataList by remember { mutableStateOf<List<SnackBarData>>(emptyList()) }

                LaunchedEffect(Unit) {
                    globalSnackbarManager.snackbarEvents.collect { snackBarData ->
                        snackBarDataList = snackBarDataList + snackBarData.copy(
                            onDismiss = { snackBarDataList = snackBarDataList.drop(1) }
                        )
                    }
                }

                // 업데이트 다운로드 완료 시 Snackbar 표시
                LaunchedEffect(updateDownloaded) {
                    if (updateDownloaded) {
                        val result = snackbarHostState.showSnackbar(
                            message = "업데이트가 준비되었습니다",
                            actionLabel = "설치",
                            duration = SnackbarDuration.Indefinite
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            Log.d("MainActivity", "User clicked install, completing update")
                            appUpdateManager.completeUpdate()
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    when (uiState) {
                        is MainActivityUiState.Loading -> {
                            Log.d("MainActivity", "UI State: Loading")
                        }

                        is MainActivityUiState.Success -> {
                            val isAutoLogin = (uiState as MainActivityUiState.Success).isAutoLogin
                            val startDestination = if (isAutoLogin) Home else Login

                            Log.d("MainActivity", "UI State: Success, isAutoLogin=$isAutoLogin")

                            val bottomDestinations: List<BottomDestination> = BottomDestination.entries
                            val navController = rememberNavController()
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            TrackScreenNavigation(navController = navController)

                            // 자동 로그인 완료 후 Deep link 처리
                            Log.d("MainActivity", "Setting up LaunchedEffect for deep link, pendingDeepLink=$pendingDeepLink, isAutoLogin=$isAutoLogin")
                            LaunchedEffect(pendingDeepLink, isAutoLogin) {
                                Log.d("MainActivity", "LaunchedEffect triggered! pendingDeepLink=$pendingDeepLink, isAutoLogin=$isAutoLogin")
                                pendingDeepLink?.let { uri ->
                                    if (isAutoLogin) {
                                        Log.d("MainActivity", "Auto login completed, waiting for token to be ready...")
                                        // 토큰이 완전히 로드되고 API 호출에 사용 가능해질 때까지 잠시 대기
                                        delay(300.milliseconds)
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

                            if (showNoticeDialog) {
                                APAlertDialog(
                                    dialogData = DialogData(
                                        type = DialogType.ALERT,
                                        title = UiText.DynamicString("서버 복구 과정 중\n데이터 롤백 및 재가입 안내"),
                                        subTitle = UiText.DynamicString("7월 7일서버 복구 과정에서 데이터가 7월 4일 기준으로 롤백되는 문제가 발생했습니다.\n\n" +
                                                "이로 인해 7월 4일 이후에 작성된 리뷰, 평가, 시청 기록 등의 데이터가 복구되지 않았으며, 해당 기간에 가입하신 계정 정보 또한 손실되었습니다.\n\n" +
                                                "7월 4일 ~ 7월 7일 가입하신 회원분들은 번거러우시겠지만 다시 가입을 진행해 주시기 바랍니다.\n\n" +
                                                "현재 동일한 문제가 재발하지 않도록 서버 증설 및 시스템 개선 작업을 진행하고 있습니다.\n\n" +
                                                "서비스 이용에 큰 불편을 드린 점 진심으로 사과드립니다."),
                                        confirm = UiText.DynamicString("닫기"),
                                        onConfirm = { viewModel.dismissNoticeDialog() },
                                        onDismiss = { viewModel.dismissNoticeDialog() }
                                    ),
                                    style = DialogStyle(
                                        subTitleAlign = TextAlign.Start
                                    )
                                )
                            }
                        }

                        is MainActivityUiState.Error -> {
                            val errorMessage = (uiState as MainActivityUiState.Error).message

                            dialogData?.let { dialogData ->
                                when (dialogData.type) {
                                    DialogType.CONFIRM -> APConfirmDialog(dialogData)
                                    DialogType.SELECT -> APReportReasonDialog(dialogData)
                                    DialogType.ALERT -> APAlertDialog(dialogData)
                                }
                            }

                            APConfirmDialog(
                                dialogData = DialogData(
                                    type = DialogType.CONFIRM,
                                    title = UiText.DynamicString("앱 초기화 실패"),
                                    subTitle = UiText.DynamicString(errorMessage),
                                    dismiss = UiText.DynamicString("종료"),
                                    confirm = UiText.DynamicString("재시도"),
                                    onDismiss = { finish() },
                                    onConfirm = { viewModel.retryAppInit() }
                                )
                            )
                        }
                    }

                    // 업데이트 Snackbar (하단)
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 80.dp) // BottomNavigation 높이만큼 위로
                    )

                    // GlobalSnackbarManager Snackbar (상단)
                    snackBarDataList.firstOrNull()?.let { snackBarData ->
                        APSnackBar(snackBarData = snackBarData)
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

    override fun onResume() {
        super.onResume()
        checkForDownloadedUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }

    /**
     * 앱 업데이트 확인 (Flexible Update, 하루 1번)
     */
    private fun checkForAppUpdate() {
        lifecycleScope.launch {
            try {
                // 하루 1번만 확인
                if (!updatePreference.shouldCheckUpdate(daysInterval = 1)) {
                    Log.d("MainActivity", "Update check skipped - already checked today")
                    return@launch
                }

                val appUpdateInfo = appUpdateManager.appUpdateInfo.await()

                // 업데이트가 있고, Flexible Update가 가능한 경우
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                ) {
                    Log.d("MainActivity", "Update available - starting flexible update")
                    appUpdateManager.registerListener(installStateUpdatedListener)
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                    )
                    updatePreference.saveUpdateCheckTime()
                } else {
                    Log.d("MainActivity", "No update available")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error checking for update", e)
            }
        }
    }

    /**
     * 다운로드 완료된 업데이트가 있는지 확인 (앱이 재시작되었을 때)
     */
    private fun checkForDownloadedUpdate() {
        lifecycleScope.launch {
            try {
                val appUpdateInfo = appUpdateManager.appUpdateInfo.await()
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    Log.d("MainActivity", "Update already downloaded, showing install prompt")
                    viewModel.setUpdateDownloaded(true)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error checking for downloaded update", e)
            }
        }
    }

    /**
     * Deep link URI를 파싱하여 적절한 화면으로 이동
     * 지원하는 형식:
     * - https://anipick.p-e.kr/app/anime/detail/{animeId}
     */
    private fun handleDeepLink(navController: NavHostController, uri: Uri) {
        try {
            Log.d("MainActivity", "Handling deep link: $uri")

            val pathSegments = uri.pathSegments

            when {
                // https://anipick.p-e.kr/app/anime/detail/123 형식
                uri.scheme in listOf("http", "https") &&
                        pathSegments.getOrNull(0) == "app" &&
                        pathSegments.getOrNull(1) == "anime" &&
                        pathSegments.getOrNull(2) == "detail" -> {
                    val animeId = pathSegments.getOrNull(3)?.toLongOrNull()
                    animeId?.let {
                        Log.d("MainActivity", "Navigating to anime detail: $it")
                        navController.navigateToInfoAnime(it)
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