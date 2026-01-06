package com.jparkbro.network.actor

import com.jparkbro.model.dto.info.InfoActorResponse

interface ActorDataSource {
    suspend fun getDetailActor(animeId: Long): Result<List<InfoActorResponse>>
}