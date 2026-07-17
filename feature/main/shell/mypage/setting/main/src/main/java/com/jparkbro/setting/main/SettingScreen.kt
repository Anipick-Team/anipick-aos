package com.jparkbro.setting.main

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.UserEditType
import com.jparkbro.setting.main.components.CategorySection
import com.jparkbro.setting.main.components.NavigateItem
import com.jparkbro.setting.main.components.SkeletonScreen
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APReportReasonDialog
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack50
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickRed
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.util.ObserveAsEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingRoot(
    onNavigateBack: () -> Unit,
    onNavigateToUserEdit: (UserEditType) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    var dialogData by rememberSaveable { mutableStateOf<DialogData?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SettingEvent.ShowDialog -> {
                dialogData = event.dialogData.copy(
                    onDismiss = { dialogData = null },
                    onConfirm = {
                        event.dialogData.onConfirm(it)
                        dialogData = null
                    }
                )
            }

            SettingEvent.LogoutSuccess -> onNavigateToLogin()
        }
    }

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                onNavigateBack = onNavigateBack
            )
        }

        UiState.Error -> {
            Scaffold(
                topBar = {
                    APTitleTopAppBar(
                        title = stringResource(R.string.setting_header),
                        onNavigateBack = onNavigateBack,
                    )
                },
                containerColor = AniPickSurface
            ) {
                APErrorScreen(
                    onClick = { viewModel.onAction(SettingAction.OnRetryClicked) },
                    modifier = Modifier.padding(it)
                )
            }
        }

        UiState.Success -> {
            SettingScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        SettingAction.NavigateBack -> onNavigateBack()
                        is SettingAction.NavigateToUserEditForm -> onNavigateToUserEdit(action.editType)
                        SettingAction.NavigateToCustomerSupport -> {
                            val intent = Intent(Intent.ACTION_VIEW, "https://forms.gle/SJ7mbQfyfoe2HDLd7".toUri())
                            context.startActivity(intent)
                        }
                        SettingAction.NavigateToServiceTerms -> {
                            val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/terms.html".toUri())
                            context.startActivity(intent)
                        }
                        SettingAction.NavigateToPrivacyPolicy -> {
                            val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/privacy.html".toUri())
                            context.startActivity(intent)
                        }
                        SettingAction.NavigateToOpenSourceLicense -> {
                            OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
                            context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                        }
                        SettingAction.NavigateToNotice -> {
                            val intent = Intent(Intent.ACTION_VIEW, "https://spiral-cowl-f89.notion.site/227b3eed42088098a351ff047659bdcb?source=copy_link".toUri())
                            context.startActivity(intent)
                        }
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }

    dialogData?.let { dialogData ->
        when (dialogData.type) {
            DialogType.CONFIRM -> APConfirmDialog(dialogData)
            DialogType.SELECT -> APReportReasonDialog(dialogData)
            DialogType.ALERT -> APAlertDialog(dialogData)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingScreen(
    state: SettingState,
    onAction: (SettingAction) -> Unit
) {
    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = stringResource(R.string.setting_header),
                onNavigateBack = { onAction(SettingAction.NavigateBack) },
            )
        },
        containerColor = AniPickSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                CategorySection(
                    title = stringResource(R.string.setting_category_account),
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
                        ) {
                            NavigateItem(
                                title = stringResource(R.string.setting_item_change_nickname),
                                content = {
                                    state.nickname?.let {
                                        Text(
                                            text = it ,
                                            style = AniPick14Normal.copy(color = AniPickBlack50)
                                        )
                                    }
                                },
                                onNavigate = { onAction(SettingAction.NavigateToUserEditForm(UserEditType.NICKNAME)) }
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_change_email),
                                content = {
                                    state.email?.let {
                                        Text(
                                            text = it ,
                                            style = AniPick14Normal.copy(color = AniPickBlack50)
                                        )
                                    }
                                },
                                isEnabled = state.provider == LoginProvider.LOCAL,
                                onNavigate = { onAction(SettingAction.NavigateToUserEditForm(UserEditType.EMAIL)) }
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_change_password),
                                content = {
                                    if (state.provider != LoginProvider.LOCAL) {
                                        Text(
                                            text = stringResource(R.string.setting_password_placeholder),
                                            style = AniPick14Normal.copy(color = AniPickGray100)
                                        )
                                    }
                                },
                                isEnabled = state.provider == LoginProvider.LOCAL,
                                onNavigate = { onAction(SettingAction.NavigateToUserEditForm(UserEditType.PASSWORD)) }
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_linked_sns),
                                content = {
                                    state.provider?.displayName?.let {
                                        Text(
                                            text = it,
                                            style = AniPick14Normal.copy(color = AniPickPrimary)
                                        )
                                    }
                                },
                                isEnabled = false,
                                isIcon = false
                            )
                        }
                    }
                )
                CategorySection(
                    title = stringResource(R.string.setting_category_app),
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
                        ) {
                            NavigateItem(
                                title = stringResource(R.string.setting_item_app_version),
                                content = {
                                    Text(
                                        text = BuildConfig.APP_VERSION_NAME,
                                        style = AniPick14Normal.copy(color = AniPickBlack50)
                                    )
                                },
                                isIcon = false
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_contact_us),
                                onNavigate = { onAction(SettingAction.NavigateToCustomerSupport) }
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_terms_of_service),
                                onNavigate = { onAction(SettingAction.NavigateToServiceTerms) }
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_privacy_policy),
                                onNavigate = { onAction(SettingAction.NavigateToPrivacyPolicy) }
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_open_source_license),
                                onNavigate = { onAction(SettingAction.NavigateToOpenSourceLicense) }
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_notice),
                                onNavigate = { onAction(SettingAction.NavigateToNotice) }
                            )
                        }
                    }
                )
                CategorySection(
                    title = stringResource(R.string.setting_category_etc),
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
                        ) {
                            NavigateItem(
                                title = stringResource(R.string.setting_item_logout),
                                titleColor = AniPickRed,
                                isIcon = false,
                                onNavigate = { onAction(SettingAction.OnLogoutClicked) }
                            )
                            NavigateItem(
                                title = stringResource(R.string.setting_item_withdraw),
                                titleColor = AniPickRed,
                                isIcon = false,
                                onNavigate = { onAction(SettingAction.NavigateToUserEditForm(UserEditType.WITHDRAWAL)) }
                            )
                        }
                    }
                )
            }
        }
    }
}