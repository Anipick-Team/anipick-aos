package com.jparkbro.domain

import android.util.Log
import com.jparkbro.data.detail.DetailRepository
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
        try {
            detailRepository.getDetailInfo(animeId).fold(
                onSuccess = { info ->
                    detailRepository.getDetailActor(animeId).fold(
                        onSuccess = { actor ->
                            detailRepository.getDetailSeries(animeId).fold(
                                onSuccess = { series ->
                                    detailRepository.getDetailRecommendation(animeId).fold(
                                        onSuccess = { recommendation ->
                                            detailRepository.getMyReview(animeId).fold(
                                                onSuccess = { myReview ->
                                                    emit(Result.success(
                                                        DetailData(
                                                            info = info,
                                                            actor = actor,
                                                            series = series,
                                                            recommendation = recommendation,
                                                            myReview = myReview
                                                        )
                                                    ))
                                                },
                                                onFailure = { exception ->
                                                    Log.e(TAG, "Failed to get my review for animeId: $animeId", exception)
                                                    emit(Result.failure(exception))
                                                }
                                            )
                                        },
                                        onFailure = { exception ->
                                            Log.e(TAG, "Failed to get detail recommendation for animeId: $animeId", exception)
                                            emit(Result.failure(exception))
                                        }
                                    )
                                },
                                onFailure = { exception ->
                                    Log.e(TAG, "Failed to get detail series for animeId: $animeId", exception)
                                    emit(Result.failure(exception))
                                }
                            )
                        },
                        onFailure = { exception ->
                            Log.e(TAG, "Failed to get detail actor for animeId: $animeId", exception)
                            emit(Result.failure(exception))
                        }
                    )
                },
                onFailure = { exception ->
                    Log.e(TAG, "Failed to get detail info for animeId: $animeId", exception)
                    emit(Result.failure(exception))
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error for animeId: $animeId", e)
            emit(Result.failure(e))
        }
    }
}