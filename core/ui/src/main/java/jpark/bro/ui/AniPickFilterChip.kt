package jpark.bro.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpark.bro.ui.theme.APColors

@Composable
fun APFilterTriggerChip(
    title: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .widthIn(min = 84.dp)
            .height(36.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) APColors.Secondary else APColors.LightGray,
                shape = RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            color = if (isSelected) APColors.Secondary else APColors.Black,
        )
        Icon(
            painter = painterResource(R.drawable.ic_chevron_down),
            contentDescription = null,
            tint = if (isSelected) APColors.Secondary else APColors.Gray
        )
    }
}

@Composable
fun APFilterOptionChip(
    title: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.W400,
        color = if (isSelected) APColors.Secondary else APColors.Black,
        modifier = Modifier
            .height(36.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) APColors.Secondary else APColors.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun APFilterTriggerChipPreview() {
    APFilterTriggerChip(
        title = "년도",
        isSelected = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun APFilterOptionChipPreview() {
    APFilterOptionChip(
        title = "년도",
        isSelected = true,
        onClick = {}
    )
}