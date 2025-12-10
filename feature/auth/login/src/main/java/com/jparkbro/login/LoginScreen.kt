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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jparkbro.model.enum.DialogType
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APSnackBarRe
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick24Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickLogoImg
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.GoogleLoginImg
import com.jparkbro.ui.theme.KakaoLoginImg
import com.jparkbro.ui.util.ObserveAsEvents
import com.jparkbro.ui.util.extension.requireActivity

@Composable
internal fun LoginRoot(
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
    onNavigateToEmailRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.requireActivity()

    var dialogData by rememberSaveable { mutableStateOf<DialogData?>(null) }
    var snackBarData by rememberSaveable { mutableStateOf<List<SnackBarData>>(emptyList()) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.LoginSuccess -> {
                if (event.reviewCompletedYn) {
                    onNavigateToPreferenceSetup()
                } else {
                    onNavigateToHome()
                }
            }

            is LoginEvent.LoginFailWithDialog -> {
                when (event.dialogData.type) {
                    DialogType.ALERT -> {
                        dialogData = event.dialogData.copy(
                            onConfirm = { dialogData = null }
                        )
                    }

                    DialogType.CONFIRM -> {
                        dialogData = event.dialogData.copy(
                            onDismiss = { dialogData = null },
                            onConfirm = {
                                dialogData = null
                                onNavigateToEmailLogin()
                            }
                        )
                    }
                }
            }

            is LoginEvent.LoginFailWithSnackBar -> {
                snackBarData = snackBarData + event.snackBarData.copy(
                    onDismiss = {
                        snackBarData = snackBarData.drop(1)
                    }
                )
            }
        }
    }

    LoginScreen(
        activity = activity,
        onAction = { action ->
            when (action) {
                LoginAction.OnEmailLoginClick -> onNavigateToEmailLogin()
                LoginAction.OnEmailRegisterClick -> onNavigateToEmailRegister()
                is LoginAction.OnProblemClick -> {
                    val intent = Intent(Intent.ACTION_VIEW, "https://forms.gle/SJ7mbQfyfoe2HDLd7".toUri())
                    context.startActivity(intent)
                }

                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

    dialogData?.let {
        when (it.type) {
            DialogType.ALERT -> {
                APAlertDialog(it)
            }

            DialogType.CONFIRM -> {
                APConfirmDialog(it)
            }
        }
    }

    snackBarData.firstOrNull()?.let { snackBarData ->
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
                    style = AniPick14Normal.copy(color = AniPickGray400),
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
                color = AniPickGray400
            )
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(R.string.login_email_login),
                    style = AniPick14Normal.copy(color = AniPickGray400),
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
        style = AniPick14Normal.copy(color = AniPickGray400),
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onAction(LoginAction.OnProblemClick) }
            .border(
                width = dimensionResource(R.dimen.border_width_default),
                color = AniPickGray400,
                shape = CircleShape
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
        activity = LocalContext.current.requireActivity(),
        onAction = {}
    )
}
