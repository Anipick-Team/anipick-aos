package com.jparkbro.data.anime

import com.jparkbro.data.user.UserRepository
import com.jparkbro.datastore.RecentAnimeDataStore
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.anime.toAnime
import com.jparkbro.model.dto.home.main.RecommendedAnimesResult
import com.jparkbro.model.dto.home.main.toResult
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.GetInfoRecommendResult
import com.jparkbro.model.dto.info.GetInfoSeriesResult
import com.jparkbro.model.dto.info.toResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.model.dto.mypage.usercontent.toResult
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.model.enum.WatchStatus
import com.jparkbro.network.anime.AnimeDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeDataSource: AnimeDataSource,
    private val userRepository: UserRepository,
    private val recentAnimeDataStore: RecentAnimeDataStore,
) : AnimeRepository {

    override suspend fun saveRecentAnime(animeId: Long): Result<Unit> {
        return recentAnimeDataStore.saveRecentAnime(animeId)
            .fold(
                onSuccess = {
                    getRecentRecommendItems(animeId)
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }
    override suspend fun loadRecentAnime(): Result<Long> {
        return recentAnimeDataStore.loadRecentAnime()
    }

    private val _recentRecommendAnime = MutableStateFlow<RecommendedAnimesResult?>(null)
    override val recentRecommendAnime = _recentRecommendAnime.asStateFlow()
    override suspend fun getRecentRecommendItems(animeId: Long): Result<Unit> {
        return animeDataSource.getRecentRecommendItems(animeId).map { it.toResult() }
            .fold(
                onSuccess = { result ->
                    _recentRecommendAnime.update { result }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    /** Anime Detail Info */
    private val detailInfoCache = MutableStateFlow<Map<Long, AnimeInfoResponse>>(emptyMap())
    override fun getDetailInfo(animeId: Long): Flow<AnimeInfoResponse?> {
        return detailInfoCache.map { cache ->
            cache[animeId]
        }
    }
    override suspend fun loadDetailInfo(animeId: Long): Result<Unit> {
        return animeDataSource.getDetailInfo(animeId)
            .fold(
                onSuccess = { response ->
                    detailInfoCache.update { cache ->
                        cache + (animeId to response)
                    }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    override suspend fun getDetailSeries(animeId: Long): Result<List<Anime>> {
        return animeDataSource.getDetailSeries(animeId).map { animeDtos ->
            animeDtos.map { it.toAnime() }
        }
    }

    override suspend fun getDetailRecommendation(animeId: Long): Result<List<Anime>> {
        return animeDataSource.getDetailRecommendation(animeId).map { animeDtos ->
            animeDtos.map { it.toAnime() }
        }
    }

    override suspend fun updateAnimeLike(animeId: Long, isLiked: Boolean): Result<Unit> {
        val result = if (!isLiked) {
            animeDataSource.setLikeAnime(animeId)
        } else {
            animeDataSource.setUnlikeAnime(animeId)
        }

        return result.fold(
            onSuccess = {
                detailInfoCache.update { cache ->
                    val currentInfo = cache[animeId]
                    if (currentInfo != null) {
                        cache + (animeId to currentInfo.copy(isLiked = !isLiked))
                    } else {
                        cache
                    }
                }
                userRepository.refreshUserInfo()
                invalidateUserContent()
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun updateWatchStatus(animeId: Long, watchStatus: WatchStatus): Result<Unit> {
        val currentInfo = detailInfoCache.value[animeId]
        val currentStatus = currentInfo?.watchStatus

        val result = when (currentStatus) {
            null -> animeDataSource.createWatchStatus(animeId, watchStatus)
            watchStatus -> animeDataSource.deleteWatchStatus(animeId)
            else -> animeDataSource.updateWatchStatus(animeId, watchStatus)
        }

        return result.fold(
            onSuccess = {
                detailInfoCache.update { cache ->
                    if (currentInfo != null) {
                        cache + (animeId to currentInfo.copy(
                            watchStatus = if (currentStatus == watchStatus) null else watchStatus
                        ))
                    } else {
                        cache
                    }
                }
                userRepository.refreshUserInfo()
                invalidateUserContent()
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun getAnimeSeries(animeId: Long, cursor: Cursor?): Result<GetInfoSeriesResult> {
        return animeDataSource.getAnimeSeries(animeId, cursor).map { it.toResult() }
    }

    override suspend fun getAnimeRecommends(animeId: Long, cursor: Cursor?): Result<GetInfoRecommendResult> {
        return animeDataSource.getAnimeRecommends(animeId, cursor).map { it.toResult() }
    }

    /** User Content */
    override suspend fun loadUserContentAnimes(request: GetUserContentRequest): Result<Unit> {
        val apiCall = when (request.contentType) {
            UserContentType.WATCHLIST -> animeDataSource.loadWatchListAnimes(request)
            UserContentType.WATCHING -> animeDataSource.loadWatchingAnimes(request)
            UserContentType.FINISHED -> animeDataSource.loadFinishedAnimes(request)
            UserContentType.LIKED_ANIME -> animeDataSource.loadLikedAnimes(request)
            else -> return Result.failure(IllegalArgumentException("Invalid content type"))
        }

        return apiCall.map { it.toResult() }
            .fold(
                onSuccess = { result ->
                    userRepository.userContentCache.update { cache ->
                        cache.copy(
                            contentType = request.contentType,
                            animes = if (request.lastId == null) result.animes else cache.animes + result.animes,
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
        loadUserContentAnimes(GetUserContentRequest(contentType = currentType))
    }
}