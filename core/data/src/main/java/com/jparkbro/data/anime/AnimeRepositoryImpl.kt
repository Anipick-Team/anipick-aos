package com.jparkbro.data.anime

import com.jparkbro.data.user.UserRepository
import com.jparkbro.datastore.RecentAnimeDataStore
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.anime.toAnime
import com.jparkbro.model.dto.explore.GetAnimeExploreRequest
import com.jparkbro.model.dto.explore.GetAnimeExploreResult
import com.jparkbro.model.dto.explore.toResult
import com.jparkbro.model.dto.home.main.RecommendedAnimesResult
import com.jparkbro.model.dto.home.main.toResult
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.GetInfoRecommendResult
import com.jparkbro.model.dto.info.GetInfoSeriesResult
import com.jparkbro.model.dto.info.toResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.model.dto.mypage.usercontent.toResult
import com.jparkbro.model.dto.ranking.GetAnimeRankingRequest
import com.jparkbro.model.dto.ranking.GetAnimeRankingResult
import com.jparkbro.model.dto.ranking.toResult
import com.jparkbro.model.enum.RankingType
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
            .onSuccess {
                getRecentRecommendItems(animeId)
            }.map { Unit }
    }

    override suspend fun loadRecentAnime(): Result<Long> {
        return recentAnimeDataStore.loadRecentAnime()
    }

    private val _recentRecommendAnime = MutableStateFlow<RecommendedAnimesResult?>(null)
    override val recentRecommendAnime = _recentRecommendAnime.asStateFlow()
    override suspend fun getRecentRecommendItems(animeId: Long): Result<Unit> {
        return animeDataSource.getRecentRecommendItems(animeId).map { it.toResult() }
            .onSuccess {
                _recentRecommendAnime.update { it }
            }.map { Unit }
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
            .onSuccess { response ->
                detailInfoCache.update { cache ->
                    cache + (animeId to response)
                }
            }.map { Unit }
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

        return result
            .onSuccess {
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
            }.map { Unit }
    }

    override suspend fun updateWatchStatus(animeId: Long, watchStatus: WatchStatus): Result<Unit> {
        val currentInfo = detailInfoCache.value[animeId]
        val currentStatus = currentInfo?.watchStatus

        val result = when (currentStatus) {
            null -> animeDataSource.createWatchStatus(animeId, watchStatus)
            watchStatus -> animeDataSource.deleteWatchStatus(animeId)
            else -> animeDataSource.updateWatchStatus(animeId, watchStatus)
        }

        return result
            .onSuccess {
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
            }.map { Unit }
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
            .onSuccess { result ->
                userRepository.userContentCache.update { cache ->
                    cache.copy(
                        contentType = request.contentType,
                        animes = if (request.lastId == null) result.animes else cache.animes + result.animes,
                        cursor = result.cursor,
                        count = result.count
                    )
                }
            }.map { Unit }
    }

    override suspend fun invalidateUserContent() {
        val currentType = userRepository.userContentCache.value.contentType ?: return
        userRepository.userContentCache.update { GetUserContentResult(contentType = currentType) }
        loadUserContentAnimes(GetUserContentRequest(contentType = currentType))
    }

    override suspend fun getAnimeRanking(request: GetAnimeRankingRequest): Result<GetAnimeRankingResult> {
        val apiCall = when (request.type) {
            RankingType.REAL_TIME -> animeDataSource.getRealTimeRanking(request)
            RankingType.YEAR_SEASON -> animeDataSource.getYearSeasonRanking(request)
            RankingType.ALL_TIME -> animeDataSource.getAllTimeRanking(request)
        }

        return apiCall.map { it.toResult() }
    }

    override suspend fun getAnimeExplore(request: GetAnimeExploreRequest): Result<GetAnimeExploreResult> {
        return animeDataSource.getAnimeExplore(request).map { it.toResult() }
    }
}