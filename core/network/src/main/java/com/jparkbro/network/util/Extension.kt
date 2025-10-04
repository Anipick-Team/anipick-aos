package com.jparkbro.network.util

import android.util.Log
import com.jparkbro.model.exception.ApiException
import com.jparkbro.network.model.ApiResponse
import retrofit2.HttpException
import retrofit2.Response

fun <T> Response<ApiResponse<T>>.toResult(tag: String, methodName: String): Result<T> {
    Log.d(tag, "$methodName() called")
    Log.d(tag, "$methodName() response - isSuccessful: $isSuccessful, code: ${code()}")

    return try {
        val apiResponse = body()
        Log.d(tag, "$methodName() apiResponse - result: ${apiResponse?.result}")

        when {
            !isSuccessful -> {
                Log.e(tag, "$methodName() HTTP error - code: ${code()}, message: ${message()}")
                Result.failure(HttpException(this))
            }

            apiResponse?.value == "success" && apiResponse.result != null -> {
                Log.d(tag, "$methodName() success")
                Result.success(apiResponse.result)
            }

            else -> {
                Log.e(tag, "$methodName() API error - $apiResponse")
                Result.failure(ApiException(apiResponse?.code ?: 0, apiResponse?.errorValue ?: ""))
            }
        }
    } catch (e: Exception) {
        Log.e(tag, "$methodName() exception", e)
        Result.failure(e)
    }
}

fun Response<ApiResponse<Unit>>.toUnitResult(tag: String, methodName: String): Result<Unit> {
    Log.d(tag, "$methodName() called")
    Log.d(tag, "$methodName() response - isSuccessful: $isSuccessful, code: ${code()}")

    return try {
        val apiResponse = body()
        Log.d(tag, "$methodName() apiResponse - value: ${apiResponse?.value}")

        when {
            !isSuccessful -> {
                Log.e(tag, "$methodName() HTTP error - code: ${code()}, message: ${message()}")
                Result.failure(HttpException(this))
            }

            apiResponse?.value == "success" -> {
                Log.d(tag, "$methodName() success")
                Result.success(Unit)
            }

            else -> {
                Log.e(tag, "$methodName() API error - code: ${apiResponse?.code}")
                Result.failure(ApiException(apiResponse?.code ?: 0, apiResponse?.errorValue ?: ""))
            }
        }
    } catch (e: Exception) {
        Log.e(tag, "$methodName() exception", e)
        Result.failure(e)
    }
}