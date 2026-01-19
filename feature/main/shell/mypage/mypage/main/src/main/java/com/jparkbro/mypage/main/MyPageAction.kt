package com.jparkbro.mypage.main

import android.net.Uri
import com.jparkbro.model.enum.UserContentType

interface MyPageAction {
    data object OnRetryClicked : MyPageAction
    data object NavigateToSetting : MyPageAction
    data class OnChangeProfileImage(val uri: Uri) : MyPageAction
    data class NavigateToUserContent(val contentType: UserContentType) : MyPageAction
    data class NavigateToInfoAnime(val animeId: Long) : MyPageAction
    data class NavigateToActor(val actorId: Long) : MyPageAction
}