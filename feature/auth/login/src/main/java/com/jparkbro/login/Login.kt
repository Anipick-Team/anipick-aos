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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.ui.APDialog
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.R

@Composable
internal fun Login(
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()

    LaunchedEffect(uiState) {
        val currentState = uiState
        when (currentState) {
            is LoginUiState.Success -> {
                if (currentState.reviewCompletedYn) {
                    onNavigateToHome()
                } else {
                    onNavigateToPreferenceSetup()
                }
                viewModel.resetUiState()
            }
            else -> Unit
        }
    }

    Login(
        modifier = modifier,
        uiState = uiState,
        onNavigateToEmailLogin = onNavigateToEmailLogin,
        onNavigateToEmailSignup = onNavigateToEmailSignup,
        signInWithKakao = viewModel::signInWithKakao,
        signInWithGoogle = viewModel::signInWithGoogle,
    )

    if (showDialog) {
        APDialog(
            title = "이미 가입된 이메일 주소입니다.",
            subTitle = "이메일 로그인을 시도해주세요.",
            dismiss = "닫기",
            confirm = "이메일 로그인",
            onDismiss = { viewModel.dismissDialog() },
            onConfirm = {
                viewModel.dismissDialog()
                onNavigateToEmailLogin()
            },
        )
    }
}

@Composable
private fun Login(
    modifier: Modifier = Modifier,
    uiState: LoginUiState = LoginUiState.Idle,
    onNavigateToEmailLogin: () -> Unit = {},
    onNavigateToEmailSignup: () -> Unit = {},
    signInWithKakao: (Activity) -> Unit = {},
    signInWithGoogle: (Activity) -> Unit = {},
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(128.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(R.drawable.anipick_logo),
                    contentDescription = "",
                    modifier = Modifier
                        .height(28.dp)
                )
                Spacer(modifier = Modifier.height(47.dp))
                Text(
                    text = "나에게 딱 맞는 애니 추천을 위해.",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700,
                    color = APColors.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "사용할수록 더 좋아지는 애니메이션 환경을 만나보세요",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = APColors.Black
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(R.drawable.kakao_login),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if (context is Activity) {
                                signInWithKakao(context)
                            }
                        }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painterResource(R.drawable.google_login),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if (context is Activity) {
                                signInWithGoogle(context)
                            }
                        }
                )
                Spacer(modifier = Modifier.height(27.dp))
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
                            text = "이메일 회원가입",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray,
                            modifier = Modifier
                                .clickable { onNavigateToEmailSignup() },
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp)
                            .padding(horizontal = 24.dp),
                        color = APColors.TextGray
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "이메일 로그인",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray,
                            modifier = Modifier
                                .clickable { onNavigateToEmailLogin() }
                        )
                    }
                }
            }
            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "https://forms.gle/SJ7mbQfyfoe2HDLd7".toUri())
                    context.startActivity(intent)
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "로그인에 문제가 있으신가요?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray,
                    modifier = Modifier
                        .border(1.dp, APColors.TextGray, CircleShape)
                        .padding(horizontal = 14.dp, vertical = 6.dp)

                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    Login(
        onNavigateToEmailSignup = {},
        onNavigateToEmailLogin = {},
        signInWithKakao = {},
        signInWithGoogle = {},
    )
}