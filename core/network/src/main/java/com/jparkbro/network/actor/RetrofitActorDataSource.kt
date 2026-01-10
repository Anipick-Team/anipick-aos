package com.jparkbro.network.actor

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.actor.GetActorResponse
import com.jparkbro.model.dto.info.GetInfoCharactersResponse
import com.jparkbro.model.dto.info.InfoActorResponse
import com.jparkbro.network.detail.RetrofitDetailDataSource
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import javax.inject.Inject

class RetrofitActorDataSource @Inject constructor(
    private val actorApi: ActorApi
) : ActorDataSource {
    companion object {
        private const val TAG = "RetrofitActorDataSource"
    }

    override suspend fun getDetailActor(animeId: Long): Result<List<InfoActorResponse>> {
        return actorApi.getDetailActor(animeId).toResult(TAG, "getDetailActor")
    }

    override suspend fun getInfoCharacters(animeId: Long, cursor: Cursor?): Result<GetInfoCharactersResponse> {
        return actorApi.getInfoCharacters(
            animeId = animeId,
            lastId = cursor?.lastId,
            lastValue = cursor?.lastValue
        ).toResult(TAG, "getInfoCharacters")
    }

    override suspend fun getActor(personId: Long, cursor: Cursor?): Result<GetActorResponse> {
        return actorApi.getActor(
            personId = personId,
            lastId = cursor?.lastId,
        ).toResult(TAG, "getInfoCharacters")
    }

    override suspend fun likeActor(personId: Long): Result<Unit> {
        return actorApi.likeActor(personId).toUnitResult(TAG, "likeActor")

    }

    override suspend fun unLikeActor(personId: Long): Result<Unit> {
        return actorApi.unLikeActor(personId).toUnitResult(TAG, "likeActor")
    }
}