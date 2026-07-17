package com.jparkbro.network.util

import android.util.Log
import com.jparkbro.model.exception.ApiException
import com.jparkbro.model.exception.NetworkException
import com.jparkbro.network.model.ApiResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(
    tag: String,
    methodName: String,
    apiCall: suspend () -> Response<ApiResponse<T>>
): Result<T> {
    Log.d(tag, "$methodName() called")
    return try {
        val response = apiCall()
        Log.d(tag, "$methodName() response - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

        val apiResponse = response.body()
        Log.d(tag, "$methodName() apiResponse - result: ${apiResponse?.result}")

        when {
            !response.isSuccessful -> {
                Log.e(tag, "$methodName() HTTP error - code: ${response.code()}, message: ${response.message()}")
                Result.failure(HttpException(response))
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
    } catch (e: IOException) {
        Log.e(tag, "$methodName() network error", e)
        Result.failure(NetworkException())
    } catch (e: Exception) {
        Log.e(tag, "$methodName() exception", e)
        Result.failure(e)
    }
}

suspend fun safeApiCallUnit(
    tag: String,
    methodName: String,
    apiCall: suspend () -> Response<ApiResponse<Unit>>
): Result<Unit> {
    Log.d(tag, "$methodName() called")
    return try {
        val response = apiCall()
        Log.d(tag, "$methodName() response - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

        val apiResponse = response.body()
        Log.d(tag, "$methodName() apiResponse - value: ${apiResponse?.value}")

        when {
            !response.isSuccessful -> {
                Log.e(tag, "$methodName() HTTP error - code: ${response.code()}, message: ${response.message()}")
                Result.failure(HttpException(response))
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
    } catch (e: IOException) {
        Log.e(tag, "$methodName() network error", e)
        Result.failure(NetworkException())
    } catch (e: Exception) {
        Log.e(tag, "$methodName() exception", e)
        Result.failure(e)
    }
}

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