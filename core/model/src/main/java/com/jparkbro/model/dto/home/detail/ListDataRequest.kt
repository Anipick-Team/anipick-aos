package com.jparkbro.model.dto.home.detail

import com.jparkbro.model.enum.HomeDetailSortType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDataRequest(
    @SerialName("animeId")
    val animeId: Int? = null,
    @SerialName("lastId")
    val lastId: Int? = null,
    @SerialName("lastValue")
    val lastValue: String? = null,
    @SerialName("sort")
    val sort: String = HomeDetailSortType.LATEST.param,
    @SerialName("size")
    val size: Int = 18,
)
