package com.jparkbro.domain

import android.util.Log
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
    companion object {
        private const val TAG = "DetailDataUseCase"
    }

    operator fun invoke(animeId: Int): Flow<Result<DetailData>> = flow {
        val info = try {
            detailRepository.getDetailInfo(animeId).getOrThrow()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get detail info for animeId: $animeId", e)
            throw e
        }
        val actor = try {
            detailRepository.getDetailActor(animeId).getOrThrow()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get detail actor for animeId: $animeId", e)
            throw e
        }
        val series = try {
            detailRepository.getDetailSeries(animeId).getOrThrow()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get detail series for animeId: $animeId", e)
            throw e
        }
        val recommendation = try {
            detailRepository.getDetailRecommendation(animeId).getOrThrow()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get detail recommendation for animeId: $animeId", e)
            throw e
        }
        val myReview = try {
            detailRepository.getMyReview(animeId).getOrThrow()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get my review for animeId: $animeId", e)
            throw e
        }

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