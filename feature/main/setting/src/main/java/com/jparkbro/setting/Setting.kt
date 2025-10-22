package com.jparkbro.setting

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.setting.ProfileEditType
import com.jparkbro.model.setting.UserInfo
import com.jparkbro.ui.APConfirmDialog
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.DialogData
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors

@Composable
internal fun Setting(
    onNavigateBack: () -> Unit,
    onNavigateToProfileEdit: (ProfileEditType) -> Unit,
    onNavigateToLogin: () -> Unit,
    onCheckSettingRefresh: () -> Boolean,
    onPopBackWithRefresh: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()

    val userInfo by viewModel.userInfo.collectAsState()

    Setting(
        uiState = uiState,
        dialogData = dialogData,
        onChangeDialogData = viewModel::updateDialogData,
        userInfo = userInfo,
        onLogout = viewModel::logout,
        onGetUserInfo = viewModel::getUserInfo,
        onNavigateBack = onNavigateBack,
        onNavigateToProfileEdit = onNavigateToProfileEdit,
        onNavigateToLogin = onNavigateToLogin,
        onCheckSettingRefresh = onCheckSettingRefresh,
        onPopBackWithRefresh = onPopBackWithRefresh,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Setting(
    uiState: SettingUiState = SettingUiState.Loading,
    dialogData: DialogData? = null,
    onChangeDialogData: (DialogData?) -> Unit,
    userInfo: UserInfo? = null,
    onLogout: ((Boolean) -> Unit) -> Unit,
    onGetUserInfo: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToProfileEdit: (ProfileEditType) -> Unit,
    onNavigateToLogin: () -> Unit,
    onCheckSettingRefresh: () -> Boolean,
    onPopBackWithRefresh: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val isUpdate = onCheckSettingRefresh()
        if (isUpdate) {
            onGetUserInfo()
            onPopBackWithRefresh()
        }
    }

    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = "설정",
                handleBackNavigation = { onNavigateBack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(APColors.Surface),
            contentPadding = PaddingValues(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(APColors.White)
                        .padding(horizontal = 20.dp, vertical = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(38.dp)
                ) {
                    Text(
                        text = "계정",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        color = APColors.Black
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onNavigateToProfileEdit(ProfileEditType.NICKNAME) },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "닉네임 변경",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${userInfo?.nickname}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                    color = Color(0xFF747382)
                                )
                                Icon(
                                    painter = painterResource(R.drawable.ic_chevron_right),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(18.dp),
                                    tint = Color(0xFF667080)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    if (userInfo?.provider == LoginProvider.LOCAL) {
                                        onNavigateToProfileEdit(ProfileEditType.EMAIL)
                                    }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "이메일 변경",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${userInfo?.email}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                    color = Color(0xFF747382)
                                )
                                Icon(
                                    painter = painterResource(R.drawable.ic_chevron_right),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(18.dp),
                                    tint = if (userInfo?.provider == LoginProvider.LOCAL) Color(0xFF667080) else APColors.LightGray
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    if (userInfo?.provider == LoginProvider.LOCAL) {
                                        onNavigateToProfileEdit(ProfileEditType.PASSWORD)
                                    }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "비밀번호 변경",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (userInfo?.provider != LoginProvider.LOCAL) {
                                    Text(
                                        text = "sns 간편가입된 계정입니다.",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        color = APColors.Gray
                                    )
                                }
                                Icon(
                                    painter = painterResource(R.drawable.ic_chevron_right),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(18.dp),
                                    tint = if (userInfo?.provider == LoginProvider.LOCAL) Color(0xFF667080) else APColors.LightGray
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "연동 SNS",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Text(
                                text = "${userInfo?.provider?.displayName}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Primary
                            )
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(APColors.White)
                        .padding(horizontal = 20.dp, vertical = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(38.dp)
                ) {
                    Text(
                        text = "앱 설정",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        color = APColors.Black
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "앱 버전",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Text(
                                text = BuildConfig.APP_VERSION_NAME,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = Color(0xFF747382)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, "https://forms.gle/SJ7mbQfyfoe2HDLd7".toUri())
                                    context.startActivity(intent)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "문의하기",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp),
                                tint = Color(0xFF667080)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/terms.html".toUri())
                                    context.startActivity(intent)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "서비스 이용약관",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp),
                                tint = Color(0xFF667080)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/privacy.html".toUri())
                                    context.startActivity(intent)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "개인정보 처리방침",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp),
                                tint = Color(0xFF667080)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
                                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "오픈소스 라이선스",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp),
                                tint = Color(0xFF667080)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, "https://spiral-cowl-f89.notion.site/227b3eed42088098a351ff047659bdcb?source=copy_link".toUri())
                                    context.startActivity(intent)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "공지사항",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp),
                                tint = Color(0xFF667080)
                            )
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(APColors.White)
                        .padding(horizontal = 20.dp, vertical = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(38.dp)
                ) {
                    Text(
                        text = "기타",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W600,
                        color = APColors.Black
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        Text(
                            text = "로그아웃",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFEA4335),
                            modifier = Modifier
                                .clickable {
                                    onChangeDialogData(
                                        DialogData(
                                            title = "로그아웃",
                                            subTitle = "로그아웃 하시겠습니까?",
                                            dismiss = "취소",
                                            confirm = "로그아웃",
                                            onDismiss = { onChangeDialogData(null) },
                                            onConfirm = {
                                                onChangeDialogData(null)
                                                onLogout { result ->
                                                    if (result) {
                                                        onNavigateToLogin()
                                                    }
                                                }
                                            },
                                        )
                                    )
                                },
                        )
                        Text(
                            text = "회원 탈퇴",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFEA4335),
                            modifier = Modifier
                                .clickable { onNavigateToProfileEdit(ProfileEditType.WITHDRAWAL) },
                        )
                    }
                }
            }
        }
    }
    dialogData?.let {
        APConfirmDialog(
            title = it.title,
            subTitle = it.subTitle,
            dismiss = it.dismiss,
            confirm = it.confirm,
            onDismiss = it.onDismiss,
            onConfirm = it.onConfirm
        )
    }
}