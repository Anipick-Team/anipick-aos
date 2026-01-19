package com.jparkbro.model.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RankingTrend {
    @SerialName("up")
    UP,

    @SerialName("down")
    DOWN,

    @SerialName("same")
    SAME,

    @SerialName("new")
    NEW,
}