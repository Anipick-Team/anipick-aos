package jpark.bro.email

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jpark.bro.ui.APSimpleBackTopAppBar
import jpark.bro.ui.APSurfaceTextField
import jpark.bro.ui.APSurfaceTextFieldWithTrailing
import jpark.bro.ui.theme.APColors

@Composable
internal fun EmailSignup(
    onNavigateBack: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EmailSignupViewModel = hiltViewModel(),
) {
    val emailText by viewModel.emailText.collectAsState()
    val emailErrorText by viewModel.emailErrorText.collectAsState()
    val passwordText by viewModel.passwordText.collectAsState()
    val isVisibility by viewModel.isVisibility.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val isAgeVerified by viewModel.isAgeVerified.collectAsState()
    val isTermsOfServiceAccepted by viewModel.isTermsOfServiceAccepted.collectAsState()
    val isPrivacyPolicyAccepted by viewModel.isPrivacyPolicyAccepted.collectAsState()
    val isAllAgreed by viewModel.isAllAgreed.collectAsState()

    EmailSignup(
        modifier = modifier,
        emailText = emailText,
        emailErrorText = emailErrorText,
        passwordText = passwordText,
        isVisibility = isVisibility,
        isPasswordValid = isPasswordValid,
        isAgeVerified = isAgeVerified,
        isTermsOfServiceAccepted = isTermsOfServiceAccepted,
        isPrivacyPolicyAccepted = isPrivacyPolicyAccepted,
        isAllAgreed = isAllAgreed,
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
    emailErrorText: String? = null,
    passwordText: String = "",
    isVisibility: Boolean = false,
    isPasswordValid: Boolean = false,
    isAgeVerified: Boolean = false,
    isTermsOfServiceAccepted: Boolean = false,
    isPrivacyPolicyAccepted: Boolean = false,
    isAllAgreed: Boolean = false,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onVisibilityClick: () -> Unit = {},
    onAgreementClick: (AgreementType, Boolean) -> Unit = { _, _ -> },
    isSignupEnabled: () -> Boolean = { false },
    onNavigateBack: () -> Unit = {},
    onSignupClick: () -> Boolean = { false },
    onNavigateToPreferenceSetup: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

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
                    text = "이메일 회원가입",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "회원 서비스 이용을 위해 회원 가입을 진행해주세요.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = APColors.Black,
                )
                Spacer(modifier = Modifier.height(48.dp))
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
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 12.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(40.dp))
                }
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "8~16자의 영문 대/소문자, 숫자, 특수문자를 조합하여 입력해주세요.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray
                )
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "애니픽 이용을 위해\n동의가 필요해요.",
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
                            .padding(vertical = 17.dp)
                            .height(52.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                tint = if (isAllAgreed) APColors.Primary else APColors.TextGray,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
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
                            text = "[필수] 이용약관에 동의합니다..",
                            isChecked = isTermsOfServiceAccepted,
                            onCheckedChange = { onAgreementClick(AgreementType.TERMS_OF_SERVICE, !isTermsOfServiceAccepted) }
                        )
                        AgreementOption(
                            text = "[필수] 개인정보 처리방침에 동의합니다..",
                            isChecked = isPrivacyPolicyAccepted,
                            onCheckedChange = { onAgreementClick(AgreementType.PRIVACY_POLICY, !isPrivacyPolicyAccepted) }
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 32.dp),
                    color = APColors.Gray
                )
                Button(
                    onClick = {
                        if (onSignupClick()) {
                            onNavigateToPreferenceSetup()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = isSignupEnabled(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp)
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .clickable { onCheckedChange() }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = if (isChecked) APColors.Primary else APColors.TextGray,
                modifier = Modifier
                    .size(20.dp)
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