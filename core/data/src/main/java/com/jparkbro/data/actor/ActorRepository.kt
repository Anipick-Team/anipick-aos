package com.jparkbro.data.actor

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.Cast
import com.jparkbro.model.detail.ActorDetailResponse
import com.jparkbro.model.dto.actor.GetActorResult
import com.jparkbro.model.dto.info.GetInfoCharactersResult

interface ActorRepository {

    /** Anime Detail Actor */
    suspend fun getDetailActor(animeId: Long): Result<List<Cast>>

    /** Info Character */
    suspend fun getInfoCharacters(animeId: Long, cursor: Cursor?): Result<GetInfoCharactersResult>

    /** Actor */
    suspend fun getActor(personId: Long, cursor: Cursor?): Result<GetActorResult>
    suspend fun updateActorLike(personId: Long, isLiked: Boolean): Result<Unit>
}