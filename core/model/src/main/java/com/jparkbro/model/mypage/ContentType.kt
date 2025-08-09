package com.jparkbro.model.mypage

enum class ContentType(val title: String, val param: String? = null) {
    WATCHLIST("볼 애니", "WATCHLIST"),
    WATCHING("보는 중", "WATCHING"),
    FINISHED("다 본 애니", "FINISHED"),
    LIKED_ANIME("좋아요한 작품"),
    LIKED_PERSON("좋아요한 인물"),
}