package com.jparkbro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jparkbro.model.enum.DialogType
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray300
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.UiText

@Composable
fun APConfirmDialog(
    dialogData: DialogData
) {
    Dialog(
        onDismissRequest = dialogData.onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AniPickSmallShape)
                .border(dimensionResource(R.dimen.border_width_default), AniPickGray50, AniPickSmallShape)
                .background(AniPickWhite)
                .padding(vertical = dimensionResource(R.dimen.padding_extra_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_36))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_huge)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                dialogData.title?.let {
                    Text(
                        text = it.asString(),
                        style = AniPick20Bold.copy(color = AniPickBlack),
                    )
                }
                dialogData.subTitle?.let {
                    Text(
                        text = it.asString(),
                        style = AniPick14Normal.copy(color = AniPickGray400),
                        textAlign = TextAlign.Center
                    )
                }
            }
            dialogData.content?.let { it() }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_huge)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { dialogData.onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    dialogData.dismiss?.let {
                        Text(
                            text = it.asString(),
                            style = AniPick16Normal.copy(color = AniPickGray300)
                        )
                    }
                }
                VerticalDivider(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { dialogData.onConfirm() },
                    contentAlignment = Alignment.Center
                ) {
                    dialogData.confirm?.let {
                        Text(
                            text = it.asString(),
                            style = AniPick16Normal.copy(color = AniPickPrimary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun APAlertDialog(
    dialogData: DialogData
) {
    Dialog(
        onDismissRequest = dialogData.onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AniPickSmallShape)
                .border(dimensionResource(R.dimen.border_width_default), AniPickGray50, AniPickSmallShape)
                .background(AniPickWhite)
                .padding(vertical = dimensionResource(R.dimen.padding_extra_large)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_36))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_huge)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                dialogData.title?.let {
                    Text(
                        text = it.asString(),
                        style = AniPick20Bold.copy(color = AniPickBlack),
                    )
                }
                dialogData.subTitle?.let {
                    Text(
                        text = it.asString(),
                        style = AniPick14Normal.copy(color = AniPickGray400),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Box(
                modifier = Modifier
                    .clickable { dialogData.onConfirm() },
                contentAlignment = Alignment.Center
            ) {
                dialogData.confirm?.let {
                    Text(
                        text = it.asString(),
                        style = AniPick16Normal.copy(color = AniPickPrimary)
                    )
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun APConfirmDialogPreview() {
    APConfirmDialog(
        dialogData = DialogData(
            type = DialogType.CONFIRM,
            title = UiText.DynamicString("이미 가입된 이메일 주소입니다."),
            subTitle = UiText.DynamicString("이메일 로그인을 시도해주세요."),
            dismiss = UiText.DynamicString("닫기"),
            confirm = UiText.DynamicString("이메일 로그인")
        )
    )
}

@DevicePreviews
@Composable
private fun APAlertDialogPreview() {
    APAlertDialog(
        dialogData = DialogData(
            type = DialogType.ALERT,
            title = UiText.DynamicString("이미 가입된 이메일 주소입니다."),
            subTitle = UiText.DynamicString("이메일 로그인을 시도해주세요."),
            confirm = UiText.DynamicString("닫기")
        )
    )
}