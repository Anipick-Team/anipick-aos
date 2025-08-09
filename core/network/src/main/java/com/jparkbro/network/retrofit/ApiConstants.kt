package com.jparkbro.network.retrofit

object ApiConstants {
    const val BASE_URL = "http://anipick.p-e.kr:8080/api/"

    /** GET animes/meta-data-group */
    const val GET_META_DATA = "animes/meta-data-group"
    /** POST tokens/refresh */
    const val TOKEN_REFRESH = "tokens/refresh"

    /** POST oauth/{provider}/callback */
    const val SOCIAL_LOGIN = "oauth/{provider}/callback"
    /** POST users/signup */
    const val EMAIL_SIGNUP = "users/signup"
    /** POST users/login */
    const val EMAIL_LOGIN = "users/login"
    /** POST users/logout */
    const val LOGOUT = "users/logout"

    /** POST auth/email/send */
    const val REQUEST_CODE = "auth/email/send"
    /** POST auth/email/verify */
    const val VERIFY_CODE = "auth/email/verify"
    /** PATCH auth/password/reset */
    const val RESET_PASSWORD = "auth/password/reset"

    /** GET */
    const val SEARCH_ANIMES = "explore-search"
    /** POST */
    const val REVIEWS_BULK = "reviews/bulk"

    /* Home */
    const val TREND_ITEMS = "home/animes/trending"
    const val RECOMMENDATION_ANIMES = "home/recommendation/animes"
    const val RECOMMENDATION_ANIMES_RECENT = "home/recommendation/animes/{animeId}/recent"
    const val REVIEW_RECENT = "home/reviews/recent"
    const val UPCOMING_SEASON = "animes/upcoming-season"
    const val COMING_SOON = "home/animes/coming-soon"

    /* Home Detail */
    const val HOME_DETAIL_RECOMMENDATION_ANIMES = "recommendation/animes"
    const val HOME_DETAIL_RECOMMENDATION_ANIMES_RECENT = "recommendation/animes/{animeId}/recent"
    const val HOME_DETAIL_REVIEWS = "reviews/recent"
    const val HOME_DETAIL_COMING_SOON = "animes/coming-soon"

    /* Explore */
    /** GET explore/animes */
    const val EXPLORE_ANIMES = "explore/animes"

    /* Ranking */
    /** rankings/real-time */
    const val RANK_REAL_TIME = "rankings/real-time"
    /** rankings/year-season */
    const val RANK_YEAR_SEASON = "rankings/year-season"
    /** rankings/all-time */
    const val RANK_ALL_TIME = "rankings/all-time"

    /* MyPage */
    const val MY_PAGE = "mypage"
    const val RATED = "mypage/animes/rated"
    const val WATCH_LIST = "mypage/animes/watchlist"
    const val WATCHING = "mypage/animes/watching"
    const val FINISHED = "mypage/animes/finished"
    const val LIKE_ANIMES = "mypage/animes/like"
    const val LIKE_PERSONS = "mypage/persons/like"
    const val EDIT_PROFILE_IMG = "mypage/profile-image"

    /* Setting */
    const val GET_USER_INFO = "setting/view"
    const val EDIT_NICKNAME = "setting/nickname"
    const val EDIT_EMAIL = "setting/email"
    const val EDIT_PASSWORD = "setting/password"
    const val USER_WITHDRAWAL = "setting/withdrawal"

    /* Search */
    const val GET_POPULAR_ANIMES = "search/init"
    const val GET_SEARCH_ANIMES = "search/animes"
    const val GET_SEARCH_PERSONS = "search/persons"
    const val GET_SEARCH_STUDIOS = "search/studios"

    /* Detail */
    const val GET_DETAIL_INFO = "animes/{animeId}/detail/info"
    const val GET_DETAIL_ACTOR = "animes/{animeId}/detail/actor"
    const val GET_DETAIL_SERIES = "animes/{animeId}/detail/series"
    const val GET_DETAIL_RECOMMENDATION = "animes/{animeId}/detail/recommendation"
    const val GET_DETAIL_REVIEWS = "animes/{animeId}/reviews"
    const val GET_DETAIL_MY_REVIEW = "animes/{animeId}/my-review"

    const val SET_LIKE_ANIME = "animes/{animeId}/like"
    const val SET_WATCH_STATUS = "users/{animeId}/status"

    const val CREATE_ANIME_RATING = "rating/{animeId}/animes"
    const val UPDATE_ANIME_RATING = "rating/{reviewId}/animes"
    const val DELETE_ANIME_RATING = "rating/{reviewId}/animes"

    /* Review */
    const val GET_MY_REVIEW = "reviews/{animeId}/my-review"
    const val EDIT_MY_REVEIW = "reviews/{animeId}/animes"

    const val LIKED_REVIEW = "reviews/{reviewId}/like"
    const val UN_LIKED_REVIEW = "reviews/{reviewId}/like"

    const val DELETE_REVIEW = "reviews/{reviewId}"
    const val REPORT_REVIEW = "reviews/{reviewId}/report"
    const val BLOCK_USER = "{userId}/block-user"
}