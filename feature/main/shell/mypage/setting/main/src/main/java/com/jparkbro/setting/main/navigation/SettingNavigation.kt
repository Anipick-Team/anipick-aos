package com.jparkbro.setting.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.model.enum.UserEditType
import com.jparkbro.setting.main.SettingRoot
import kotlinx.serialization.Serializable

@Serializable data object Setting

fun NavHostController.navigateToSetting(
    navOptions: NavOptions? = null
) = navigate(Setting, navOptions)

fun NavGraphBuilder.settingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToUserEdit: (UserEditType) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable<Setting> {
        SettingRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToUserEdit = onNavigateToUserEdit,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}