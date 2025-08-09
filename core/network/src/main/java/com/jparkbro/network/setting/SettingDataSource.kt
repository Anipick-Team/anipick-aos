package com.jparkbro.network.setting

import com.jparkbro.model.setting.UpdateEmail
import com.jparkbro.model.setting.UpdateNickname
import com.jparkbro.model.setting.UpdatePassword
import com.jparkbro.model.setting.UserInfo

interface SettingDataSource {
    suspend fun getUserInfo(): Result<UserInfo>

    suspend fun editNickname(request: UpdateNickname): Result<Unit>
    suspend fun editEmail(request: UpdateEmail): Result<Unit>
    suspend fun editPassword(request: UpdatePassword): Result<Unit>
    suspend fun userWithdrawal(): Result<Unit>
}