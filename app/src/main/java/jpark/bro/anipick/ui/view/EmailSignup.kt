package jpark.bro.anipick.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jpark.bro.anipick.R
import jpark.bro.anipick.ui.theme.APColors
import jpark.bro.anipick.ui.view.common.APSimpleBackTopAppBar
import jpark.bro.anipick.ui.view.common.APSurfaceTextField
import jpark.bro.anipick.ui.view.common.APSurfaceTextFieldWithTrailing
import jpark.bro.anipick.ui.viewmodel.EmailSignupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailSignup(
    viewModel: EmailSignupViewModel = hiltViewModel(),
    handleBackNavigation: () -> Unit,
) {
    val emailText by viewModel.emailText.collectAsState()
    val passwordText by viewModel.passwordText.collectAsState()
    val isVisibility by viewModel.isVisibility.collectAsState()
    var isPasswordValid by remember { mutableStateOf(false) }

    var isAgeVerified by remember { mutableStateOf(false) }
    var isTermsOfServiceAccepted by remember { mutableStateOf(false) }
    var isPrivacyPolicyAccepted by remember { mutableStateOf(false) }
    var isAllAgreed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            APSimpleBackTopAppBar { handleBackNavigation() }
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = APColors.Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                    onValueChange = { viewModel.updateEmail(it) },
                    placeholder = "이메일을 입력해주세요",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                )
                Spacer(modifier = Modifier.height(32.dp))
                APSurfaceTextFieldWithTrailing(
                    label = "비밀번호",
                    value = passwordText,
                    onValueChange = {
                        viewModel.updatePassword(it)
                        // TODO 수정
                        if (passwordText.isNotEmpty()) {
                            isPasswordValid = true
                        } else {
                            isPasswordValid = false
                        }
                                    },
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
                    trailingComponent = {
                        Image(
                            painter = if (!isVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable { viewModel.togglePasswordVisibility() }
                        )
                    }
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
                        onClick = { isAllAgreed = !isAllAgreed },
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
                            onCheckedChange = { isAgeVerified = !isAgeVerified }
                        )
                        AgreementOption(
                            text = "[필수] 이용약관에 동의합니다..",
                            isChecked = isTermsOfServiceAccepted,
                            onCheckedChange = { isTermsOfServiceAccepted = !isTermsOfServiceAccepted }
                        )
                        AgreementOption(
                            text = "[필수] 개인정보 처리방침에 동의합니다..",
                            isChecked = isPrivacyPolicyAccepted,
                            onCheckedChange = { isPrivacyPolicyAccepted = !isPrivacyPolicyAccepted }
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 32.dp),
                    color = APColors.Gray
                )
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = true,
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
fun EmailSignupPreview() {
    EmailSignup(
        handleBackNavigation = {}
    )
}