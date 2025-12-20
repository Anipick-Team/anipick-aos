package com.jparkbro.model.enum

enum class HomeDetailType(val title: String) {
    RECOMMENDS("추천 애니메이션"),
    LATEST_REVIEWS("최근 리뷰"),
    SIMILAR_TO_WATCHED("추천 애니메이션"),
    UPCOMING_RELEASE("공개 예정")
}