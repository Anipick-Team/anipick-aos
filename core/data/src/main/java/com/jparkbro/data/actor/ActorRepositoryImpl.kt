package com.jparkbro.data.actor

import com.jparkbro.model.dto.info.InfoActorResult
import com.jparkbro.model.dto.info.toResult
import com.jparkbro.network.actor.ActorDataSource
import javax.inject.Inject

class ActorRepositoryImpl @Inject constructor(
    private val actorDataSource: ActorDataSource
) : ActorRepository {

    override suspend fun getDetailActor(animeId: Int): Result<List<InfoActorResult>> {
        return actorDataSource.getDetailActor(animeId).map { it.map { it.toResult() } }
    }
}