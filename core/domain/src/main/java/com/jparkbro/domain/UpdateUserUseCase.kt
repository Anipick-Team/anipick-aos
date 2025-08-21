package com.jparkbro.domain

import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.common.CommonRepository
import com.jparkbro.data.search.SearchRepository
import com.jparkbro.data.setting.SettingRepository
import com.jparkbro.model.common.Result
import com.jparkbro.model.common.asResult
import com.jparkbro.model.setting.ProfileEditType
import com.jparkbro.model.setting.UpdateEmail
import com.jparkbro.model.setting.UpdateNickname
import com.jparkbro.model.setting.UpdatePassword
import com.jparkbro.model.setting.UpdateUserRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val settingRepository: SettingRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val commonRepository: CommonRepository,
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(type: ProfileEditType, request: UpdateUserRequest): Flow<Result<Unit>> = flow {
        when (type) {
            ProfileEditType.NICKNAME -> {
                // 1. api
                settingRepository.editNickname(UpdateNickname(nickname = request.nickname.toString())).getOrThrow()
                // 2. datastore
                val userId = userPreferenceRepository.getUserId().getOrNull()
                if (userId != null) {
                    userPreferenceRepository.saveUserInfo(
                        userId = userId,
                        nickname = request.nickname.toString()
                    )
                }
            }
            ProfileEditType.PASSWORD -> {
                settingRepository.editPassword(UpdatePassword(
                    currentPassword = request.currentPassword.toString(),
                    newPassword = request.newPassword.toString(),
                    confirmNewPassword = request.newPasswordConfirm.toString()
                )).getOrThrow()
            }
            ProfileEditType.EMAIL -> {
                settingRepository.editEmail(UpdateEmail(
                    newEmail = request.email.toString(),
                    password = request.currentPassword.toString()
                )).getOrThrow()
            }
            ProfileEditType.WITHDRAWAL -> {
                // 1. 회원탈퇴 Api
                settingRepository.userWithdrawal().getOrThrow()

                // 2. Data Store 정보 삭제
                // 2-1. 토큰, 닉네임, 아이디
                userPreferenceRepository.clearAllData().getOrThrow()

                // 2-2. 최근 애니
                commonRepository.clearRecentAnime().getOrThrow()

                // 2-3. 검색 내역
                searchRepository.deleteAll().getOrThrow()
            }
        }

        emit(Unit)
    }.asResult()
}