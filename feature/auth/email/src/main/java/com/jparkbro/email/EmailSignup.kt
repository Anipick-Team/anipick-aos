package com.jparkbro.email

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.ui.APSimpleBackTopAppBar
import com.jparkbro.ui.APSurfaceTextField
import com.jparkbro.ui.APSurfaceTextFieldWithTrailing
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.R

@Composable
internal fun EmailSignup(
    onNavigateBack: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EmailSignupViewModel = hiltViewModel(),
) {
    val emailText by viewModel.emailText.collectAsState()
    val passwordText by viewModel.passwordText.collectAsState()
    val isVisibility by viewModel.isVisibility.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val isAgeVerified by viewModel.isAgeVerified.collectAsState()
    val isTermsOfServiceAccepted by viewModel.isTermsOfServiceAccepted.collectAsState()
    val isPrivacyPolicyAccepted by viewModel.isPrivacyPolicyAccepted.collectAsState()
    val isAllAgreed by viewModel.isAllAgreed.collectAsState()
    val signupUiState by viewModel.signupUiState.collectAsState()

    EmailSignup(
        modifier = modifier,
        emailText = emailText,
        passwordText = passwordText,
        isVisibility = isVisibility,
        isPasswordValid = isPasswordValid,
        isAgeVerified = isAgeVerified,
        isTermsOfServiceAccepted = isTermsOfServiceAccepted,
        isPrivacyPolicyAccepted = isPrivacyPolicyAccepted,
        isAllAgreed = isAllAgreed,
        signupUiState = signupUiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onVisibilityClick = viewModel::togglePasswordVisibility,
        onAgreementClick = viewModel::updateAgreementStatus,
        isSignupEnabled = viewModel::canProceedSignup,
        onNavigateBack = onNavigateBack,
        onSignupClick = viewModel::signup,
        onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailSignup(
    modifier: Modifier = Modifier,
    emailText: String = "",
    passwordText: String = "",
    isVisibility: Boolean = false,
    isPasswordValid: Boolean = false,
    isAgeVerified: Boolean = false,
    isTermsOfServiceAccepted: Boolean = false,
    isPrivacyPolicyAccepted: Boolean = false,
    isAllAgreed: Boolean = false,
    signupUiState: SignupUiState = SignupUiState.Idle,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onVisibilityClick: () -> Unit = {},
    onAgreementClick: (AgreementType, Boolean) -> Unit = { _, _ -> },
    isSignupEnabled: () -> Boolean = { false },
    onNavigateBack: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    onNavigateToPreferenceSetup: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    LaunchedEffect(signupUiState) {
        if (signupUiState is SignupUiState.Success) {
            onNavigateToPreferenceSetup()
        }
    }
    val emailErrorText = when (signupUiState) {
        is SignupUiState.Error -> signupUiState.message
        else -> null
    }

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
                .padding(innerPadding)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(64.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "이메일 회원가입",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600,
                        color = APColors.Black,
                    )
                    Text(
                        text = "회원 서비스 이용을 위해 회원 가입을 진행해주세요.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        color = APColors.Black,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(37.dp)
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
                        if (emailErrorText != null) {
                            Text(
                                text = emailErrorText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Point,
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        APSurfaceTextFieldWithTrailing(
                            label = "비밀번호",
                            value = passwordText,
                            onValueChange = { onPasswordChange(it) },
                            placeholder = "비밀번호를 입력해주세요",
                            isVisibility = isVisibility,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_check),
                                    contentDescription = null,
                                    tint = if (!isPasswordValid) APColors.TextGray else APColors.Primary,
                                    modifier = Modifier
                                        .size(18.dp)
                                )
                            },
                            trailingButton = {
                                Image(
                                    painter = if (!isVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable { onVisibilityClick() }
                                )
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
                        Text(
                            text = "8~16자의 영문 대/소문자, 숫자, 특수문자를 조합하여 입력해주세요.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(17.dp)
                ) {
                    Text(
                        text = "애니픽 이용을 위해 동의가 필요해요.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W700,
                        color = APColors.Black
                    )
                    Button(
                        onClick = { onAgreementClick(AgreementType.ALL, !isAllAgreed) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = APColors.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                tint = if (isAllAgreed) APColors.Primary else APColors.TextGray,
                                modifier = Modifier
                                    .size(16.dp)
                            )
                            Text(
                                text = "모두 동의합니다.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(11.dp)
                    ) {
                        AgreementOption(
                            text = "[필수] 만 14세 이상입니다.",
                            isChecked = isAgeVerified,
                            onCheckedChange = { onAgreementClick(AgreementType.AGE_VERIFICATION, !isAgeVerified) }
                        )
                        AgreementOption(
                            text = "[필수] 이용약관에 동의합니다.",
                            isChecked = isTermsOfServiceAccepted,
                            onCheckedChange = { onAgreementClick(AgreementType.TERMS_OF_SERVICE, !isTermsOfServiceAccepted) }
                        )
                        AgreementOption(
                            text = "[필수] 개인정보 처리방침에 동의합니다.",
                            isChecked = isPrivacyPolicyAccepted,
                            onCheckedChange = { onAgreementClick(AgreementType.PRIVACY_POLICY, !isPrivacyPolicyAccepted) }
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
                    onClick = { onSignupClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = isSignupEnabled(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "회원가입하기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }
}

@Composable
fun AgreementOption(
    text: String,
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable { onCheckedChange() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = if (isChecked) APColors.Primary else APColors.TextGray,
                modifier = Modifier
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = APColors.Black
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = "",
            tint = Color(0xFFC3C3CA)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailSignupPreview() {
    EmailSignup(
        onNavigateBack = {},
        onSignupClick = { false },
        onEmailChange = {},
        onPasswordChange = {},
        onVisibilityClick = {},
    )
}