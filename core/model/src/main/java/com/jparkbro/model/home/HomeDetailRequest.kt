package com.jparkbro.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDetailRequest(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("lastId")
    val lastId: Long? = null,
    @SerialName("lastValue")
    val lastValue: String? = null,
    @SerialName("sort")
    val sort: String = Sort.LATEST.param,
    @SerialName("size")
    val size: Int = 18,
)

enum class Sort(val param: String, val displayName: String) {
    LATEST("latest", "최신순"),
    POPULARITY("popularity", "인기순"),
    START_DATE("startDate", "방영 예정 순")
}