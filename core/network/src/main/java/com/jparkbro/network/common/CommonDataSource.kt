package com.jparkbro.network.common

import com.jparkbro.model.auth.AuthToken
import com.jparkbro.model.common.AppInitRequest
import com.jparkbro.model.common.AppInitResponse
import com.jparkbro.model.common.MetaData

interface CommonDataSource {
    suspend fun checkAppInit(
        userAppVersion: String
    ): Result<AppInitResponse>

    suspend fun getMetaData(
    ): Result<MetaData>

    suspend fun requestToken(
        refreshToken: String
    ): Result<AuthToken>
}