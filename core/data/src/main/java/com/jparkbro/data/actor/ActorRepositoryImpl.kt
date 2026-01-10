package com.jparkbro.data.actor

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.Cast
import com.jparkbro.model.dto.actor.GetActorResult
import com.jparkbro.model.dto.actor.toResult
import com.jparkbro.model.dto.info.GetInfoCharactersResult
import com.jparkbro.model.dto.info.toCast
import com.jparkbro.model.dto.info.toResult
import com.jparkbro.network.actor.ActorDataSource
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ActorRepositoryImpl @Inject constructor(
    private val actorDataSource: ActorDataSource
) : ActorRepository {

    override suspend fun getDetailActor(animeId: Long): Result<List<Cast>> {
        return actorDataSource.getDetailActor(animeId).map { it.map { it.toCast() } }
    }

    override suspend fun getInfoCharacters(animeId: Long, cursor: Cursor?): Result<GetInfoCharactersResult> {
        return actorDataSource.getInfoCharacters(animeId, cursor).map { it.toResult() }
    }

    override suspend fun getActor(personId: Long, cursor: Cursor?): Result<GetActorResult> {
        return actorDataSource.getActor(personId, cursor).map { it.toResult() }
    }

    override suspend fun updateActorLike(personId: Long, isLiked: Boolean): Result<Unit> {
        return if (isLiked) {
            actorDataSource.unLikeActor(personId)
        } else {
            actorDataSource.likeActor(personId)
        }
    }
}