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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.jparkbro.explore.navigation.navigateToExplore
import com.jparkbro.home.main.navigation.navigateToHome
import com.jparkbro.mypage.main.navigation.navigateToMyPage
import com.jparkbro.ranking.navigation.navigateToRanking
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.util.extension.clearStack

@Composable
internal fun APBottomNavigation(
    modifier: Modifier = Modifier,
    items: List<BottomDestination>,
    navController: NavHostController,
    currentDestination: NavDestination? = null,
) {
    var lastMainDestination by rememberSaveable { mutableStateOf(BottomDestination.HOME) }

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
            thickness = dimensionResource(R.dimen.border_width_default),
            color = APColors.LightGray
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.space_36)),
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
                    clearStack(navController)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (!isSelected) {
                                when (destination) {
                                    BottomDestination.HOME -> { navController.navigateToHome() }
                                    BottomDestination.RANKING -> { navController.navigateToRanking() }
                                    BottomDestination.EXPLORE -> { navController.navigateToExplore(navOptions = navOptions) }
                                    BottomDestination.MY_PAGE -> { navController.navigateToMyPage() }
                                }
                            }
                        }
                        .padding(vertical = dimensionResource(R.dimen.padding_medium)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
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
                        style = AniPick12Normal.copy(color = if (isSelected) AniPickPrimary else AniPickGray100)
                    )
                }
            }
        }
    }
}