package com.jparkbro.mypage.impl.setting.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jparkbro.core.designsystem.component.AniPickDialogFrame
import com.jparkbro.core.designsystem.icon.Check
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.setting.ThemeMode

/** 설정 화면의 화면 모드 표시 문구 */
internal val ThemeMode.label: String
    get() = when (this) {
        ThemeMode.SYSTEM -> "시스템 설정"
        ThemeMode.LIGHT -> "라이트"
        ThemeMode.DARK -> "다크"
    }

/** 화면 모드 선택 다이얼로그. 고른 값은 "확인"을 눌러야 [onConfirm]으로 넘어간다. */
@Composable
internal fun ThemeModeDialog(
    current: ThemeMode,
    onConfirm: (ThemeMode) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        ThemeModeDialogContent(current = current, onConfirm = onConfirm, onDismiss = onDismiss)
    }
}

/** [Dialog]는 Preview에 렌더링되지 않아 실제 콘텐츠를 분리한 것 - Preview에서는 이 함수를 직접 호출한다. */
@Composable
private fun ThemeModeDialogContent(
    current: ThemeMode,
    onConfirm: (ThemeMode) -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf(current) }

    AniPickDialogFrame(
        title = "화면 모드",
        confirmText = "확인",
        onConfirm = { onConfirm(selected) },
        dismissText = "취소",
        onDismiss = onDismiss,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ThemeMode.entries.forEach { mode ->
                ThemeModeOptionRow(
                    mode = mode,
                    isSelected = mode == selected,
                    onClick = { selected = mode },
                )
            }
        }
    }
}

@Composable
private fun ThemeModeOptionRow(
    mode: ThemeMode,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = mode.label,
            style = AniPickTheme.typography.body1,
            color = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.black,
        )
        if (isSelected) {
            Icon(
                imageVector = Check,
                contentDescription = "선택됨",
                modifier = Modifier.size(18.dp),
                tint = AniPickTheme.colors.primary,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ThemeModeDialogContentPreview() {
    ThemeModeDialogContent(
        current = ThemeMode.SYSTEM,
        onConfirm = {},
        onDismiss = {},
    )
}
