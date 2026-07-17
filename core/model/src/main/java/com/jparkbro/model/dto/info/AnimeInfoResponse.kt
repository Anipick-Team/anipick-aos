package com.jparkbro.model.dto.info

import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.Studio
import com.jparkbro.model.enum.WatchStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeInfoResponse(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("bannerImageUrl")
    val bannerImageUrl: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("averageRating")
    val averageRating: String? = null,
    @SerialName("isLiked")
    val isLiked: Boolean? = null,
    @SerialName("watchStatus")
    val watchStatus: WatchStatus? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("reviewCount")
    val reviewCount: Int? = null,
    @SerialName("genres")
    val genres: List<ResponseMap> = emptyList(),
    @SerialName("episode")
    val episode: Int? = null,
    @SerialName("airDate")
    val airDate: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("age")
    val age: String? = null,
    @SerialName("studios")
    val studios: List<Studio> = emptyList()
)