package com.jparkbro.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APBackStackTopAppBar
import com.jparkbro.ui.components.APLabeledTextField
import com.jparkbro.ui.components.APPrimaryActionButton
import com.jparkbro.ui.components.APSnackBarRe
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick14Regular
import com.jparkbro.ui.theme.AniPick24Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.EyeClosedIcon
import com.jparkbro.ui.theme.EyeOpenedIcon
import com.jparkbro.ui.util.ObserveAsEvents

@Composable
internal fun EmailLoginRoot(
    onNavigateBack: () -> Unit,
    onNavigateToEmailRegister: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    viewModel: EmailLoginViewModel = hiltViewModel()
) {
    var dialogData by rememberSaveable { mutableStateOf<DialogData?>(null) }
    var snackBarData by rememberSaveable { mutableStateOf<List<SnackBarData>>(emptyList()) }

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is EmailLoginEvent.LoginSuccess -> {
                if (event.reviewCompletedYn) {
                    onNavigateToPreferenceSetup()
                } else {
                    onNavigateToHome()
                }
            }
            is EmailLoginEvent.LoginFailWithDialog -> {
                dialogData = event.dialogData.copy(
                    onConfirm = { dialogData = null }
                )
            }

            is EmailLoginEvent.LoginFailWithSnackBar -> {
                snackBarData = snackBarData + event.snackBarData.copy(
                    onDismiss = {
                        snackBarData = snackBarData.drop(1)
                    }
                )
            }
        }
    }

    EmailLoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmailLoginAction.NavigateBack -> onNavigateBack()
                EmailLoginAction.NavigateToRegister -> onNavigateToEmailRegister()
                EmailLoginAction.NavigateToFindPassword -> onNavigateToFindPassword()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

    dialogData?.let {
        APAlertDialog(it)
    }

    snackBarData.firstOrNull()?.let { snackBarData ->
        APSnackBarRe(snackBarData)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailLoginScreen(
    state: EmailLoginState,
    onAction: (EmailLoginAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { APBackStackTopAppBar(onNavigateBack = { onAction(EmailLoginAction.NavigateBack) }) },
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
                EmailPasswordInputSection(
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
            text = stringResource(R.string.email_login_title),
            style = AniPick24Bold.copy(color = AniPickBlack),
        )
        Text(
            text = stringResource(R.string.email_login_subtitle),
            style = AniPick14Regular.copy(color = AniPickBlack),
        )
    }
}

@Composable
private fun EmailPasswordInputSection(
    state: EmailLoginState,
    onAction: (EmailLoginAction) -> Unit,
    focusManager: FocusManager
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_40))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            APLabeledTextField(
                label = stringResource(R.string.email_login_email),
                state = state.email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                onKeyboardAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                placeholder = stringResource(R.string.email_login_email_placeholder),
            )
            state.emailErrorMessage?.let {
                Text(
                    text = state.emailErrorMessage.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
        APLabeledTextField(
            label = stringResource(R.string.email_login_password),
            state = state.password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            onKeyboardAction = {
                focusManager.clearFocus()
                onAction(EmailLoginAction.OnLoginClicked)
            },
            placeholder = stringResource(R.string.email_login_password_placeholder),
            trailingIcon = {
                Icon(
                    imageVector = if (state.isPasswordVisible) EyeOpenedIcon else EyeClosedIcon,
                    contentDescription = stringResource(R.string.visible_icon),
                    tint = AniPickGray500,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAction(EmailLoginAction.OnTogglePasswordVisibility) }
                        .padding(dimensionResource(R.dimen.padding_small))
                )
            },
            isPassword = true,
            showPassword = state.isPasswordVisible,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = stringResource(R.string.email_login_register),
                    style = AniPick14Normal.copy(color = AniPickGray400),
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAction(EmailLoginAction.NavigateToRegister) }
                        .padding(dimensionResource(R.dimen.padding_extra_small))
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.spacing_default))
                    .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                color = AniPickGray400
            )
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(R.string.email_login_find_password),
                    style = AniPick14Normal.copy(color = AniPickGray400),
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAction(EmailLoginAction.NavigateToFindPassword) }
                        .padding(dimensionResource(R.dimen.padding_extra_small))
                )
            }
        }
        state.loginErrorMessage?.let {
            Text(
                text = state.loginErrorMessage,
                style = AniPick14Normal.copy(color = AniPickPoint),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Footer(
    state: EmailLoginState,
    onAction: (EmailLoginAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        HorizontalDivider(color = AniPickGray100)
        APPrimaryActionButton(
            text = stringResource(R.string.email_login_login_btn),
            onClick = { onAction(EmailLoginAction.OnLoginClicked) },
            enabled = state.isLoginEnabled,
            isLoading = state.isLoggingIn
        )
    }
}

@DevicePreviews
@Composable
private fun EmailLoginScreenPreview() {
    EmailLoginScreen(
        state = EmailLoginState(),
        onAction = {}
    )
}