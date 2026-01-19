package com.jparkbro.ranking.util

internal fun sanitizeYear(year: String?): String? {
    return when {
        year == null || year == "전체년도" || year.isBlank() -> null
        else -> year
    }
}

internal fun sanitizeSeason(year: String?, season: String?): String? {
    return when {
        year == null || year == "전체년도" -> null
        season == null || season == "전체분기" || season.isBlank() -> null
        else -> season
    }
}