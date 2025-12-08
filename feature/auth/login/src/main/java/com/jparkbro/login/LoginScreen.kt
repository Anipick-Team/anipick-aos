package com.jparkbro.login

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.enum.DialogType
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APSnackBarRe
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick24Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray300
import com.jparkbro.ui.theme.AniPickLogoImg
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.GoogleLoginImg
import com.jparkbro.ui.theme.KakaoLoginImg
import com.jparkbro.ui.util.ObserveAsEvents

@Composable
internal fun LoginRoot(
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current as Activity
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is LoginEvent.LoginSuccess -> {
                if (event.reviewCompletedYn) {
                    onNavigateToPreferenceSetup()
                } else {
                    onNavigateToHome()
                }
            }
        }
    }

    LoginScreen(
        activity = context,
        onAction = { action ->
            when (action) {
                LoginAction.OnEmailLoginClick -> onNavigateToEmailLogin()
                LoginAction.OnEmailRegisterClick -> onNavigateToEmailSignup()
                is LoginAction.OnProblemClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://forms.gle/SJ7mbQfyfoe2HDLd7".toUri())
                    context.startActivity(intent)
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

    uiState.dialogData?.let {
        when(it.type) {
            DialogType.ALERT -> {
                APAlertDialog(it)
            }
            DialogType.CONFIRM -> {
                APConfirmDialog(
                    it.copy(
                        onConfirm = {
                            viewModel.onAction(LoginAction.OnDialogDismiss)
                            onNavigateToEmailLogin()
                        }
                    )
                )
            }
        }
    }

    uiState.snackBarQueue.firstOrNull()?.let { snackBarData ->
        APSnackBarRe(snackBarData)
    }
}

@Composable
private fun LoginScreen(
    activity: Activity,
    onAction: (LoginAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.padding_large))
            .background(AniPickWhite),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(124.dp, Alignment.CenterVertically)
    ) {
        Header()
        Content(
            activity = activity,
            onAction = onAction
        )
        Footer(onAction = onAction)
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = AniPickLogoImg,
            contentDescription = stringResource(R.string.app_logo),
            modifier = Modifier
                .height(28.dp)
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_48)))
        Text(
            text = stringResource(R.string.login_title),
            style = AniPick24Bold.copy(color = AniPickBlack),
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.login_subtitle),
            style = AniPick14Normal.copy(color = AniPickBlack),
        )
    }
}

@Composable
private fun Content(
    activity: Activity,
    onAction: (LoginAction) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = KakaoLoginImg,
            contentDescription = stringResource(R.string.kakao_login_button),
            modifier = Modifier
                .clip(AniPickSmallShape)
                .clickable { onAction(LoginAction.OnKakaoLoginClick(activity)) }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
        Image(
            imageVector = GoogleLoginImg,
            contentDescription = stringResource(R.string.google_login_button),
            modifier = Modifier
                .clip(AniPickSmallShape)
                .clickable { onAction(LoginAction.OnGoogleLoginClick(activity)) }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_28)))
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
                    text = stringResource(R.string.login_email_register),
                    style = AniPick14Normal.copy(color = AniPickGray300),
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAction(LoginAction.OnEmailRegisterClick) }
                        .padding(dimensionResource(R.dimen.padding_extra_small))
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.spacing_default))
                    .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                color = AniPickGray300
            )
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(R.string.login_email_login),
                    style = AniPick14Normal.copy(color = AniPickGray300),
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAction(LoginAction.OnEmailLoginClick) }
                        .padding(dimensionResource(R.dimen.padding_extra_small))
                )
            }
        }
    }
}

@Composable
private fun Footer(
    onAction: (LoginAction) -> Unit
) {
    Text(
        text = stringResource(R.string.login_problem),
        style = AniPick14Normal.copy(color = AniPickGray300),
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onAction(LoginAction.OnProblemClick) }
            .border(
                dimensionResource(R.dimen.border_width_default),
                AniPickGray300,
                CircleShape
            )
            .padding(
                horizontal = dimensionResource(R.dimen.padding_default),
                vertical = dimensionResource(R.dimen.padding_small)
            )
    )
}

@DevicePreviews
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        activity = LocalContext.current as Activity,
        onAction = {}
    )
}
