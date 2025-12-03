package com.jparkbro.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.exception.ApiException
import com.jparkbro.model.setting.ProfileEditType
import com.jparkbro.model.setting.UserInfo
import com.jparkbro.ui.APAlertDialog
import com.jparkbro.ui.APConfirmDialog
import com.jparkbro.ui.APLabelTextField
import com.jparkbro.ui.APSurfaceTextFieldWithTrailing
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.BulletPointText
import com.jparkbro.ui.DialogData
import com.jparkbro.ui.DialogType
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.EmailValidator
import com.jparkbro.ui.util.PasswordValidator

@Composable
internal fun ProfileEdit(
    type: ProfileEditType,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onPopBackWithRefresh: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val userInfo by viewModel.userInfo.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()

    val newNickname by viewModel.newNickname.collectAsState()
    val newEmail by viewModel.newEmail.collectAsState()
    val currentPassword by viewModel.currentPassword.collectAsState()
    val newPassword by viewModel.newPassword.collectAsState()
    val newPasswordConfirm by viewModel.newPasswordConfirm.collectAsState()

    val errorException by viewModel.errorException.collectAsState()

    ProfileEdit(
        type = type,
        dialogData = dialogData,
        onChangeDialogData = viewModel::updateDialogData,
        newNickname = newNickname,
        onChangeNewNickname = viewModel::updateNewNickname,
        newEmail = newEmail,
        onChangeNewEmail = viewModel::updateNewEmail,
        currentPassword = currentPassword,
        onChangeCurrentPassword = viewModel::updateCurrentPassword,
        newPassword = newPassword,
        onChangeNewPassword = viewModel::updateNewPassword,
        newPasswordConfirm = newPasswordConfirm,
        onChangeNewPasswordConfirm = viewModel::updateNewPasswordConfirm,
        userInfo = userInfo,
        onChangeUserInfo = viewModel::updateUserInfo,
        onLogout = viewModel::logout,
        errorException = errorException,
        onNavigateBack = onNavigateBack,
        onNavigateToLogin = onNavigateToLogin,
        onPopBackWithRefresh = onPopBackWithRefresh,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileEdit(
    type: ProfileEditType,
    dialogData: DialogData? = null,
    onChangeDialogData: (DialogData?) -> Unit,
    newNickname: String = "",
    onChangeNewNickname: (String) -> Unit,
    newEmail: String = "",
    onChangeNewEmail: (String) -> Unit,
    currentPassword: String = "",
    onChangeCurrentPassword: (String) -> Unit,
    newPassword: String = "",
    onChangeNewPassword: (String) -> Unit,
    newPasswordConfirm: String = "",
    onChangeNewPasswordConfirm: (String) -> Unit,
    userInfo: UserInfo?,
    onChangeUserInfo: (ProfileEditType, (Boolean) -> Unit) -> Unit,
    onLogout: ((Boolean) -> Unit) -> Unit,
    errorException: ApiException? = null,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onPopBackWithRefresh: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = when (type) {
                    ProfileEditType.NICKNAME -> "닉네임 변경"
                    ProfileEditType.EMAIL -> "이메일 변경"
                    ProfileEditType.PASSWORD -> "비밀번호 변경"
                    ProfileEditType.WITHDRAWAL -> "회원 탈퇴"
                },
                handleBackNavigation = {
                    onNavigateBack()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(APColors.White)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(12.dp).fillMaxWidth().background(APColors.Surface))
            when (type) {
                ProfileEditType.NICKNAME -> {
                    NicknameEditForm(
                        userInfo = userInfo,
                        newNickname = newNickname,
                        onChangeNewNickname = onChangeNewNickname,
                        errorException = errorException,
                    )
                }
                ProfileEditType.PASSWORD -> {
                    PasswordEditForm(
                        currentPassword = currentPassword,
                        newPassword = newPassword,
                        newPasswordConfirm = newPasswordConfirm,
                        onChangeCurrentPassword = onChangeCurrentPassword,
                        onChangeNewPassword = onChangeNewPassword,
                        onChangeNewPasswordConfirm = onChangeNewPasswordConfirm,
                        errorException = errorException,
                    )
                }
                ProfileEditType.EMAIL -> {
                    EmailEditForm(
                        userInfo = userInfo,
                        newEmail = newEmail,
                        onChangeNewEmail = onChangeNewEmail,
                        currentPassword = currentPassword,
                        onChangeCurrentPassword = onChangeCurrentPassword,
                        errorException = errorException,
                    )
                }
                ProfileEditType.WITHDRAWAL -> {
                    WithdrawalConfirmForm(
                        userInfo = userInfo,
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color(0xFFEDEDED))
                TextButton(
                    onClick = {
                        when (type) {
                            ProfileEditType.WITHDRAWAL -> {
                                onChangeDialogData(
                                    DialogData(
                                        title = "회원탈퇴",
                                        subTitle = "회원탈퇴를 하시겠습니까?",
                                        dismiss = "취소",
                                        confirm = "탈퇴하기",
                                        onDismiss = { onChangeDialogData(null) },
                                        onConfirm = {
                                            onChangeDialogData(null)
                                            onChangeUserInfo(type) { result ->
                                                if (result) onNavigateToLogin()
                                            }
                                        },
                                    )
                                )
                            }
                            ProfileEditType.EMAIL -> {
                                onChangeUserInfo(type) { result ->
                                    if (result) {
                                        onChangeDialogData(
                                            DialogData(
                                                type = DialogType.ALERT,
                                                title = "이메일 변경이 완료되었어요.",
                                                dismiss = "확인",
                                                onDismiss = {
                                                    onLogout { result ->
                                                        if (result) {
                                                            onChangeDialogData(null)
                                                            onNavigateToLogin()
                                                        }
                                                    }
                                                },
                                                errorMsg = "변경된 이메일로 다시 로그인해 주세요."
                                            )
                                        )
                                    }
                                }
                            }
                            else -> {
                                onChangeUserInfo(type) { result ->
                                    if (result) onPopBackWithRefresh()
                                }
                            }
                        }
                    },
                    enabled = when (type) {
                        ProfileEditType.NICKNAME -> newNickname.isNotEmpty()
                        ProfileEditType.PASSWORD -> PasswordValidator.validate(currentPassword) && PasswordValidator.validate(newPassword) && PasswordValidator.validate(newPasswordConfirm)
                        ProfileEditType.EMAIL -> EmailValidator.validate(newEmail) && currentPassword.length >= 8 && currentPassword.length <= 16
                        ProfileEditType.WITHDRAWAL -> true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                        .height(51.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = APColors.White,
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    )
                ) {
                    Text(
                        text = when (type) {
                            ProfileEditType.WITHDRAWAL -> "탈퇴하기"
                            else -> "저장"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }

    dialogData?.let {
        when (it.type) {
            DialogType.ALERT -> {
                APAlertDialog(
                    title = it.title,
                    errorMsg = it.errorMsg,
                    dismiss = it.dismiss,
                    onDismiss = it.onDismiss
                )
            }
            DialogType.CONFIRM -> {
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
    }
}

@Composable
private fun ColumnScope.NicknameEditForm(
    userInfo: UserInfo?,
    newNickname: String,
    onChangeNewNickname: (String) -> Unit,
    errorException: ApiException? = null,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 20.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        APLabelTextField(
            label = "기존 닉네임",
            value = "${userInfo?.nickname}",
            onValueChange = {},
            placeholder = "",
            enabled = false,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            APLabelTextField(
                label = "새 닉네임",
                value = newNickname,
                onValueChange = { onChangeNewNickname(it) },
                placeholder = "새 닉네임 입력",
            )
            if (errorException != null && (errorException.errorCode == 116 || errorException.errorCode == 117 || errorException.errorCode == 118)) {
                Text(
                    text = errorException.errorValue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Point
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.PasswordEditForm(
    currentPassword: String,
    newPassword: String,
    newPasswordConfirm: String,
    onChangeCurrentPassword: (String) -> Unit,
    onChangeNewPassword: (String) -> Unit,
    onChangeNewPasswordConfirm: (String) -> Unit,
    errorException: ApiException? = null
) {
    var isCurrentVisibility by remember { mutableStateOf(false) }
    var isNewVisibility by remember { mutableStateOf(false) }
    var isConfirmVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 20.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            APSurfaceTextFieldWithTrailing(
                label = "현재 비밀번호",
                value = currentPassword,
                onValueChange = { onChangeCurrentPassword(it) },
                placeholder = "현재 비밀번호",
                isVisibility = isCurrentVisibility,
                trailingButton = {
                    IconButton(
                        onClick = { isCurrentVisibility = !isCurrentVisibility }
                    ) {
                        Image(
                            painter = if (!isCurrentVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                            contentDescription = "",
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
            )
            if (errorException != null && errorException.errorCode == 107) {
                Text(
                    text = errorException.errorValue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Point
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            APSurfaceTextFieldWithTrailing(
                label = "새 비밀번호",
                value = newPassword,
                onValueChange = { onChangeNewPassword(it) },
                placeholder = "새 비밀번호",
                isVisibility = isNewVisibility,
                trailingButton = {
                    IconButton(
                        onClick = { isNewVisibility = !isNewVisibility }
                    ) {
                        Image(
                            painter = if (!isNewVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                            contentDescription = "",
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
            )
            Text(
                text = "8~16자의 영문 대/소문자, 숫자, 특수문자를 조합하여 입력해주세요.",
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                color = APColors.TextGray
            )
            if (errorException != null && errorException.errorCode == 110) {
                Text(
                    text = errorException.errorValue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Point
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            APSurfaceTextFieldWithTrailing(
                label = "새 비밀번호 확인",
                value = newPasswordConfirm,
                onValueChange = { onChangeNewPasswordConfirm(it) },
                placeholder = "새 비밀번호 확인",
                isVisibility = isConfirmVisibility,
                trailingButton = {
                    IconButton(
                        onClick = { isConfirmVisibility = !isConfirmVisibility }
                    ) {
                        Image(
                            painter = if (!isConfirmVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                            contentDescription = "",
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
            )
            if (errorException != null && errorException.errorCode == 108) {
                Text(
                    text = errorException.errorValue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Point
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.EmailEditForm(
    userInfo: UserInfo?,
    newEmail: String,
    onChangeNewEmail: (String) -> Unit,
    currentPassword: String,
    onChangeCurrentPassword: (String) -> Unit,
    errorException: ApiException? = null
) {
    var isPasswordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 20.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        APLabelTextField(
            label = "기존 이메일",
            value = "${userInfo?.email}",
            onValueChange = {},
            placeholder = "",
            modifier = Modifier,
            enabled = false,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            APLabelTextField(
                label = "새 이메일",
                value = newEmail,
                onValueChange = { onChangeNewEmail(it) },
                placeholder = "새 이메일 입력",
            )
            if (errorException != null && (errorException.errorCode == 102 || errorException.errorCode == 103 || errorException.errorCode == 109)) {
                Text(
                    text = errorException.errorValue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Point
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            APSurfaceTextFieldWithTrailing(
                label = "비밀번호",
                value = currentPassword,
                onValueChange = { onChangeCurrentPassword(it) },
                placeholder = "비밀번호 입력",
                isVisibility = isPasswordVisibility,
                trailingButton = {
                    IconButton(
                        onClick = { isPasswordVisibility = !isPasswordVisibility }
                    ) {
                        Image(
                            painter = if (!isPasswordVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                            contentDescription = "",
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
            )
            if (errorException != null && (errorException.errorCode == 105 || errorException.errorCode == 106 || errorException.errorCode == 107)) {
                Text(
                    text = errorException.errorValue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Point
                )
            }
        }
        Column {
            val guideTextStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = APColors.TextGray
            )
            BulletPointText(
                text = "반드시 본인 명의의 이메일을 입력해주세요.",
                textStyle = guideTextStyle
            )
            BulletPointText(
                text = "본 이메일은 계정 분실 시 아이디 및 비밀번호 찾기, 개인정보 관련 주요 고지사항 안내 등에 사용됩니다.",
                textStyle = guideTextStyle
            )
        }
    }
}

@Composable
private fun ColumnScope.WithdrawalConfirmForm(
    userInfo: UserInfo?,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 20.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(56.dp)
    ) {
        APLabelTextField(
            label = "계정 이메일",
            value = "${userInfo?.email}",
            onValueChange = {},
            placeholder = "",
            modifier = Modifier,
            enabled = false,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "탈퇴 시 주의사항",
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                color = APColors.Black
            )
            Text(
                text = "탈퇴 시, 회원정보는 당사의 개인정보처리방침에 따라 삭제 또는 격리하여 보존 조치되며, 삭제된 데이터는 복구가 불가능합니다. 서비스 내에서 남긴 리뷰는 탈퇴 후에 자동 삭제되지 않습니다.",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = APColors.TextGray
            )
            Text(
                text = "\'회원탈퇴\'를 누르는 것은 상기 안내사항을 모두 확인하였으며 이에 동의함을 의미합니다.",
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
                color = APColors.Point
            )
        }
    }
}