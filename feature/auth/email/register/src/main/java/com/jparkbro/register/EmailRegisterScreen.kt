package com.jparkbro.register

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.register.components.ConsentItemRow
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APBackStackTopAppBar
import com.jparkbro.ui.components.APLabeledTextField
import com.jparkbro.ui.components.APLabeledTextFieldWithLabelTrailingComponent
import com.jparkbro.ui.components.APPrimaryActionButton
import com.jparkbro.ui.components.APSnackBarRe
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick14Regular
import com.jparkbro.ui.theme.AniPick18ExtraBold
import com.jparkbro.ui.theme.AniPick24Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.CheckIcon
import com.jparkbro.ui.theme.EyeClosedIcon
import com.jparkbro.ui.theme.EyeOpenedIcon
import com.jparkbro.ui.util.ObserveAsEvents

@Composable
internal fun EmailRegisterRoot(
    onNavigateBack: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    viewModel: EmailRegisterViewModel = hiltViewModel()
) {
    var snackBarData by rememberSaveable { mutableStateOf<List<SnackBarData>>(emptyList()) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EmailRegisterEvent.RegisterSuccess -> {
                onNavigateToPreferenceSetup()
            }
            is EmailRegisterEvent.RegisterFailWithSnackBar -> {
                snackBarData = snackBarData + event.snackBarData.copy(
                    onDismiss = {
                        snackBarData = snackBarData.drop(1)
                    }
                )
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    EmailRegisterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmailRegisterAction.NavigateBack -> onNavigateBack()
                EmailRegisterAction.OnPrivacyPolicyLinkClicked -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/privacy.html".toUri())
                    context.startActivity(intent)
                }
                EmailRegisterAction.OnTermsOfServiceLinkClicked -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/terms.html".toUri())
                    context.startActivity(intent)
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

    snackBarData.firstOrNull()?.let { snackBarData ->
        APSnackBarRe(snackBarData)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailRegisterScreen(
    state: EmailRegisterState,
    onAction: (EmailRegisterAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { APBackStackTopAppBar(onNavigateBack = { onAction(EmailRegisterAction.NavigateBack) }) },
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
                ConsentSection(
                    state = state,
                    onAction = onAction,
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
            text = stringResource(R.string.email_register_title),
            style = AniPick24Bold.copy(color = AniPickBlack),
        )
        Text(
            text = stringResource(R.string.email_register_subtitle),
            style = AniPick14Regular.copy(color = AniPickBlack),
        )
    }
}

@Composable
private fun EmailPasswordInputSection(
    state: EmailRegisterState,
    onAction: (EmailRegisterAction) -> Unit,
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
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            APLabeledTextFieldWithLabelTrailingComponent(
                label = stringResource(R.string.email_login_password),
                state = state.password,
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
                        imageVector = if (state.isPasswordVisible) EyeOpenedIcon else EyeClosedIcon,
                        contentDescription = stringResource(R.string.visible_icon),
                        tint = AniPickGray500,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onAction(EmailRegisterAction.OnTogglePasswordVisibility) }
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
                text = stringResource(R.string.email_register_password_info),
                style = AniPick12Normal.copy(color = AniPickGray400)
            )
        }
    }
}

@Composable
private fun ConsentSection(
    state: EmailRegisterState,
    onAction: (EmailRegisterAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
    ) {
        Text(
            text = stringResource(R.string.email_register_consent_title),
            style = AniPick18ExtraBold.copy(color = AniPickBlack),
        )
        TextButton(
            onClick = { onAction(EmailRegisterAction.OnAllAgreeClicked) },
            shape = AniPickSmallShape,
            colors = ButtonDefaults.buttonColors(containerColor = AniPickGray50),
            contentPadding = PaddingValues(all = dimensionResource(R.dimen.padding_medium))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = stringResource(R.string.check_icon),
                    tint = if (state.isAllAgreed) AniPickPrimary else AniPickGray400,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_small))
                )
                Text(
                    text = stringResource(R.string.email_register_consent_all),
                    style = AniPick14Normal.copy(color = AniPickBlack)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            ConsentItemRow(
                text = stringResource(R.string.email_register_consent_age),
                isChecked = state.isAgeVerified,
                onCheckedChange = { onAction(EmailRegisterAction.OnAgeVerificationClicked) },
            )
            ConsentItemRow(
                text = stringResource(R.string.email_register_consent_terms),
                isChecked = state.isTermsOfServiceAccepted,
                onCheckedChange = { onAction(EmailRegisterAction.OnTermsOfServiceClicked) },
                onNavigateToTerms = { onAction(EmailRegisterAction.OnTermsOfServiceLinkClicked) }
            )
            ConsentItemRow(
                text = stringResource(R.string.email_register_consent_privacy),
                isChecked = state.isPrivacyPolicyAccepted,
                onCheckedChange = { onAction(EmailRegisterAction.OnPrivacyPolicyClicked) },
                onNavigateToTerms = { onAction(EmailRegisterAction.OnPrivacyPolicyLinkClicked) }
            )
        }
    }
}

@Composable
private fun Footer(
    state: EmailRegisterState,
    onAction: (EmailRegisterAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        HorizontalDivider(color = AniPickGray100)
        APPrimaryActionButton(
            text = stringResource(R.string.email_register_register_btn),
            onClick = { onAction(EmailRegisterAction.OnRegisterClicked) },
            enabled = state.isRegisterEnabled,
            isLoading = state.isRegisterIng,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
        )
    }
}

@DevicePreviews
@Composable
private fun EmailRegisterScreenPreview() {
    EmailRegisterScreen(
        state = EmailRegisterState(),
        onAction = {}
    )
}