package com.jparkbro.model.mypage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyPageResponse(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String,
    @SerialName("watchCounts")
    val watchCounts: MyPageWatchCount,
    @SerialName("likedAnimes")
    val likedAnimes: List<LikedAnime>,
    @SerialName("likedPersons")
    val likedPersons: List<LikedPerson>
)

@Serializable
data class MyPageWatchCount(
    @SerialName("watchList")
    val watchList: Int,
    @SerialName("watching")
    val watching: Int,
    @SerialName("finished")
    val finished: Int,
)

@Serializable
data class LikedAnime(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("animeLikeId")
    val animeLikeId: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
)

@Serializable
data class LikedPerson(
    @SerialName("personId")
    val personId: Int,
    @SerialName("userLikedVoiceActorId")
    val userLikedVoiceActorId: Int,
    @SerialName("name")
    override val name: String?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String,
) : UserContentData {
    override val id: Int get() = personId
    override val imageUrl: String get() = profileImageUrl
}

@Serializable
data class UserContentAnime(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("userAnimeStatusId")
    val userAnimeStatusId: Int? = null,
    @SerialName("myRating")
    val myRating: Float? = null,
    @SerialName("animeLikeId")
    val animeLikeId: Int? = null,
) : UserContentData {
    override val id: Int get() = animeId
    override val name: String? get() = title
    override val imageUrl: String get() = coverImageUrl
}

sealed interface UserContentData {
    val id: Int
    val name: String?
    val imageUrl: String
}