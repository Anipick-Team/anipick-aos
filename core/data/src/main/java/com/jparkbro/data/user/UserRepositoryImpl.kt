package com.jparkbro.data.user

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.auth.AuthRepository
import com.jparkbro.data.common.CommonRepository
import com.jparkbro.data.search.SearchRepository
import com.jparkbro.data.util.image.ImageCompressor
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.dto.mypage.main.GetUserInfoResult
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageRequest
import com.jparkbro.model.dto.mypage.main.toResult
import com.jparkbro.model.dto.mypage.setting.UserResponse
import com.jparkbro.model.dto.mypage.setting.UserResult
import com.jparkbro.model.dto.mypage.setting.toResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.model.dto.mypage.useredit.UpdateEmailRequest
import com.jparkbro.model.dto.mypage.useredit.UpdateNicknameRequest
import com.jparkbro.model.dto.mypage.useredit.UpdatePasswordRequest
import com.jparkbro.network.user.UserDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val imageCompressor: ImageCompressor,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val commonRepository: CommonRepository,
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository,
    @param:ApplicationContext private val context: Context,
) : UserRepository {

    /** MyPage Info - 캐시 기반 사용자 정보 */
    private val userInfoCache = MutableStateFlow<GetUserInfoResult?>(null)
    override fun getUserInfo(): Flow<GetUserInfoResult?> {
        return userInfoCache
    }
    override suspend fun loadUserInfo(): Result<Unit> {
        if (userInfoCache.value != null) {
            return Result.success(Unit)
        }
        return refreshUserInfo()
    }
    override suspend fun refreshUserInfo(): Result<Unit> {
        return userDataSource.getUserInfo()
            .fold(
                onSuccess = { response ->
                    userInfoCache.update { response.toResult() }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
    }

    override suspend fun loadProfileImage(): Result<Unit> {
        val imageBytes = userInfoCache.value?.profileImageUrl?.let { url ->
            userDataSource.getUserProfileImage(url).getOrNull()
        }
        userInfoCache.update {
            it?.copy(profileImageBytes = imageBytes)
        }
        return Result.success(Unit)
    }

    override suspend fun updateProfileImage(contentUri: Uri): Result<Unit> {
        val mimeType = context.contentResolver.getType(contentUri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)

        val compressedImage = imageCompressor.compressImage(
            contentUri = contentUri,
            compressionThreshold = 200 * 1024L,
        )

        return userDataSource.updateProfileImage(
            request = UpdateProfileImageRequest(
                imageData = compressedImage,
                mimeType = mimeType,
                filename = "profile_image.$extension"
            )
        ).fold(
            onSuccess = {
                refreshUserInfo()
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )
    }

    /** User Content - 캐시만 관리 */
    override val userContentCache = MutableStateFlow(GetUserContentResult())
    override fun getUserContent(): Flow<GetUserContentResult> = userContentCache

    /** Setting User Info */
    private val userCache = MutableStateFlow<UserResult?>(null)
    override fun getUser(): Flow<UserResult?> {
        return userCache
    }
    override suspend fun loadUser(): Result<Unit> {
        return userDataSource.loadUser()
            .onSuccess { response -> userCache.update { response.toResult() } }
            .map { Unit }
    }

    override suspend fun updateNickname(request: UpdateNicknameRequest): Result<Unit> {
        return userDataSource.updateNickname(request)
            .onSuccess {
                val userId = userPreferenceRepository.getUserId().getOrNull()
                userId?.let {
                    userPreferenceRepository.saveUserInfo(
                        userId = it,
                        nickname = request.nickname
                    )
                }
                loadUser()
                refreshUserInfo()
            }.map { Unit }
    }

    override suspend fun updateEmail(request: UpdateEmailRequest): Result<Unit> {
        return userDataSource.updateEmail(request)
            .onSuccess {
                refreshUserInfo()
                loadUser()
            }.map { Unit }
    }

    override suspend fun updatePassword(request: UpdatePasswordRequest): Result<Unit> {
        return userDataSource.updatePassword(request)
            .onSuccess { refreshUserInfo() }.map { Unit }
    }

    override suspend fun userWithdrawal(): Result<Unit> {
        return userDataSource.userWithdrawal()
            .onSuccess {
                userPreferenceRepository.clearAllData()
                commonRepository.clearRecentAnime()
                searchRepository.deleteAll()
                clearCache()
                if (userCache.value?.provider == LoginProvider.KAKAO) {
                    authRepository.kakaoUnlink()
                        .onSuccess { Unit }
                }
            }.map { Unit }
    }

    override suspend fun userLogout(): Result<Unit> {
        return userPreferenceRepository.clearAllData()
            .onSuccess {
                clearCache()
                if (userCache.value?.provider == LoginProvider.KAKAO) {
                    authRepository.kakaoUnlink()
                        .onSuccess { Unit }
                }
            }.map { Unit }
    }

    override fun clearCache() {
        userInfoCache.update { null }
    }
}
