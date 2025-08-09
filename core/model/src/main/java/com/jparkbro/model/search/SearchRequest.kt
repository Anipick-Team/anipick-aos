package com.jparkbro.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    @SerialName("query")
    val query: String,
    @SerialName("lastId")
    val lastId: Int? = null,
    @SerialName("size")
    val size: Int? = null,
    @SerialName("page")
    val page: Int? = null,
)
