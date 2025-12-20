package com.jparkbro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jparkbro.model.enum.DialogType
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.AniPickPoint
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
                        style = AniPick14Normal.copy(color = AniPickGray500),
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
                            style = AniPick16Normal.copy(color = AniPickGray400)
                        )
                    }
                }
                VerticalDivider(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { dialogData.onConfirm(null) },
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
                        style = AniPick14Normal.copy(color = AniPickGray500),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Box(
                modifier = Modifier
                    .clickable { dialogData.onConfirm(null) },
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

@Composable
fun APReportReasonDialog(
    dialogData: DialogData
) {
    var selectedReason by rememberSaveable { mutableStateOf<String?>(null) }

    APConfirmDialog(
        dialogData = dialogData.copy(
            content = {
                ReportReasonContent(
                    onReasonSelected = { selected ->
                        selectedReason = selected
                    }
                )
            },
            onConfirm = {
                dialogData.onConfirm(selectedReason)
            }
        )
    )
}

@Composable
fun ReportReasonContent(
    onReasonSelected: (String) -> Unit,
) {
    val radioOptions = listOf(
        "스포일러",
        "편파적인 언행",
        "욕설 및 비하",
        "홍보성 및 영리 목적",
        "음란성 및 선정성"
    )
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }

    LaunchedEffect(selectedOption) {
        onReasonSelected(selectedOption)
    }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_default))
            .selectableGroup(),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .weight(1f)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { selectedOption = text },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { selectedOption = text },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AniPickPoint,
                        unselectedColor = AniPickGray100
                    )
                )
                Text(
                    text = text,
                    style = AniPick14Normal.copy(color = AniPickBlack),
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_extra_small))
                )
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