package com.jparkbro.setting.detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.model.enum.UserEditType
import com.jparkbro.setting.detail.UserEditRoot
import kotlinx.serialization.Serializable

@Serializable data class UserEdit(val editType: UserEditType)

fun NavHostController.navigateToUserEdit(
    editType: UserEditType, navOptions: NavOptions? = null
) = navigate(UserEdit(editType), navOptions)

fun NavGraphBuilder.userEditScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable<UserEdit> {
        UserEditRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}