package com.jparkbro.model.exception

data class ApiException(
    val errorCode: Int,
    val errorValue: String
) : Exception("API Error - code: $errorCode, value: $errorValue")
