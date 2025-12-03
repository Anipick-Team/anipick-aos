package com.jparkbro.anipick.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.jparkbro.explore.navigation.navigateToExplore
import com.jparkbro.home.navigation.navigateToHome
import com.jparkbro.mypage.navigation.navigateToMyPage
import com.jparkbro.ranking.navigation.navigateToRanking
import com.jparkbro.ui.theme.APColors

@Composable
internal fun APBottomNavigation(
    modifier: Modifier = Modifier,
    items: List<BottomDestination>,
    navController: NavHostController,
    currentDestination: NavDestination? = null,
) {
    var lastMainDestination by remember { mutableStateOf(BottomDestination.HOME) }

    LaunchedEffect(currentDestination) {
        val currentMainDest = BottomDestination.entries.find {
            currentDestination?.route == it.route.java.name
        }
        if (currentMainDest != null) {
            lastMainDestination = currentMainDest
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
            .background(Color.White),
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = APColors.LightGray
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEach { destination ->
                val isSelected = if (BottomDestination.entries.any { bottomDest ->
                    currentDestination?.route?.startsWith(bottomDest.route.java.name) == true
                    }) {
                    currentDestination?.route?.startsWith(destination.route.java.name) == true
                } else {
                    lastMainDestination == destination
                }

                val navOptions: NavOptions = navOptions {
                    clearBackStack(navController)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (!isSelected) {
                                when (destination) {
                                    BottomDestination.HOME -> { navController.navigateToHome(navOptions) }
                                    BottomDestination.RANKING -> { navController.navigateToRanking(navOptions) }
                                    BottomDestination.EXPLORE -> { navController.navigateToExplore(navOptions = navOptions) }
                                    BottomDestination.MY_PAGE -> { navController.navigateToMyPage(navOptions) }
                                }
                            }
                        }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        painter = painterResource(
                            if (isSelected) destination.selectedIcon else destination.unselectedIcon
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(18.dp)
                    )
                    Text(
                        text = stringResource(destination.labelTextId),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = if (isSelected) APColors.Primary else APColors.Gray
                    )
                }
            }
        }
    }
}