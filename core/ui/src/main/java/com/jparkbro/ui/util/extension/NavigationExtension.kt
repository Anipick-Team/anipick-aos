package com.jparkbro.ui.util.extension

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

fun NavOptionsBuilder.clearStack(navController: NavHostController) {
    popUpTo(navController.graph.id) {
        saveState = false
    }
    launchSingleTop = true
    restoreState = false
}

/** 모든 스택 제거 */
fun NavOptionsBuilder.clearAllBackStack(
    saveState: Boolean = false
) {
    launchSingleTop = true
    popUpTo(0) {
        inclusive = true
        this.saveState = saveState
    }
}
