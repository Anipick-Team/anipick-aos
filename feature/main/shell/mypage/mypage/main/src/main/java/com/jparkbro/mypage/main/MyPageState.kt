package com.jparkbro.mypage.main

import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.actor.Person
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.dto.mypage.main.WatchCounts

data class MyPageState(
    val uiState: UiState = UiState.Loading,

    /* API 통신 로딩 */
    val isLoading: Boolean = false,

    /* API 통신 데이터 */
    val nickname: String? = "AniPick",
    val profileImageByteArray: ByteArray? = null,
    val watchCounts: WatchCounts? = null,
    val likeAnimes: List<Anime> = emptyList(),
    val likeActors: List<Person> = emptyList()
)
