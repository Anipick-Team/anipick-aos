package jpark.bro.anipick.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpark.bro.anipick.R
import jpark.bro.anipick.ui.theme.APColors

/**
 * Label + BaseTextField
 */
@Composable
fun APBaseTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    trailingComponent: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                color = APColors.Black
            )
            if (trailingIcon != null) trailingIcon()
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = value,
                onValueChange = { onValueChange(it) },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = APColors.LightGray,
                    unfocusedContainerColor = APColors.LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                ),
                placeholder = {
                    Text(
                        text = placeholder,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.TextGray
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            )
            if (trailingComponent != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingComponent()
            }
        }
    }
}

/**
 * BackGround Color
 */
@Composable
fun APSurfaceTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    APBaseTextField(
        label = label,
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = placeholder,
        keyboardOptions = keyboardOptions
    )
}

/**
 * BackGround Color + Trailing Component
 */
@Composable
fun APSurfaceTextFieldWithTrailing(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isVisibility: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    trailingComponent: @Composable (() -> Unit)? = null,
) {
    APBaseTextField(
        label = label,
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        trailingComponent = { if (trailingComponent != null) trailingComponent() },
        visualTransformation = if (!isVisibility) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Preview(showBackground = true)
@Composable
fun APSurfaceTextFieldPreview() {
    APSurfaceTextField(
        label = "비밀번호",
        value = "",
        onValueChange = {  },
        placeholder = "비밀번호를 입력해주세요"
    )
}
@Preview(showBackground = true)
@Composable
fun APSurfaceTextFieldWithTrailingPreview() {
    APSurfaceTextFieldWithTrailing(
        label = "비밀번호",
        value = "",
        onValueChange = {  },
        placeholder = "비밀번호를 입력해주세요",
        isVisibility = false,
        trailingComponent = {
            Image(
                painter = painterResource(R.drawable.ic_visibility_on),
                contentDescription = "",
                modifier = Modifier
                    .clickable { }
            )
        }
    )
}