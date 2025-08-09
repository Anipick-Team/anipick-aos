package com.jparkbro.model.search

import com.jparkbro.model.common.Cursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("animeCount")
    val animeCount: Int? = null,
    @SerialName("personCount")
    val personCount: Int? = null,
    @SerialName("studioCount")
    val studioCount: Int? = null,
    @SerialName("nextPage")
    val nextPage: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<SearchResultAnime> = emptyList(),
    @SerialName("persons")
    val persons: List<SearchResultPerson> = emptyList(),
    @SerialName("studios")
    val studios: List<SearchResultStudio> = emptyList(),
)

@Serializable
data class SearchResultAnime(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("clickLog")
    val clickLog: String,
    @SerialName("impressionLog")
    val impressionLog: String,
)

@Serializable
data class SearchResultPerson(
    @SerialName("personId")
    val personId: Int,
    @SerialName("name")
    val name: String?,
    @SerialName("profileImage")
    val profileImage: String,
)

@Serializable
data class SearchResultStudio(
    @SerialName("studioId")
    val studioId: Int,
    @SerialName("name")
    val name: String?,
)