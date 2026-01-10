package com.jparkbro.network.actor

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.actor.GetActorResponse
import com.jparkbro.model.dto.info.GetInfoCharactersResponse
import com.jparkbro.model.dto.info.InfoActorResponse

interface ActorDataSource {
    /** Anime Detail Actor */
    suspend fun getDetailActor(animeId: Long): Result<List<InfoActorResponse>>

    /** Info Character */
    suspend fun getInfoCharacters(animeId: Long, cursor: Cursor?): Result<GetInfoCharactersResponse>

    /** Actor */
    suspend fun getActor(personId: Long, cursor: Cursor?): Result<GetActorResponse>
    suspend fun likeActor(personId: Long): Result<Unit>
    suspend fun unLikeActor(personId: Long): Result<Unit>
}