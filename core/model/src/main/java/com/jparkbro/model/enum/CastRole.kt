package com.jparkbro.model.enum

import kotlinx.serialization.SerialName

enum class CastRole {
    @SerialName("MAIN")
    MAIN,
    @SerialName("SUPPORTING")
    SUPPORTING,
    @SerialName("BACKGROUND")
    BACKGROUND
}