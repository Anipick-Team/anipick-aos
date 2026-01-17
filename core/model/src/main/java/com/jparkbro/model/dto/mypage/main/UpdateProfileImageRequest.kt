package com.jparkbro.model.dto.mypage.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileImageRequest(
    val name: String = "profileImageFile",
    @SerialName("profileImageUrl")
    val imageData: ByteArray?,
    val mimeType: String?,
    val filename: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UpdateProfileImageRequest

        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        return imageData.contentHashCode()
    }
}
