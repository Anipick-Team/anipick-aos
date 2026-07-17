package com.jparkbro.network.actor

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.dto.actor.GetActorResponse
import com.jparkbro.model.dto.info.GetInfoCharactersResponse
import com.jparkbro.model.dto.info.InfoActorResponse
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResponse
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.dto.search.GetSearchResultResponse
import com.jparkbro.network.util.safeApiCall
import com.jparkbro.network.util.safeApiCallUnit
import javax.inject.Inject

class RetrofitActorDataSource @Inject constructor(
    private val actorApi: ActorApi
) : ActorDataSource {
    companion object {
        private const val TAG = "RetrofitActorDataSource"
    }

    override suspend fun getDetailActor(animeId: Long): Result<List<InfoActorResponse>> {
        return safeApiCall(TAG, "getDetailActor") { actorApi.getDetailActor(animeId) }
    }

    override suspend fun getInfoCharacters(animeId: Long, cursor: Cursor?): Result<GetInfoCharactersResponse> {
        return safeApiCall(TAG, "getInfoCharacters") {
            actorApi.getInfoCharacters(
                animeId = animeId,
                lastId = cursor?.lastId,
                lastValue = cursor?.lastValue
            )
        }
    }

    override suspend fun getActor(personId: Long, cursor: Cursor?): Result<GetActorResponse> {
        return safeApiCall(TAG, "getActor") {
            actorApi.getActor(
                personId = personId,
                lastId = cursor?.lastId,
            )
        }
    }

    override suspend fun likeActor(personId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "likeActor") { actorApi.likeActor(personId) }
    }

    override suspend fun unLikeActor(personId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "unLikeActor") { actorApi.unLikeActor(personId) }
    }

    override suspend fun loadUserContentActors(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return safeApiCall(TAG, "loadUserContentActors") {
            actorApi.loadUserContentActors(
                lastId = request.lastId,
            )
        }
    }

    override suspend fun getSearchResult(request: GetSearchResultRequest): Result<GetSearchResultResponse> {
        return safeApiCall(TAG, "getSearchResult") {
            actorApi.getSearchResult(
                query = request.query,
                lastId = request.lastId,
                size = request.size,
            )
        }
    }
}
