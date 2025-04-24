package jpark.bro.anipick.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmailLogin() {
    Scaffold(
        topBar = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Column {
                Text(
                    text = "이메일 로그인",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF2F2E41),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "회원 서비스 이용을 위해 로그인 해주세요.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF2F2E41),
                )
                Spacer(modifier = Modifier.height(48.dp))

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EmailLoginPreview() {
    EmailLogin()
}