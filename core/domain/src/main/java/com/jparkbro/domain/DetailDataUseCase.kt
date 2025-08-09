package com.jparkbro.domain

import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.model.common.Result
import com.jparkbro.model.common.asResult
import com.jparkbro.model.detail.DetailData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DetailDataUseCase @Inject constructor(
    private val detailRepository: DetailRepository,
) {
    operator fun invoke(animeId: Int): Flow<Result<DetailData>> = flow {
        val info = detailRepository.getDetailInfo(animeId).getOrThrow()
        val actor = detailRepository.getDetailActor(animeId).getOrThrow()
        val series = detailRepository.getDetailSeries(animeId).getOrThrow()
        val recommendation = detailRepository.getDetailRecommendation(animeId).getOrThrow()
        val myReview = detailRepository.getMyReview(animeId).getOrThrow()

        emit(
            DetailData(
                info = info,
                actor = actor,
                series = series,
                recommendation = recommendation,
                myReview = myReview
            )
        )
    }.asResult()
}