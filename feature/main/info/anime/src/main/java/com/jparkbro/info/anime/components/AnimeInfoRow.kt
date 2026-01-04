package com.jparkbro.info.anime.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack

@Composable
internal fun AnimeInfoRow(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = AniPick14Normal.copy(color = AniPickBlack),
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .weight(4f),
            contentAlignment = Alignment.CenterEnd
        ) {
            content()
        }
    }
}