package jpark.bro.findpassword

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import jpark.bro.ui.APDialog
import jpark.bro.ui.APSimpleBackTopAppBar
import jpark.bro.ui.APSnackBar
import jpark.bro.ui.APSurfaceTextField
import jpark.bro.ui.APSurfaceTextFieldWithTrailing
import jpark.bro.ui.theme.APColors
import kotlinx.coroutines.launch

@Composable
internal fun PasswordVerification(
    onNavigateBack: () -> Unit,
    onNavigateToPasswordReset: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PasswordVerificationViewModel = hiltViewModel(),
) {
    val emailText by viewModel.emailText.collectAsState()
    val emailErrorText by viewModel.emailErrorText.collectAsState()
    val verificationCodeText by viewModel.verificationCodeText.collectAsState()
    val requestCodeButtonState by viewModel.requestCodeButtonState.collectAsState()
    val verifyButtonState by viewModel.verifyButtonState.collectAsState()

    PasswordVerification(
        modifier = modifier,
        emailText = emailText,
        emailErrorText = emailErrorText,
        requestCodeButtonState = requestCodeButtonState,
        verifyButtonState = verifyButtonState,
        verificationCodeText = verificationCodeText,
        onEmailChange = viewModel::updateEmail,
        onVerificationCodeChange = viewModel::updateVerificationCode,
        onRequestCodeClick = viewModel::requestVerificationCode,
        onNavigateBack = onNavigateBack,
        onNavigateToPasswordReset = onNavigateToPasswordReset,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasswordVerification(
    modifier: Modifier = Modifier,
    emailText: String = "",
    emailErrorText: String? = null,
    verificationCodeText: String = "",
    requestCodeButtonState: RequestCodeButtonUiState = RequestCodeButtonUiState.Initial,
    verifyButtonState: VerifyButtonUiState = VerifyButtonUiState.Active,
    onEmailChange: (String) -> Unit = {},
    onVerificationCodeChange: (String) -> Unit = {},
    onRequestCodeClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToPasswordReset: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    // viewmodel 로 수정
    var showDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            APSimpleBackTopAppBar { onNavigateBack() }
        },
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color.White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "비밀번호 찾기",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "회원 서비스 사용을 위해 비밀번호를 찾아주세요.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = APColors.Black,
                )
                Spacer(modifier = Modifier.height(48.dp))
                APSurfaceTextFieldWithTrailing(
                    label = "이메일",
                    value = emailText,
                    onValueChange = { onEmailChange(it) },
                    isVisibility = true,
                    placeholder = "이메일을 입력해주세요",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    trailingComponent = {
                        Button(
                            onClick = {
                                onRequestCodeClick()
                                focusManager.clearFocus() // TODO 통과시
                                showSnackbar = true // TODO 제약조건 통과시로 수정
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = APColors.Primary,
                                contentColor = Color.White,
                                disabledContainerColor = Color.White,
                                disabledContentColor = Color(0xFF8B8B8B),

                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = when (requestCodeButtonState) {
                                is RequestCodeButtonUiState.Initial -> true
                                is RequestCodeButtonUiState.Counting -> false
                                is RequestCodeButtonUiState.Ready -> true
                            },
                            border = when (requestCodeButtonState) {
                                is RequestCodeButtonUiState.Counting -> BorderStroke(1.dp, Color(0xFFEBEBEB))
                                else -> BorderStroke(0.dp, Color.White)
                            },
                            modifier = Modifier
                                .height(52.dp)
                        ) {
                            Text(
                                text = when (requestCodeButtonState) {
                                    is RequestCodeButtonUiState.Initial -> "인증번호 받기"
                                    is RequestCodeButtonUiState.Counting -> "전송됨 ${requestCodeButtonState.formatTime()}"
                                    is RequestCodeButtonUiState.Ready -> "재발송하기"
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                            )
                        }
                    }
                )
                if (emailErrorText != null) {
                    Text(
                        text = emailErrorText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Point,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 12.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(40.dp))
                }
                APSurfaceTextField(
                    label = "인증번호",
                    value = verificationCodeText,
                    onValueChange = { onVerificationCodeChange(it) },
                    placeholder = "인증번호를 입력해주세요",
                    enabled = when (verifyButtonState) {
                        is VerifyButtonUiState.Active -> false
                        is VerifyButtonUiState.Counting -> true
                        is VerifyButtonUiState.Success -> false
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    trailingButton = {
                        if (verifyButtonState is VerifyButtonUiState.Counting) {
                            Text(
                                text = verifyButtonState.formatTime(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                color = APColors.Point,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        } else {
                            null
                        }
                    }
                )
                // TODO 인증번호 errorText 부분
//                if (emailErrorText != null) {
//                    Text(
//                        text = emailErrorText,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.W500,
//                        color = APColors.Point,
//                        modifier = Modifier
//                            .padding(top = 8.dp)
//                    )
//                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 40.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 32.dp),
                    color = APColors.Gray
                )
                Button(
                    onClick = {
                        // TODO 성공 하면 다음 화면
                        onNavigateToPasswordReset()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    enabled = !verificationCodeText.isEmpty() && verifyButtonState is VerifyButtonUiState.Counting,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "다음",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }
    if (showDialog) APDialog(
        title = "SNS로 간편 가입된 계정입니다.",
        subTitle = "SNS로 로그인해주세요.",
        dismiss = "닫기",
        confirm = "SNS로그인",
        onDismiss = { showDialog = false },
        onConfirm = { showDialog = false },
    )
    APSnackBar(
        snackbarData = "전송이 완료되었어요.",
        visible = showSnackbar,
        onDismiss = { showSnackbar = false }
    )
}

@Preview(showBackground = true)
@Composable
private fun FindPasswordPreview() {
    PasswordVerification(
        onEmailChange = {},
        onNavigateBack = {},
        onNavigateToPasswordReset = {},
        onVerificationCodeChange = {},
    )
}