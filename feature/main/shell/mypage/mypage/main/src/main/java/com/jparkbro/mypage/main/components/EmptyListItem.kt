package com.jparkbro.mypage.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickSmallShape

@Composable
internal fun EmptyListItem(
    contentText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AniPickGray50, AniPickSmallShape)
            .padding(horizontal = dimensionResource(R.dimen.space_32), vertical = dimensionResource(R.dimen.padding_default))
    ) {
        Image(
            painter = painterResource(R.drawable.ic_mascot_empty),
            contentDescription = stringResource(R.string.empty_list_item_img),
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
        Text(
            text = contentText,
            style = AniPick16Normal.copy(color = AniPickBlack),
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.padding_small))
        )
    }
}