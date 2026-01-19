package com.jparkbro.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import com.jparkbro.ui.R
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick16Bold
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite

@Composable
fun APPrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = AniPickPrimary,
        contentColor = AniPickWhite,
        disabledContainerColor = AniPickGray100,
        disabledContentColor = AniPickWhite
    ),
    isLoading: Boolean = false,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
        enabled = enabled,
        shape = AniPickSmallShape,
        colors = colors,
        contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.padding_medium))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_extra_large))
                    .alpha(if (isLoading) 1f else 0f),
                color = AniPickWhite,
            )
            Text(
                text = text,
                style = AniPick16Bold.copy(color = AniPickWhite),
                modifier = Modifier
                    .alpha(if (isLoading) 0f else 1f)
            )
        }
    }
}

@DevicePreviews
@Composable
private fun APPrimaryActionButtonPreview() {
    APPrimaryActionButton(
        text = "확인",
        onClick = {}
    )
}