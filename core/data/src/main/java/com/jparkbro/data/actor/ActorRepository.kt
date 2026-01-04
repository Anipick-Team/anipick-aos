package com.jparkbro.data.actor

import com.jparkbro.model.dto.info.InfoActorResult

interface ActorRepository {

    /* Anime Detail Actor */
    suspend fun getDetailActor(animeId: Int): Result<List<InfoActorResult>>
}