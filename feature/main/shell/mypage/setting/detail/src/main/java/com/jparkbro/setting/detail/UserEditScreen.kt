package com.jparkbro.setting.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.UserEditType
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APBulletPointText
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APLabeledTextField
import com.jparkbro.ui.components.APLabeledTextFieldWithLabelTrailingComponent
import com.jparkbro.ui.components.APPrimaryActionButton
import com.jparkbro.ui.components.APReportReasonDialog
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.CheckIcon
import com.jparkbro.ui.theme.EyeClosedIcon
import com.jparkbro.ui.theme.EyeOpenedIcon
import com.jparkbro.ui.util.ObserveAsEvents
import com.jparkbro.ui.util.extension.advancedImePadding

@Composable
internal fun UserEditRoot(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: UserEditViewModel = hiltViewModel()
) {
    var dialogData by rememberSaveable { mutableStateOf<DialogData?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is UserEditEvent.ShowDialog -> {
                dialogData = event.dialogData.copy(
                    onDismiss = { dialogData = null },
                    onConfirm = {
                        event.dialogData.onConfirm(it)
                        dialogData = null
                    }
                )
            }
            UserEditEvent.UpdateSuccess -> onNavigateBack()
            UserEditEvent.WithdrawalSuccess -> onNavigateToLogin()
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    UserEditScreen(
        state = state,
        onAction = { action ->
            when (action) {
                UserEditAction.NavigateBack -> onNavigateBack()
            }
            viewModel.onAction(action)
        }
    )

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
private fun UserEditScreen(
    state: UserEditState,
    onAction: (UserEditAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        topBar = {
            APTitleTopAppBar(
                title = when (state.editType) {
                    UserEditType.NICKNAME -> stringResource(R.string.change_nickname)
                    UserEditType.PASSWORD -> stringResource(R.string.change_password)
                    UserEditType.EMAIL -> stringResource(R.string.change_email)
                    UserEditType.WITHDRAWAL -> stringResource(R.string.account_withdrawal)
                },
                onNavigateBack = { onAction(UserEditAction.NavigateBack) },
            )
        },
        containerColor = AniPickWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_36))
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(R.dimen.padding_large))
            ) {
                when (state.editType) {
                    UserEditType.NICKNAME -> EditNickname(
                        state = state,
                        focusManager = focusManager
                    )
                    UserEditType.PASSWORD -> EditPassword(
                        state = state,
                        focusManager = focusManager
                    )
                    UserEditType.EMAIL -> EditEmail(
                        state = state,
                        focusManager = focusManager
                    )
                    UserEditType.WITHDRAWAL -> Withdrawal(
                        state = state,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
            ) {
                HorizontalDivider(color = AniPickGray100)
                APPrimaryActionButton(
                    text = stringResource(
                        when (state.editType) {
                            UserEditType.WITHDRAWAL -> R.string.user_edit_withdraw_action_submit
                            else -> R.string.user_edit_action_save
                        }
                    ),
                    onClick = {
                        when (state.editType) {
                            UserEditType.NICKNAME -> onAction(UserEditAction.OnNicknameChanged)
                            UserEditType.PASSWORD -> onAction(UserEditAction.OnPasswordChanged)
                            UserEditType.EMAIL -> onAction(UserEditAction.OnEmailChanged)
                            UserEditType.WITHDRAWAL -> onAction(UserEditAction.OnWithdrawalClicked)
                        }
                    },
                    enabled = when (state.editType) {
                        UserEditType.NICKNAME -> state.isChangeNicknameEnabled
                        UserEditType.PASSWORD -> state.isChangePasswordEnabled
                        UserEditType.EMAIL -> state.isChangeEmailEnabled
                        UserEditType.WITHDRAWAL -> true
                    },
                    isLoading = state.isLoading,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_large))
                )
            }
        }
    }
}

