package com.jparkbro.findpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.ui.APSimpleBackTopAppBar
import com.jparkbro.ui.APSurfaceTextFieldWithTrailing
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.R

@Composable
internal fun PasswordReset(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PasswordResetViewModel = hiltViewModel(),
) {
    val password by viewModel.password.collectAsState()
    val passwordConfirm by viewModel.passwordConfirm.collectAsState()
    val isPasswordVisibility by viewModel.isPasswordVisibility.collectAsState()
    val isConfirmVisibility by viewModel.isConfirmVisibility.collectAsState()

    PasswordReset(
        modifier = modifier,
        password = password,
        passwordConfirm = passwordConfirm,
        isPasswordVisibility = isPasswordVisibility,
        isConfirmVisibility = isConfirmVisibility,
        onPasswordChange = viewModel::updatePassword,
        onPasswordConfirmChange = viewModel::updatePasswordConfirm,
        onPasswordVisibilityClick = viewModel::togglePasswordVisibility,
        onConfirmVisibilityClick = viewModel::toggleConfirmVisibility,
        onResetPassword = viewModel::resetPassword,
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasswordReset(
    modifier: Modifier = Modifier,
    password: String = "",
    passwordConfirm: String = "",
    isPasswordVisibility: Boolean = false,
    isConfirmVisibility: Boolean = false,
    onPasswordChange: (String) -> Unit = {},
    onPasswordConfirmChange: (String) -> Unit = {},
    onPasswordVisibilityClick: () -> Unit = {},
    onConfirmVisibilityClick: () -> Unit = {},
    onResetPassword: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(64.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "비밀번호 찾기",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600,
                        color = APColors.Black,
                    )
                    Text(
                        text = "회원 서비스 사용을 위해 비밀번호를 찾아주세요.",
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
                        APSurfaceTextFieldWithTrailing(
                            label = "새 비밀번호",
                            value = password,
                            onValueChange = { onPasswordChange(it) },
                            placeholder = "새 비밀번호를 입력해주세요",
                            isVisibility = isPasswordVisibility,
                            trailingButton = {
                                IconButton(
                                    onClick = { onPasswordVisibilityClick() }
                                ) {
                                    Image(
                                        painter = if (!isPasswordVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                                        contentDescription = "",
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
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
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        APSurfaceTextFieldWithTrailing(
                            label = "새 비밀번호 확인",
                            value = passwordConfirm,
                            onValueChange = { onPasswordConfirmChange(it) },
                            placeholder = "새 비밀번호를 다시 한번 입력해주세요",
                            isVisibility = isConfirmVisibility,
                            trailingButton = {
                                IconButton(
                                    onClick = { onConfirmVisibilityClick() }
                                ) {
                                    Image(
                                        painter = if (!isConfirmVisibility) painterResource(R.drawable.ic_visibility_off) else painterResource(R.drawable.ic_visibility_on),
                                        contentDescription = "",
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                        )
                        // TODO Error Text 추가
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
                    onClick = {
                        onResetPassword()
                        // TODO page 이동
                    },
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
private fun PasswordResetPreview() {
    PasswordReset(
        onNavigateBack = {},
        onPasswordChange = {},
        onPasswordConfirmChange = {},
        onPasswordVisibilityClick = {},
        onConfirmVisibilityClick = {},
    )
}