package com.jparkbro.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jparkbro.model.setting.ProfileEditType
import com.jparkbro.setting.ProfileEdit
import com.jparkbro.setting.Setting
import kotlinx.serialization.Serializable

@Serializable data object Setting

@Serializable data class ProfileEdit(val type: ProfileEditType)

fun NavController.navigateToSetting(navOptions: NavOptions? = null) = navigate(Setting, navOptions)

fun NavGraphBuilder.settingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfileEdit: (ProfileEditType) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable<Setting> {
        Setting(
            onNavigateBack = onNavigateBack,
            onNavigateToProfileEdit = onNavigateToProfileEdit,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}

fun NavController.navigateToProfileEdit(type: ProfileEditType, navOptions: NavOptions? = null) = navigate(ProfileEdit(type), navOptions)

fun NavGraphBuilder.profileEditScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable<ProfileEdit> { entry ->
        val route = entry.toRoute<ProfileEdit>()

        ProfileEdit(
            type = route.type,
            onNavigateBack = onNavigateBack,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}