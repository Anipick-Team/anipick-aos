package jpark.bro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import jpark.bro.ui.theme.APColors

@Composable
fun APDialog(
    title: String = "",
    subTitle: String = "",
    content: @Composable (() -> Unit)? = null,
    dismiss: String = "",
    confirm: String = "",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, APColors.Gray, RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(vertical = 24.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black
                )
                Text(
                    text = subTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFF667080)
                )
            }
            // Content
            if (content != null) content()
            // Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = dismiss,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.TextGray
                    )
                }
                VerticalDivider(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { onConfirm() },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = confirm,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun APDialogPreview() {
    APDialog(
        title = "SNS로 간편 가입된 계정입니다.",
        subTitle = "SNS로 로그인해주세요.",
        content = { Text("sadf") },
        dismiss = "닫기",
        confirm = "SNS로그인",
        onDismiss = {},
        onConfirm = {},
    )
}