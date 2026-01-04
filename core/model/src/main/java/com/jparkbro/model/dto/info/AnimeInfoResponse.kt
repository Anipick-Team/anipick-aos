package com.jparkbro.model.dto.info

import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.StudioMap
import com.jparkbro.model.enum.WatchStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeInfoResponse(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String?,
    @SerialName("bannerImageUrl")
    val bannerImageUrl: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("averageRating")
    val averageRating: String?,
    @SerialName("isLiked")
    val isLiked: Boolean,
    @SerialName("watchStatus")
    val watchStatus: WatchStatus?,
    @SerialName("type")
    val type: String?,
    @SerialName("reviewCount")
    val reviewCount: Int,
    @SerialName("genres")
    val genres: List<ResponseMap>,
    @SerialName("episode")
    val episode: Int?,
    @SerialName("airDate")
    val airDate: String?,
    @SerialName("status")
    val status: String?,
    @SerialName("age")
    val age: String?,
    @SerialName("studios")
    val studios: List<StudioMap>
)