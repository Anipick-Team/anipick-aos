package com.jparkbro.network.setting

import com.jparkbro.model.setting.UpdateEmail
import com.jparkbro.model.setting.UpdateNickname
import com.jparkbro.model.setting.UpdatePassword
import com.jparkbro.model.setting.UserInfo
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import javax.inject.Inject

class RetrofitSettingDataSource @Inject constructor(
    private val settingApi: SettingApi
) : SettingDataSource {
    companion object {
        const val TAG = "RetrofitSettingDataSource"
    }

    override suspend fun getUserInfo(): Result<UserInfo> {
        return settingApi.getUserInfo().toResult(TAG, "getUserInfo")
    }

    override suspend fun editNickname(request: UpdateNickname): Result<Unit> {
        return settingApi.editNickname(request).toUnitResult(TAG, "editNickname")
    }

    override suspend fun editEmail(request: UpdateEmail): Result<Unit> {
        return settingApi.editEmail(request).toUnitResult(TAG, "editEmail")
    }

    override suspend fun editPassword(request: UpdatePassword): Result<Unit> {
        return settingApi.editPassword(request).toUnitResult(TAG, "editPassword")
    }

    override suspend fun userWithdrawal(): Result<Unit> {
        return settingApi.userWithdrawal().toUnitResult(TAG, "userWithdrawal")
    }
}