@Composable
private fun EditNickname(
    state: UserEditState,
    focusManager: FocusManager
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        APLabeledTextField(
            label = stringResource(R.string.user_edit_nickname_current),
            state = state.nickname,
            readOnly = true,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextField(
                label = stringResource(R.string.user_edit_nickname_new),
                state = state.newNickname,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                placeholder = stringResource(R.string.user_edit_nickname_new),
            )
            state.nicknameErrorMessage?.let {
                Text(
                    text = it.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
    }
}

@Composable
private fun EditEmail(
    state: UserEditState,
    focusManager: FocusManager
) {
    var isVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        APLabeledTextField(
            label = stringResource(R.string.user_edit_email_current),
            state = state.email,
            readOnly = true,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextField(
                label = stringResource(R.string.user_edit_email_new),
                state = state.newEmail,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                onKeyboardAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                placeholder = stringResource(R.string.user_edit_email_new),
            )
            state.emailErrorMessage?.let {
                Text(
                    text = it.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextField(
                label = stringResource(R.string.email_login_password),
                state = state.currentPassword,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                placeholder = stringResource(R.string.email_login_password_placeholder),
                trailingIcon = {
                    Icon(
                        imageVector = if (isVisible) EyeOpenedIcon else EyeClosedIcon,
                        contentDescription = stringResource(R.string.visible_icon),
                        tint = AniPickGray500,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { isVisible = !isVisible }
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                },
                isPassword = true,
                showPassword = isVisible,
            )
            state.currentPasswordErrorMessage?.let {
                Text(
                    text = it.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            APBulletPointText(
                text = stringResource(R.string.user_edit_email_notice_title),
                textStyle = AniPick14Normal.copy(color = AniPickGray400)
            )
            APBulletPointText(
                text = stringResource(R.string.user_edit_email_notice_desc),
                textStyle = AniPick14Normal.copy(color = AniPickGray400)
            )
        }
    }

}

@Composable
private fun EditPassword(
    state: UserEditState,
    focusManager: FocusManager
) {
    var isCurrentPasswordVisible by remember { mutableStateOf(false) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isNewPasswordConfirmVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextField(
                label = stringResource(R.string.user_edit_password_current),
                state = state.currentPassword,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                placeholder = stringResource(R.string.user_edit_password_current),
                trailingIcon = {
                    Icon(
                        imageVector = if (isCurrentPasswordVisible) EyeOpenedIcon else EyeClosedIcon,
                        contentDescription = stringResource(R.string.visible_icon),
                        tint = AniPickGray500,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { isCurrentPasswordVisible = !isCurrentPasswordVisible }
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                },
                isPassword = true,
                showPassword = isCurrentPasswordVisible,
            )
            state.currentPasswordErrorMessage?.let {
                Text(
                    text = it.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextFieldWithLabelTrailingComponent(
                label = stringResource(R.string.user_edit_password_new),
                state = state.newPassword,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                placeholder = stringResource(R.string.user_edit_password_new),
                trailingIcon = {
                    Icon(
                        imageVector = if (isNewPasswordVisible) EyeOpenedIcon else EyeClosedIcon,
                        contentDescription = stringResource(R.string.visible_icon),
                        tint = AniPickGray500,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { isNewPasswordVisible = !isNewPasswordVisible }
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                },
                isPassword = true,
                showPassword = isNewPasswordVisible,
                labelTrailingComponent = {
                    Icon(
                        imageVector = CheckIcon,
                        contentDescription = stringResource(R.string.check_icon),
                        tint = if (state.isNewPasswordValid.isValidPassword) AniPickPrimary else AniPickGray400,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_medium))
                    )
                }
            )
            state.newPasswordErrorMessage?.let {
                Text(
                    text = it.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextField(
                label = stringResource(R.string.user_edit_password_confirm),
                state = state.newPasswordConfirm,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                placeholder = stringResource(R.string.user_edit_password_confirm),
                trailingIcon = {
                    Icon(
                        imageVector = if (isNewPasswordConfirmVisible) EyeOpenedIcon else EyeClosedIcon,
                        contentDescription = stringResource(R.string.visible_icon),
                        tint = AniPickGray500,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { isNewPasswordConfirmVisible = !isNewPasswordConfirmVisible }
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                },
                isPassword = true,
                showPassword = isNewPasswordConfirmVisible,
            )
            state.newPasswordConfirmErrorMessage?.let {
                Text(
                    text = it.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
    }
}

@Composable
private fun Withdrawal(
    state: UserEditState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_56))
    ) {
        APLabeledTextField(
            label = stringResource(R.string.user_edit_withdraw_email_label),
            state = state.email,
            readOnly = true,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            Text(
                text = stringResource(R.string.user_edit_withdraw_notice_title),
                style = AniPick20Bold.copy(color = AniPickBlack)
            )
            Text(
                text = stringResource(R.string.user_edit_withdraw_notice_desc),
                style = AniPick14Normal.copy(color = AniPickGray400)
            )
            Text(
                text = stringResource(R.string.user_edit_withdraw_agreement_check),
                style = AniPick12Normal.copy(color = AniPickPoint),
            )
        }
    }
}