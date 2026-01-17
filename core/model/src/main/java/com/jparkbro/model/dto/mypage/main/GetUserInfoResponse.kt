package com.jparkbro.model.dto.mypage.main

import com.jparkbro.model.common.actor.UserLikedActorDto
import com.jparkbro.model.common.actor.toPerson
import com.jparkbro.model.common.anime.UserLikedAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserInfoResponse(
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
    @SerialName("watchCounts")
    val watchCounts: WatchCounts? = null,
    @SerialName("likedAnimes")
    val likedAnimes: List<UserLikedAnimeDto> = emptyList(),
    @SerialName("likedPersons")
    val likedPersons: List<UserLikedActorDto> = emptyList(),
)

fun GetUserInfoResponse.toResult() : GetUserInfoResult = GetUserInfoResult(
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    watchCounts = watchCounts,
    likedAnimes = likedAnimes.map { it.toAnime() },
    likedPersons = likedPersons.map { it.toPerson() }
)