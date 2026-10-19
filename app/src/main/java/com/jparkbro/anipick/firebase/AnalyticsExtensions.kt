package com.jparkbro.anipick.firebase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavKey
import com.jparkbro.core.navigation.NavigationState
import kotlinx.serialization.serializer

@Composable
fun TrackScreenNavigation(navigationState: NavigationState) {
    val context = LocalContext.current
    val currentKey = navigationState.subStacks[navigationState.currentTopLevelKey]?.lastOrNull() ?: return

    LaunchedEffect(currentKey) {
        // 전체 route: com.jparkbro.login.navigation.Login
        // 원하는 결과: login
        val fullRoute = currentKey.toRoutePattern()

        val screenName = fullRoute
            .substringAfterLast(".")  // Login

        val screenClass = fullRoute
            .substringBefore("?")     // 쿼리 파라미터 제거
            .substringBefore("/")     // 경로 파라미터 제거

        FirebaseManager.getInstance(context).logScreenView(
            screenName = screenName,
            screenClass = screenClass
        )
    }
}

/** Navigation 2의 type-safe route 문자열과 같은 형식(`serialName/{arg}?optional={optional}`)으로 만들어 기존 Analytics 화면 이름을 유지한다. */
private fun NavKey.toRoutePattern(): String {
    val descriptor = serializer(this::class.java).descriptor
    val (optional, required) = (0 until descriptor.elementsCount).partition { descriptor.isElementOptional(it) }
    val path = required.joinToString("") { "/{${descriptor.getElementName(it)}}" }
    val query = optional.joinToString("&") { "${descriptor.getElementName(it)}={${descriptor.getElementName(it)}}" }
    return descriptor.serialName + path + if (query.isEmpty()) "" else "?$query"
}
