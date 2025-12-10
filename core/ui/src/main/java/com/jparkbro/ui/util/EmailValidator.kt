package com.jparkbro.ui.util

object EmailValidator {
    /**
     * 이메일 유효성 검사
     * 조건: ~50자리, 이메일 형식
     * @param email 검사할 이메일
     * @return 유효성 검사 결과
     */
    fun validate(email: String): Boolean {
        // 이메일이 비어있는지 체크
        if (email.isBlank()) return false

        // 이메일 길이 체크 (최대 50자)
        if (email.length > 50) return false

        // @ 기호가 정확히 1개만 있는지 체크
        if (email.count { it == '@' } != 1) return false

        // 연속된 점(..) 체크
        if (email.contains("..")) return false

        // 점이 시작이나 끝에 있는지 체크
        if (email.startsWith(".") || email.endsWith(".")) return false

        // @ 앞뒤로 점이 있는지 체크
        if (email.contains(".@") || email.contains("@.")) return false

        // 이메일 형식 검증을 위한 정규식
        // 로컬 파트: 영문, 숫자, 특수문자(._-+%)만 허용
        // 도메인: 영문, 숫자, 하이픈만 허용 (하이픈은 중간에만)
        val emailPattern = Regex(
            "^[a-zA-Z0-9._+%-]+" +                      // 로컬 파트 (특수문자 제한)
                    "@" +                               // @ 기호
                    "[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?" + // 도메인 (하이픈은 중간에만)
                    "(\\.[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?)+$" // 서브도메인 (최소 1개)
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