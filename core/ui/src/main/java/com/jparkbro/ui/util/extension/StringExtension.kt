package com.jparkbro.ui.util.extension

import com.jparkbro.ui.R
import com.jparkbro.ui.util.EmailValidator
import com.jparkbro.ui.util.UiText

fun String.isEmailValid(): Boolean {
    return EmailValidator.validate(this)
}

fun String.getEmailErrorMessage(): UiText? {
    return when {
        this.isEmpty() -> null
        this.length > 50 -> UiText.StringResource(R.string.error_email_too_long)
        !this.isEmailValid() -> UiText.StringResource(R.string.error_email_invalid_format)
        else -> null
    }
}


/**
 * 한글 문자를 필터링하는 함수
 * @return 한글이 제거된 텍스트
 */
fun String.filterKorean(): String {
    return this.filter { char ->
        Character.UnicodeBlock.of(char) != Character.UnicodeBlock.HANGUL_SYLLABLES &&
                Character.UnicodeBlock.of(char) != Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
    }
}

fun String.quarterStringToInt(): Int? {
    return when (this) {
        "1분기" -> 1
        "2분기" -> 2
        "3분기" -> 3
        "4분기" -> 4
        else -> null
    }
}

/**
 * URL에 default.jpg가 포함되어 있으면 기본 이미지 리소스 ID를 반환하고,
 * 그렇지 않으면 원본 URL을 반환합니다.
 * @return URL 문자열 또는 drawable 리소스 ID
 */
fun String.toImageModel(): Any {
    return if (this.contains("default.jpg")) {
        R.drawable.thumbnail_img
    } else {
        this
    }
}