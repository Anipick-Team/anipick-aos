package com.jparkbro.model.dto.mypage.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileImageResponse(
    @SerialName("imageId")
    val imageId: String? = null
)