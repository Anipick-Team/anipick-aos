package com.jparkbro.verification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APBackStackTopAppBar
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APLabeledTextField
import com.jparkbro.ui.components.APLabeledTextFieldWithSideComponent
import com.jparkbro.ui.components.APPrimaryActionButton
import com.jparkbro.ui.components.APSnackBarRe
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick14Regular
import com.jparkbro.ui.theme.AniPick16Bold
import com.jparkbro.ui.theme.AniPick24Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickExtraSmallShape
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.ObserveAsEvents
import java.util.Locale

@Composable
internal fun PasswordVerificationRoot(
    onNavigateBack: () -> Unit,
    onNavigateToPasswordReset: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: PasswordVerificationViewModel = hiltViewModel()
) {
    var dialogData by rememberSaveable { mutableStateOf<DialogData?>(null) }
    var snackBarData by rememberSaveable { mutableStateOf<List<SnackBarData>>(emptyList()) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PasswordVerificationEvent.VerificationSuccess -> {
                onNavigateToPasswordReset(event.email)
            }
            is PasswordVerificationEvent.VerificationWithDialog -> {
                dialogData = event.dialogData.copy(
                    onDismiss = { dialogData = null },
                    onConfirm = {
                        dialogData = null
                        onNavigateToLogin()
                    }
                )
            }
            is PasswordVerificationEvent.VerificationWithSnackBar -> {
                snackBarData = snackBarData + event.snackBarData.copy(
                    onDismiss = {
                        snackBarData = snackBarData.drop(1)
                    }
                )
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    PasswordVerificationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                PasswordVerificationAction.NavigateBack -> onNavigateBack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

    dialogData?.let {
        APConfirmDialog(it)
    }

    snackBarData.firstOrNull()?.let { snackBarData ->
        APSnackBarRe(snackBarData)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordVerificationScreen(
    state: PasswordVerificationState,
    onAction: (PasswordVerificationAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { APBackStackTopAppBar(onNavigateBack = { onAction(PasswordVerificationAction.NavigateBack) }) },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
        containerColor = AniPickWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(R.dimen.padding_large))
                    .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_64))
            ) {
                Header()
                EmailCodeInputSection(
                    state = state,
                    onAction = onAction,
                    focusManager = focusManager,
                )
            }
            Footer(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun Header() {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
    ) {
        Text(
            text = stringResource(R.string.password_verification_title),
            style = AniPick24Bold.copy(color = AniPickBlack),
        )
        Text(
            text = stringResource(R.string.password_verification_subtitle),
            style = AniPick14Regular.copy(color = AniPickBlack),
        )
    }
}

@Composable
private fun EmailCodeInputSection(
    state: PasswordVerificationState,
    onAction: (PasswordVerificationAction) -> Unit,
    focusManager: FocusManager
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_40))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextFieldWithSideComponent(
                label = stringResource(R.string.password_verification_email),
                state = state.email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                onKeyboardAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                placeholder = stringResource(R.string.password_verification_email_placeholder),
                sideComponent = {
                    TextButton(
                        onClick = { onAction(PasswordVerificationAction.OnGetCodeClicked) },
                        modifier = Modifier
                            .height(52.dp)
                            .width(124.dp),
                        enabled = state.codeSendState == CodeSendState.Idle || state.codeSendState == CodeSendState.Ready,
                        shape = AniPickExtraSmallShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AniPickPrimary,
                            contentColor = AniPickWhite,
                            disabledContainerColor = AniPickWhite,
                            disabledContentColor = AniPickGray100
                        ),
                        border = when (state.codeSendState) {
                            CodeSendState.Loading, CodeSendState.Cooldown -> {
                                BorderStroke(dimensionResource(R.dimen.border_width_default), Color(0xFFEBEBEB))
                            }
                            CodeSendState.Idle, CodeSendState.Ready -> null
                        },
                    ) {
                        when (state.codeSendState) {
                            CodeSendState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(dimensionResource(R.dimen.icon_size_extra_large)),
                                    color = AniPickGray100,
                                )
                            }
                            else -> {
                                Text(
                                    text = when (state.codeSendState) {
                                        CodeSendState.Cooldown -> stringResource(R.string.password_verification_code_sending, state.resendCooldownSeconds)
                                        CodeSendState.Ready -> stringResource(R.string.password_verification_code_resend)
                                        else -> stringResource(R.string.password_verification_code_send)
                                    },
                                    style = AniPick16Bold.copy(
                                        color = when (state.codeSendState) {
                                            CodeSendState.Cooldown -> AniPickGray100
                                            else -> AniPickWhite
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            )
            state.emailErrorMessage?.let {
                Text(
                    text = state.emailErrorMessage.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextField(
                label = stringResource(R.string.password_verification_verification_code),
                state = state.code,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                placeholder = stringResource(R.string.password_verification_verification_code_placeholder),
                trailingIcon = {
                    if (state.codeSendState != CodeSendState.Idle) {
                        val minutes = state.codeExpirationSeconds / 60
                        val seconds = state.codeExpirationSeconds % 60
                        val time = String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
                        Text(
                            text = time,
                            style = AniPick14Normal.copy(color = AniPickPoint),
                            modifier = Modifier
                                .padding(end = dimensionResource(R.dimen.padding_default))
                        )
                    }
                }
            )
            state.codeErrorMessage?.let {
                Text(
                    text = state.codeErrorMessage.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
    }
}

@Composable
private fun Footer(
    state: PasswordVerificationState,
    onAction: (PasswordVerificationAction) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        HorizontalDivider(color = AniPickGray100)
        APPrimaryActionButton(
            text = stringResource(R.string.password_verification_next_btn),
            onClick = { onAction(PasswordVerificationAction.OnNextClicked) },
            isLoading = state.isVerifyingCode,
            enabled = state.isNextEnabled,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
        )
    }
}

@DevicePreviews
@Composable
private fun PasswordVerificationScreenPreview() {
    PasswordVerificationScreen(
        state = PasswordVerificationState(),
        onAction = {}
    )
}