package com.jparkbro.model.enum

enum class HomeDetailSortType(val param: String, val displayName: String) {
    LATEST("latest", "최신순"),
    POPULARITY("popularity", "인기순"),
    START_DATE("startDate", "방영 예정 순"),
}

enum class ExpireSortType(val param: String, val displayName: String) {
    POPULARITY("popularity", "인기순"),
    RATING("rating", "평점순"),
}

enum class ReviewSortType(val param: String, val displayName: String) {
    LATEST("latest", "최신순"),
    LIKES("likes", "좋아요순"),
    RATING_DESC("ratingDesc", "평점 높은 순"),
    RATING_ASC("ratingAsc", "평점 낮은 순"),
}