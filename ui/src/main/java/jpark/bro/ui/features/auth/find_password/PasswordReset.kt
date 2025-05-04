package jpark.bro.ui.features.auth.find_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jpark.bro.ui.R
import jpark.bro.ui.component.APSimpleBackTopAppBar
import jpark.bro.ui.component.APSurfaceTextFieldWithTrailing
import jpark.bro.ui.theme.APColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordReset(
    viewModel: PasswordResetViewModel = hiltViewModel(),
    handleBackNavigation: () -> Unit,
) {
    val password by viewModel.password.collectAsState()
    val passwordConfirm by viewModel.passwordConfirm.collectAsState()
    val isPasswordVisibility by viewModel.isPasswordVisibility.collectAsState()
    val isConfirmVisibility by viewModel.isConfirmVisibility.collectAsState()

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
                    label = "새 비밀번호",
                    value = password,
                    onValueChange = { viewModel.updatePassword(it) },
                    placeholder = "비밀번호를 입력해주세요",
                    isVisibility = isPasswordVisibility,
                    trailingButton = {
                        IconButton(
                            onClick = {
                                viewModel.togglePasswordVisibility()
                            }
                        ) {
                            Image(
                                painter = if (!isPasswordVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                                contentDescription = "",
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                APSurfaceTextFieldWithTrailing(
                    label = "새 비밀번호 확인",
                    value = passwordConfirm,
                    onValueChange = { viewModel.updatePasswordConfirm(it) },
                    placeholder = "비밀번호를 입력해주세요",
                    isVisibility = isConfirmVisibility,
                    trailingButton = {
                        IconButton(
                            onClick = {
                                viewModel.toggleConfirmVisibility()
                            }
                        ) {
                            Image(
                                painter = if (!isConfirmVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                                contentDescription = "",
                            )
                        }
                    }
                )
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
                    },
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
                        text = "비밀번호 변경 완료",
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
fun PasswordResetPreview() {
    PasswordReset() {}
}