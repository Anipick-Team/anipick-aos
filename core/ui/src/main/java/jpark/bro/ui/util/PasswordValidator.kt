package jpark.bro.ui.util

object PasswordValidator {
    /**
     * 비밀번호 유효성 검사
     * 조건: 8~16자리, 영문 대/소문자, 숫자, 특수문자 1개 이상씩 포함
     * @param password 검사할 비밀번호
     * @return 유효성 검사 결과
     */
    fun validate(password: String): Boolean {
        // 길이 검사 (8~16자리)
        if (password.length !in 8..16) return false

        // 영문자 포함 여부 (대소문자 구분 없이)
        val hasLetter = password.any { it.isLetter() }
        if (!hasLetter) return false

        // 숫자 포함 여부
        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) return false

        // 특수문자 포함 여부 (특수문자 목록은 필요에 따라 조정)
        val specialChars = "!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/"
        val hasSpecialChar = password.any { it in specialChars }
        if (!hasSpecialChar) return false

        // 모든 조건 충족
        return true
    }
}