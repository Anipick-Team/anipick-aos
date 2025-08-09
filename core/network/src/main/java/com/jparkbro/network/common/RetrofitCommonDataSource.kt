package com.jparkbro.network.common

import com.jparkbro.model.auth.AuthToken
import com.jparkbro.model.common.MetaData
import com.jparkbro.network.util.toResult
import javax.inject.Inject

class RetrofitCommonDataSource @Inject constructor(
    private val commonApi: CommonApi
) : CommonDataSource {
    companion object {
        private const val TAG = "RetrofitCommonDataSource"
    }

    override suspend fun getMetaData(): Result<MetaData> {
        return commonApi.getMetaData().toResult(TAG, "getMetaData")
    }

    override suspend fun requestToken(refreshToken: String): Result<AuthToken> {
        return commonApi.requestToken("Bearer $refreshToken").toResult(TAG, "requestToken")
    }
}