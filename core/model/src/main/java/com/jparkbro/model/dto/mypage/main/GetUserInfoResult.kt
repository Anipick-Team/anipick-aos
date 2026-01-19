package com.jparkbro.model.dto.mypage.main

import com.jparkbro.model.common.actor.Person
import com.jparkbro.model.common.anime.Anime

data class GetUserInfoResult(
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val profileImageBytes: ByteArray? = null,
    val watchCounts: WatchCounts? = null,
    val likedAnimes: List<Anime> = emptyList(),
    val likedPersons: List<Person> = emptyList(),
)
