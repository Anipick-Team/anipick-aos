package com.jparkbro.data.anime

import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.anime.toAnime
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.network.anime.AnimeDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val animeDataSource: AnimeDataSource
) : AnimeRepository {

    /** Anime Detail Info */
    private val _detailInfo = MutableStateFlow<AnimeInfoResponse?>(null)
    override val detailInfo = _detailInfo.asStateFlow()
    override suspend fun getDetailInfo(animeId: Int): Result<Unit> {
        return animeDataSource.getDetailInfo(animeId)
            .fold(
                onSuccess = { response ->
                    _detailInfo.update { response }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    override suspend fun getDetailSeries(animeId: Int): Result<List<Anime>> {
        return animeDataSource.getDetailSeries(animeId).map { animeDtos ->
            animeDtos.map { it.toAnime() }
        }
    }

    override suspend fun getDetailRecommendation(animeId: Int): Result<List<Anime>> {
        return animeDataSource.getDetailRecommendation(animeId).map { animeDtos ->
            animeDtos.map { it.toAnime() }
        }
    }
}