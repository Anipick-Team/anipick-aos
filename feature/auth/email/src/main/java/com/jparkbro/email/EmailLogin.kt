package com.jparkbro.email

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.ui.APAlertDialog
import com.jparkbro.ui.APConfirmDialog
import com.jparkbro.ui.APSimpleBackTopAppBar
import com.jparkbro.ui.APSurfaceTextField
import com.jparkbro.ui.APSurfaceTextFieldWithTrailing
import com.jparkbro.ui.DialogType
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors

@Composable
internal fun EmailLogin(
    onNavigateBack: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EmailLoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val emailText by viewModel.emailText.collectAsState()
    val passwordText by viewModel.passwordText.collectAsState()
    val isVisibility by viewModel.isVisibility.collectAsState()

    val showDialog by viewModel.showDialog.collectAsState()
    val emailErrorMessage by viewModel.emailErrorMessage.collectAsState()
    val loginFailMessage by viewModel.loginFailMessage.collectAsState()

    EmailLogin(
        modifier = modifier,
        uiState = uiState,
        emailText = emailText,
        passwordText = passwordText,
        isVisibility = isVisibility,
        emailErrorMessage = emailErrorMessage,
        loginFailMessage = loginFailMessage,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onVisibilityClick = viewModel::togglePasswordVisibility,
        onNavigateBack = onNavigateBack,
        onNavigateToEmailSignup = onNavigateToEmailSignup,
        onNavigateToFindPassword = onNavigateToFindPassword,
        onNavigateToHome = onNavigateToHome,
        onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
        onLogin = viewModel::emailValid
    )

    showDialog?.let { dialogData ->
        APAlertDialog(
            title = dialogData.title,
            errorMsg = dialogData.errorMsg,
            dismiss = dialogData.dismiss,
            onDismiss = dialogData.onDismiss
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailLogin(
    modifier: Modifier = Modifier,
    uiState: EmailLoginUiState = EmailLoginUiState.Idle,
    emailText: String,
    passwordText: String,
    isVisibility: Boolean = false,
    emailErrorMessage: String? = null,
    loginFailMessage: String? = null,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onVisibilityClick: () -> Unit = {},
    onNavigateBack: () -> Unit= {},
    onNavigateToEmailSignup: () -> Unit = {},
    onNavigateToFindPassword: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToPreferenceSetup: () -> Unit = {},
    onLogin: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            APSimpleBackTopAppBar { onNavigateBack() }
        },
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(64.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "이메일 로그인",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600,
                        color = APColors.Black,
                    )
                    Text(
                        text = "회원 서비스 이용을 위해 로그인 해주세요.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        color = APColors.Black,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        APSurfaceTextField(
                            label = "이메일",
                            value = emailText,
                            onValueChange = { onEmailChange(it) },
                            placeholder = "이메일을 입력해주세요",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                        )
                        if (emailErrorMessage != null) {
                            Text(
                                text = emailErrorMessage,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Point
                            )
                        }
                    }
                    APSurfaceTextFieldWithTrailing(
                        label = "비밀번호",
                        value = passwordText,
                        onValueChange = { onPasswordChange(it) },
                        placeholder = "비밀번호를 입력해주세요",
                        isVisibility = isVisibility,
                        trailingButton = {
                            IconButton(
                                onClick = { onVisibilityClick() }
                            ) {
                                Image(
                                    painter = if (!isVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                                    contentDescription = "",
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "회원가입",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray,
                            modifier = Modifier
                                .clickable { onNavigateToEmailSignup() }
                        )
                        VerticalDivider(
                            modifier = Modifier
                                .height(16.dp)
                                .padding(horizontal = 24.dp),
                            color = APColors.TextGray
                        )
                        Text(
                            text = "비밀번호 찾기",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray,
                            modifier = Modifier
                                .clickable { onNavigateToFindPassword() }

                        )
                    }
                    if (loginFailMessage != null) {
                        Text(
                            text = loginFailMessage,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Point,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .height(107.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(bottom = 30.dp),
                    color = APColors.Gray
                )
                Button(
                    onClick = { onLogin() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "로그인",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.White
                    )
                }
            }
        }
    }

    when (uiState) {
        is EmailLoginUiState.Idle -> { }
        is EmailLoginUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(APColors.ScrimColor),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is EmailLoginUiState.Success -> {
            if (uiState.reviewCompletedYn) {
                onNavigateToHome()
            } else {
                onNavigateToPreferenceSetup()
            }
        }
        is EmailLoginUiState.Error -> {
            // TODO 통신에러 처리
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailLoginPreview() {
    EmailLogin(
        emailText = "",
        passwordText = "",
        onNavigateBack = {},
        onNavigateToEmailSignup = {},
        onNavigateToFindPassword = {},
        onEmailChange = {},
        onPasswordChange = {},
        onVisibilityClick = {},
    )
}