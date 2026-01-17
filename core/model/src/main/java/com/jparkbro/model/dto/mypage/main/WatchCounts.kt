package com.jparkbro.model.dto.mypage.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchCounts(
    @SerialName("watchList")
    val watchList: Int? = 0,
    @SerialName("watching")
    val watching: Int? = 0,
    @SerialName("finished")
    val finished: Int? = 0,
)
