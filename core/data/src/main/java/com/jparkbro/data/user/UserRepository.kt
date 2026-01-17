package com.jparkbro.data.user

import android.net.Uri
import com.jparkbro.model.dto.mypage.main.GetUserInfoResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    /** MyPage Info - 마이페이지 화면용 (캐시 기반) */
    fun getUserInfo(): Flow<GetUserInfoResult?>
    suspend fun loadUserInfo(): Result<Unit>
    suspend fun refreshUserInfo(): Result<Unit>

    /** Profile Image - 프로필 이미지 업데이트 */
    suspend fun updateProfileImage(contentUri: Uri): Result<Unit>

    /** User Content - 캐시만 관리 */
    val userContentCache: MutableStateFlow<GetUserContentResult>
    fun getUserContent(): Flow<GetUserContentResult>

    /** Cache Clear (로그아웃, 회원탈퇴) */
    fun clearCache()
}
