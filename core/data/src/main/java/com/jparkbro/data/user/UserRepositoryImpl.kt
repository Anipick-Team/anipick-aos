package com.jparkbro.data.user

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.jparkbro.data.util.image.ImageCompressor
import com.jparkbro.model.dto.mypage.main.GetUserInfoResult
import com.jparkbro.model.dto.mypage.main.UpdateProfileImageRequest
import com.jparkbro.model.dto.mypage.main.toResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.network.user.UserDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val imageCompressor: ImageCompressor,
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
                    val result = response.toResult()
                    // URL로 이미지도 같이 로드
                    val imageBytes = result.profileImageUrl?.let { url ->
                        userDataSource.getUserProfileImage(url).getOrNull()
                    }
                    userInfoCache.update { result.copy(profileImageBytes = imageBytes) }
                    Result.success(Unit)
                },
                onFailure = { Result.failure(it) }
            )
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

    override fun clearCache() {
        userInfoCache.update { null }
    }
}
