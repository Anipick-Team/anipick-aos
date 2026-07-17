package com.jparkbro.model.dto.search

data class GetSearchResultRequest(
    val query: String,
    val lastId: Long? = null,
    val size: Int? = null,
    val page: Int? = null,
)
