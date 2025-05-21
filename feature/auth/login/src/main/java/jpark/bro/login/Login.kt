package jpark.bro.login

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jpark.bro.ui.theme.APColors

/**
 * Login Screen
 */
@Composable
internal fun Login(
    onNavigateToHome: () -> Unit,
    onNavigateToPreferenceSetup: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
    onNavigateToEmailSignup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    Login(
        modifier = modifier,
        onNavigateToHome = onNavigateToHome,
        onNavigateToPreferenceSetup = onNavigateToPreferenceSetup,
        onNavigateToEmailLogin = onNavigateToEmailLogin,
        onNavigateToEmailSignup = onNavigateToEmailSignup,
        signInWithKakao = viewModel::signInWithKakao,
        signInWithGoogle = viewModel::signInWithGoogle,
    )
}

@Composable
internal fun Login(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit = {},
    onNavigateToPreferenceSetup: () -> Unit = {},
    onNavigateToEmailLogin: () -> Unit = {},
    onNavigateToEmailSignup: () -> Unit = {},
    signInWithKakao: (Activity) -> Unit = {},
    signInWithGoogle: (Activity) -> Unit = {},
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
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
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "나에게 딱 맞는 애니 추천을 위해.",
                fontSize = 24.sp,
                fontWeight = FontWeight.W700,
                color = APColors.Black
            )
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
                    .clickable {
                        if (context is Activity) {
                            signInWithKakao(context)
                        }
                        // TODO 조건
                        onNavigateToHome()
//                        onNavigateToPreferenceSetup()
                    }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painterResource(R.drawable.google_login),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        if (context is Activity) {
                            signInWithGoogle(context)
                        }
                        // TODO 조건
                        onNavigateToHome()
//                        onNavigateToPreferenceSetup()
                    }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "이메일 회원가입",
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
                    text = "이메일 로그인",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray,
                    modifier = Modifier
                        .clickable { onNavigateToEmailLogin() }

                )
            }
        }
        TextButton(
            onClick = {}
        ) {
            Text(
                text = "로그인에 문제가 있으신가요?",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = APColors.TextGray,
                modifier = Modifier
                    .border(1.dp, APColors.TextGray, RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 6.dp)

            )
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    Login(
        onNavigateToEmailSignup = {},
        onNavigateToEmailLogin = {},
        onNavigateToHome = {},
        onNavigateToPreferenceSetup = {},
        signInWithKakao = {},
        signInWithGoogle = {},
    )
}