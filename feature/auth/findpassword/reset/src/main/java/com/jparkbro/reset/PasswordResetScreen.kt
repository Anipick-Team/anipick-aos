package com.jparkbro.reset

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APBackStackTopAppBar
import com.jparkbro.ui.components.APLabeledTextField
import com.jparkbro.ui.components.APLabeledTextFieldWithLabelTrailingComponent
import com.jparkbro.ui.components.APPrimaryActionButton
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick14Regular
import com.jparkbro.ui.theme.AniPick24Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.CheckIcon
import com.jparkbro.ui.theme.EyeClosedIcon
import com.jparkbro.ui.theme.EyeOpenedIcon
import com.jparkbro.ui.util.ObserveAsEvents

@Composable
internal fun PasswordResetRoot(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: PasswordResetViewModel = hiltViewModel()
) {
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            PasswordResetEvent.PasswordChangeSuccess -> {
                onNavigateToLogin()
            }
            else -> Unit
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    PasswordResetScreen(
        state = state,
        onAction = { action ->
            when (action) {
                PasswordResetAction.NavigateBack -> onNavigateBack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordResetScreen(
    state: PasswordResetState,
    onAction: (PasswordResetAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { APBackStackTopAppBar(onNavigateBack = { onAction(PasswordResetAction.NavigateBack) }) },
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
                PasswordInputSection(
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
            text = stringResource(R.string.password_reset_title),
            style = AniPick24Bold.copy(color = AniPickBlack),
        )
        Text(
            text = stringResource(R.string.password_reset_subtitle),
            style = AniPick14Regular.copy(color = AniPickBlack),
        )
    }
}

@Composable
private fun PasswordInputSection(
    state: PasswordResetState,
    onAction: (PasswordResetAction) -> Unit,
    focusManager: FocusManager
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_40))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            APLabeledTextFieldWithLabelTrailingComponent(
                label = stringResource(R.string.password_reset_password),
                state = state.password,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                onKeyboardAction = {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Next
                    )
                },
                placeholder = stringResource(R.string.password_reset_password_placeholder),
                trailingIcon = {
                    Icon(
                        imageVector = if (state.isPasswordVisible) EyeOpenedIcon else EyeClosedIcon,
                        contentDescription = stringResource(R.string.visible_icon),
                        tint = AniPickGray500,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onAction(PasswordResetAction.OnTogglePasswordVisibility) }
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                },
                isPassword = true,
                showPassword = state.isPasswordVisible,
                labelTrailingComponent = {
                    Icon(
                        imageVector = CheckIcon,
                        contentDescription = stringResource(R.string.check_icon),
                        tint = if (state.isPasswordValid.isValidPassword) AniPickPrimary else AniPickGray400,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_medium))
                    )
                }
            )
            Text(
                text = stringResource(R.string.password_reset_password_info),
                style = AniPick12Normal.copy(color = AniPickGray400)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            APLabeledTextField(
                label = stringResource(R.string.password_reset_confirm_password),
                state = state.confirmPassword,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                onKeyboardAction = {
                    focusManager.clearFocus()
                },
                placeholder = stringResource(R.string.password_reset_confirm_password_placeholder),
                trailingIcon = {
                    Icon(
                        imageVector = if (state.isConfirmPasswordVisible) EyeOpenedIcon else EyeClosedIcon,
                        contentDescription = stringResource(R.string.visible_icon),
                        tint = AniPickGray500,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onAction(PasswordResetAction.OnTogglePasswordConfirmVisibility) }
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                },
                isPassword = true,
                showPassword = state.isConfirmPasswordVisible,
            )
            state.confirmPasswordErrorMessage?.let {
                Text(
                    text = state.confirmPasswordErrorMessage.asString(),
                    style = AniPick14Normal.copy(color = AniPickPoint)
                )
            }
        }
    }
}

@Composable
private fun Footer(
    state: PasswordResetState,
    onAction: (PasswordResetAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        HorizontalDivider(color = AniPickGray100)
        APPrimaryActionButton(
            text = stringResource(R.string.password_reset_change_btn),
            onClick = { onAction(PasswordResetAction.OnCompleteClicked) },
            enabled = state.isChangeEnabled,
            isLoading = state.isChangeIng
        )
    }
}

@DevicePreviews
@Composable
private fun PasswordResetScreenPreview() {
    PasswordResetScreen(
        state = PasswordResetState(),
        onAction = {}
    )
}