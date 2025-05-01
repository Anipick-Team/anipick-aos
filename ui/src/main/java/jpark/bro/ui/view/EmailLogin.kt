package jpark.bro.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import jpark.bro.ui.R
import jpark.bro.ui.theme.APColors
import jpark.bro.ui.component.APSimpleBackTopAppBar
import jpark.bro.ui.component.APSurfaceTextField
import jpark.bro.ui.component.APSurfaceTextFieldWithTrailing
import jpark.bro.ui.viewmodel.EmailLoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailLogin(
    viewModel: EmailLoginViewModel = hiltViewModel(),
    onNavigateToEmailSignUp: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
    handleBackNavigation: () -> Unit,
) {
    val emailText by viewModel.emailText.collectAsState()
    val passwordText by viewModel.passwordText.collectAsState()
    val isVisibility by viewModel.isVisibility.collectAsState()

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
                    text = "이메일 로그인",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "회원 서비스 이용을 위해 로그인 해주세요.",
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
                    onValueChange = { viewModel.updatePassword(it) },
                    placeholder = "비밀번호를 입력해주세요",
                    isVisibility = isVisibility,
                    trailingComponent = {
                        Image(
                            painter = if (!isVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable { viewModel.togglePasswordVisibility() }
                        )
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
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
                            .clickable { onNavigateToEmailSignUp() }
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
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 40.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(bottom = 32.dp),
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
                        text = "로그인",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EmailLoginPreview() {
    EmailLogin(
        onNavigateToEmailSignUp = {},
        onNavigateToFindPassword = {},
        handleBackNavigation = {}
    )
}