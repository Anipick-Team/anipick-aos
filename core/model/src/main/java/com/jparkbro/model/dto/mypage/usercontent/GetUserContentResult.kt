package com.jparkbro.model.dto.mypage.usercontent

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.Person
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.enum.UserContentType

data class GetUserContentResult(
    val contentType: UserContentType? = null,
    val count: Int? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val actors: List<Person> = emptyList(),
)
