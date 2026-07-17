package com.jparkbro.model.exception

data class NetworkException(
    override val message: String = "네트워크 연결을 확인해주세요"
) : Exception(message)
