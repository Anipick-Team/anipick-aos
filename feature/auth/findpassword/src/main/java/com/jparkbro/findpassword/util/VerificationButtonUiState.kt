package com.jparkbro.findpassword.util

import java.util.Locale

// 인증번호 받기 버튼 상태
sealed interface RequestCodeButtonUiState {
    // 초기 상태: "인증번호 받기" 텍스트, 활성화
    data object Initial : RequestCodeButtonUiState

    // API 요청 중: "전송 중..." 비활성화
    data object Loading : RequestCodeButtonUiState

    // 카운트다운 중 상태: "전송됨 00:30" 비활성화, 타이머
    data class Counting(val remainingSeconds: Int) : RequestCodeButtonUiState {
        fun formatTime(): String {
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
        }
    }

    // 재발송 가능 상태: "재발송하기" 텍스트, 활성화
    data object Ready : RequestCodeButtonUiState
}

// 인증하기 버튼 상태
sealed interface VerifyButtonUiState {
    // 초기 상태: "인증하기" 비활성화
    data object Inactive : VerifyButtonUiState

    // 활성화 상태: "인증하기 3:00" 활성화, 타이머
    data class Counting(val remainingSeconds: Int) : VerifyButtonUiState {
        fun formatTime(): String {
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
        }
    }

    // 인증 성공: "인증완료" 비활성화
    data object Success : VerifyButtonUiState

    // 시간 만료: "인증하기" 비활성화
    data object Expired : VerifyButtonUiState
}