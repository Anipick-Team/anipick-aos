package com.jparkbro.data.setting

import com.jparkbro.model.setting.UpdateEmail
import com.jparkbro.model.setting.UpdateNickname
import com.jparkbro.model.setting.UpdatePassword
import com.jparkbro.model.setting.UserInfo
import com.jparkbro.network.setting.SettingDataSource
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingDataSource: SettingDataSource
) : SettingRepository {

    override suspend fun getUserInfo(): Result<UserInfo> {
        return settingDataSource.getUserInfo()
    }

    override suspend fun editNickname(request: UpdateNickname): Result<Unit> {
        return settingDataSource.editNickname(request)
    }

    override suspend fun editEmail(request: UpdateEmail): Result<Unit> {
        return settingDataSource.editEmail(request)
    }

    override suspend fun editPassword(request: UpdatePassword): Result<Unit> {
        return settingDataSource.editPassword(request)
    }

    override suspend fun userWithdrawal(): Result<Unit> {
        return settingDataSource.userWithdrawal()
    }
}