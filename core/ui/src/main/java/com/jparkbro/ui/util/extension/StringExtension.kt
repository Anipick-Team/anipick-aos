package com.jparkbro.ui.util.extension

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