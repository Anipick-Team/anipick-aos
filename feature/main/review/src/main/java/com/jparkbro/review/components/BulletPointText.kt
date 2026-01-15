package com.jparkbro.review.components


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R

@Composable
internal fun BulletPointText(
    text: String,
    textStyle: TextStyle
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.review_form_caution_bullet_point),
            style = textStyle,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_small))
                .alignByBaseline()
        )
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier
                .alignByBaseline()
        )
    }
}