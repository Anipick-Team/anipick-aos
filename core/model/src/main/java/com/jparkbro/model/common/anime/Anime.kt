package com.jparkbro.model.common.anime

data class Anime(
    val animeId: Long? = null,
    val title: String? = null,
    val titleKor: String? = null,
    val titleEng: String? = null,
    val coverImageUrl: String? = null,
    val genres: List<String?> = emptyList(),
    val rank: Int? = null,
    val releaseDate: String? = null,
    val clickLog: String? = null,
    val impressionLog: String? = null,
    val isAdult: Boolean = false,
    val change: String? = null,
    val trend: String? = null,
    val popularity: Long? = null,
    val trending: Int? = null,
    val airDate: String? = null,
    val userAnimeStatusId: Int? = null,
    val myRating: Float? = null,
    val animeLikeId: Int? = null,
)