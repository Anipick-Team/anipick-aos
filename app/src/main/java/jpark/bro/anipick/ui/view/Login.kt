package jpark.bro.anipick.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpark.bro.anipick.R

/**
 * Login View
 */
@Composable
fun Login() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        /**
         * Top App Information
         */
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
                    .size(width = 120.dp, height = 32.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "나에게 딱 맞는 애니 추천을 위해.",
                fontSize = 24.sp,
                fontWeight = FontWeight.W700,
                color = Color(0xFF2F2E41)
            )
            Text(
                text = "사용할수록 더 좋아지는 애니메이션 환경을 만나보세요",
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                color = Color(0xFF2F2E41)
            )
        }

        /**
         * Mid Login Section
         */
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .width(360.dp)
                    .height(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFEE500)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.kakao_symbol),
                    contentDescription = "",
                    modifier = Modifier
                        .size(28.dp)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "카카오 계정으로 계속하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF3A1E20)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .width(360.dp)
                    .height(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE3E3E3), RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFFFFF)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.google_symbol),
                    contentDescription = "",
                    modifier = Modifier
                        .size(28.dp)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "구글 계정으로 계속하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF3A1E20)
                )
            }
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
                    color = Color(0xFFB1B6C1)
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(16.dp)
                        .padding(horizontal = 24.dp),
                    color = Color(0xFFB1B6C1)
                )
                Text(
                    text = "이메일 회원가입",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFFB1B6C1)
                )
            }
        }

        /**
         * Bottom Report Button
         */
        TextButton(
            onClick = {}
        ) {
            Text(
                text = "로그인에 문제가 있으신가요?",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = Color(0xFFB1B6C1),
                modifier = Modifier
                    .border(1.dp, Color(0xFFB1B6C1), RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 6.dp)

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login()
}