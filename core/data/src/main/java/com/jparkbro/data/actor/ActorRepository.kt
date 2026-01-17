package com.jparkbro.data.actor

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.Cast
import com.jparkbro.model.common.actor.Person
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.detail.ActorDetailResponse
import com.jparkbro.model.dto.actor.GetActorResult
import com.jparkbro.model.dto.info.GetInfoCharactersResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import kotlinx.coroutines.flow.Flow

interface ActorRepository {

    /** Anime Detail Actor */
    suspend fun getDetailActor(animeId: Long): Result<List<Cast>>

    /** Info Character */
    suspend fun getInfoCharacters(animeId: Long, cursor: Cursor?): Result<GetInfoCharactersResult>

    /** Actor */
    suspend fun getActor(personId: Long, cursor: Cursor?): Result<GetActorResult>
    suspend fun updateActorLike(personId: Long, isLiked: Boolean): Result<Unit>

    /** User Content */
    suspend fun loadUserContentActors(request: GetUserContentRequest): Result<Unit>
    suspend fun invalidateUserContent()
}