package com.jparkbro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jparkbro.ui.theme.APColors

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
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
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
                    color = Color(0xFF667080),
                    textAlign = TextAlign.Center
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
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dismiss,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.TextGray,
                    )
                }
                VerticalDivider(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onConfirm() },
                    contentAlignment = Alignment.Center
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

@Composable
fun APErrorDialog(
    errorMsg: String = "",
    dismiss: String = "닫기",
    onDismiss: () -> Unit,
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "오류",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black
                )
                Text(
                    text = errorMsg,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFF667080),
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dismiss,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray,
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onDismiss()
                        }
                )
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

@Preview(showBackground = true)
@Composable
private fun APErrorDialogPreview() {
    APErrorDialog(
        dismiss = "확인",
        errorMsg = "에러입니다. 에러입니다. 에러입니다. 에러입니다. 에러입니다. 에러입니다.",
        onDismiss = {},
    )
}

data class DialogData(
    val title: String = "",
    val subTitle: String = "",
    val content: @Composable () -> Unit = {},
    val dismiss: String = "",
    val confirm: String = "",
    val onDismiss: () -> Unit = {},
    val onConfirm: () -> Unit = {},
    val errorMsg: String = "",
)