package com.jparkbro.ui.util

object EmailValidator {
    /**
     * 이메일 유효성 검사
     * 조건: ~50자리, 이메일 형식
     * @param email 검사할 이메일
     * @return 유효성 검사 결과
     */
    fun validate(email: String): Boolean {
        // 이메일 길이 체크 (최대 50자)
        if (email.length > 50) return false

        // 이메일이 비어있는지 체크
        if (email.isBlank()) return false

        // 이메일 형식 검증을 위한 정규식
        val emailPattern = Regex(
            "[a-zA-Z0-9+._%\\-]{1,256}" +               // 로컬 파트
                    "@" +                               // @ 기호
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + // 도메인 이름
                    "(" +
                    "\\." +                             // 점
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + // 서브도메인
                    ")+"
        )

        return emailPattern.matches(email)
    }

    /**
     * 이메일 유효성 검사 결과와 오류 메시지를 반환
     * @param email 검사할 이메일
     * @return 이메일이 유효하면 null, 그렇지 않으면 오류 메시지
     */
    fun getErrorMessage(email: String): String? {
        return when {
            email.isBlank() -> "이메일을 입력해주세요."
            email.length > 50 -> "이메일은 최대 50자까지 입력 가능합니다."
            !validate(email) -> "올바른 이메일 형식이 아닙니다."
            else -> null
        }
    }
}