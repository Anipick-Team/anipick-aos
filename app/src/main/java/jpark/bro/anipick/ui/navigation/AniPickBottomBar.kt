package jpark.bro.anipick.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import jpark.bro.anipick.ui.theme.APColors
import jpark.bro.anipick.ui.navigation.AniPickDestinations.Tabs.items as entries

@Composable
fun APBottomBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = APColors.White,
    ) {
        entries.forEach { destination ->
            val selected = selectedTab == destination.route

            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(destination.route) },
                icon = {
                    Icon(
                        painter = if (selected) painterResource(destination.selectedIcon) else painterResource(destination.unselectedIcon),
                        contentDescription = "",
                        tint = if (selected) APColors.Primary else APColors.Gray
                    )
                },
                label = {
                    Text(
                        text = destination.label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = if (selected) APColors.Primary else APColors.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )

        }
    }
}