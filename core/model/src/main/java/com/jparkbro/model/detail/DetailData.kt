package com.jparkbro.model.detail

import com.jparkbro.model.common.DefaultAnime

data class DetailData(
    val info: DetailInfo,
    val actor: List<DetailActor>,
    val series: List<DetailSeries>,
    val recommendation: List<DefaultAnime>,
    val myReview: DetailMyReview
)
