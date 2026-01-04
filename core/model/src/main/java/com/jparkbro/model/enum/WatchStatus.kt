package com.jparkbro.model.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class WatchStatus {
    @SerialName("WATCHLIST")
    WATCHLIST,

    @SerialName("WATCHING")
    WATCHING,

    @SerialName("FINISHED")
    FINISHED,
}