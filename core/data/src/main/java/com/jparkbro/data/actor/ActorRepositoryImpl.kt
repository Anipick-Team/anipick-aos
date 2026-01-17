package com.jparkbro.data.actor

import com.jparkbro.data.user.UserRepository
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.Cast
import com.jparkbro.model.dto.actor.GetActorResult
import com.jparkbro.model.dto.actor.toResult
import com.jparkbro.model.dto.info.GetInfoCharactersResult
import com.jparkbro.model.dto.info.toCast
import com.jparkbro.model.dto.info.toResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.model.dto.mypage.usercontent.toResult
import com.jparkbro.network.actor.ActorDataSource
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ActorRepositoryImpl @Inject constructor(
    private val actorDataSource: ActorDataSource,
    private val userRepository: UserRepository,
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

    /** User Content */
    override suspend fun loadUserContentActors(request: GetUserContentRequest): Result<Unit> {
        return actorDataSource.loadUserContentActors(request)
            .map { it.toResult() }
            .fold(
                onSuccess = { result ->
                    userRepository.userContentCache.update { cache ->
                        cache.copy(
                            contentType = request.contentType,
                            actors = if (request.lastId == null) result.actors else cache.actors + result.actors,
                            cursor = result.cursor,
                            count = result.count
                        )
                    }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    override suspend fun invalidateUserContent() {
        val currentType = userRepository.userContentCache.value.contentType ?: return
        userRepository.userContentCache.update { GetUserContentResult(contentType = currentType) }
        loadUserContentActors(GetUserContentRequest(contentType = currentType))
    }
}