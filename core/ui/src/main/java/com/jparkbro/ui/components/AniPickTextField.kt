package com.jparkbro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick18ExtraBold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.CheckIcon
import com.jparkbro.ui.theme.EyeClosedIcon

@Composable
fun APBaseTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AniPick16Normal.copy(color = AniPickBlack),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    placeholder: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
) {
    if (isPassword) {
        BasicSecureTextField(
            state = state,
            modifier = modifier
                .height(52.dp)
                .clip(AniPickSmallShape),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = KeyboardActionHandler {
                onKeyboardAction()
            },
            cursorBrush = SolidColor(AniPickPrimary),
            decorator = { innerBox ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(AniPickGray50)
                        .padding(
                            start = dimensionResource(R.dimen.padding_default),
                            end = if (trailingIcon != null) {
                                dimensionResource(R.dimen.padding_extra_small)
                            } else {
                                dimensionResource(R.dimen.padding_default)
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = AniPick16Normal.copy(color = AniPickGray400),
                                maxLines = 1
                            )
                        }
                        innerBox()
                    }
                    trailingIcon?.let {
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                        trailingIcon()
                    }
                }
            },
            textObfuscationMode = if (showPassword) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.Hidden
            },
            textObfuscationCharacter = '*',
        )
    } else {
        BasicTextField(
            state = state,
            modifier = modifier
                .height(52.dp)
                .clip(AniPickSmallShape),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = KeyboardActionHandler {
                onKeyboardAction()
            },
            cursorBrush = SolidColor(AniPickPrimary),
            lineLimits = lineLimits,
            decorator = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AniPickGray50)
                        .padding(
                            start = dimensionResource(R.dimen.padding_default),
                            end = if (trailingIcon != null) {
                                dimensionResource(R.dimen.padding_extra_small)
                            } else {
                                dimensionResource(R.dimen.padding_default)
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = AniPick16Normal.copy(color = AniPickGray400),
                                maxLines = 1
                            )
                        }
                        innerBox()
                    }
                    trailingIcon?.let {
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                        trailingIcon()
                    }
                }
            },
        )
    }
}

/**
 * Label과 TextField를 Column으로 구성한 기본 TextField
 * 구조: Column { Label, TextField }
 */
@Composable
fun APLabeledTextField(
    label: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AniPick16Normal.copy(color = AniPickBlack),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    placeholder: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Text(
            text = label,
            style = AniPick18ExtraBold.copy(color = AniPickBlack)
        )
        APBaseTextField(
            state = state,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = lineLimits,
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            isPassword = isPassword,
            showPassword = showPassword,
        )
    }
}

/**
 * Label 라인 끝에 추가 컴포넌트가 있는 TextField
 * 구조: Column { Row { Label, Spacer, LabelEndComponent }, TextField }
 * 사용 예: "필수" 뱃지, "?" 도움말 아이콘
 */
@Composable
fun APLabeledTextFieldWithLabelTrailingComponent(
    label: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AniPick16Normal.copy(color = AniPickBlack),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    placeholder: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    labelTrailingComponent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = AniPick18ExtraBold.copy(color = AniPickBlack)
            )
            labelTrailingComponent()
        }
        APBaseTextField(
            state = state,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = lineLimits,
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            isPassword = isPassword,
            showPassword = showPassword,
        )
    }
}

/**
 * TextField 오른쪽에 별도 컴포넌트가 있는 TextField (trailingIcon과는 별개)
 * 구조: Column { Label, Row { TextField, SideComponent } }
 * 사용 예: "중복확인" 버튼, "인증" 버튼
 */
@Composable
fun APLabeledTextFieldWithSideComponent(
    label: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AniPick16Normal.copy(color = AniPickBlack),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    placeholder: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    sideComponent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Text(
            text = label,
            style = AniPick18ExtraBold.copy(color = AniPickBlack)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            APBaseTextField(
                state = state,
                modifier = modifier.weight(1f),
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                onKeyboardAction = onKeyboardAction,
                lineLimits = lineLimits,
                placeholder = placeholder,
                trailingIcon = trailingIcon,
                isPassword = isPassword,
                showPassword = showPassword,
            )
            sideComponent()
        }
    }
}

/**
 * Label 라인 끝과 TextField 오른쪽 모두에 컴포넌트가 있는 TextField
 * 구조: Column { Row { Label, Spacer, LabelEndComponent }, Row { TextField, SideComponent } }
 * 사용 예: Label 끝에 "필수" 뱃지 + TextField 옆에 "중복확인" 버튼
 */
@Composable
fun APLabeledTextFieldWithBothComponents(
    label: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AniPick16Normal.copy(color = AniPickBlack),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    placeholder: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    labelTrailingComponent: @Composable () -> Unit,
    sideComponent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = AniPick18ExtraBold.copy(color = AniPickBlack)
            )
            labelTrailingComponent()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            APBaseTextField(
                state = state,
                modifier = modifier.weight(1f),
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                onKeyboardAction = onKeyboardAction,
                lineLimits = lineLimits,
                placeholder = placeholder,
                trailingIcon = trailingIcon,
                isPassword = isPassword,
                showPassword = showPassword,
            )
            sideComponent()
        }
    }
}

@DevicePreviews
@Composable
private fun APBaseTextFieldPreview() {
    APBaseTextField(
        state = TextFieldState(),
        placeholder = "검색어를 입력해주세요.",
        trailingIcon = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {  }
                        .padding(4.dp)
                        .size(16.dp),
                    tint = APColors.TextGray
                )
                Icon(
                    painter = painterResource(R.drawable.ic_search_outline),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                        }
                        .padding(4.dp)
                        .size(16.dp),
                    tint = APColors.TextGray
                )
            }
        }
    )
}

@DevicePreviews
@Composable
private fun APLabeledTextFieldPreview() {
    APLabeledTextField(
        label = "비밀번호",
        state = TextFieldState(),
        placeholder = "비밀번호를 입력해주세요.",
        trailingIcon = {
            Icon(
                imageVector = EyeClosedIcon,
                contentDescription = null,
                modifier = Modifier,
                tint = APColors.TextGray
            )
        }
    )
}

@DevicePreviews
@Composable
private fun APLabeledTextFieldWithLabelTrailingComponentPreview() {
    APLabeledTextFieldWithLabelTrailingComponent(
        label = "이메일",
        state = TextFieldState(),
        placeholder = "이메일을 입력해주세요.",
        labelTrailingComponent = {
            Icon(
                imageVector = CheckIcon,
                contentDescription = null,
                modifier = Modifier,
                tint = APColors.TextGray
            )
        },
    )
}

@DevicePreviews
@Composable
private fun APLabeledTextFieldWithSideComponentPreview() {
    APLabeledTextFieldWithSideComponent(
        label = "이메일",
        state = TextFieldState(),
        placeholder = "이메일을 입력해주세요.",
        sideComponent = {
            Button(
                onClick = {}
            ) {
                Text("버튼")
            }
        },
    )
}