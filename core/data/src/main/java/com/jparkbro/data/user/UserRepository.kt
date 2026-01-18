package com.jparkbro.data.user

import android.net.Uri
import com.jparkbro.model.dto.mypage.main.GetUserInfoResult
import com.jparkbro.model.dto.mypage.setting.UserResult
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.model.dto.mypage.useredit.UpdateEmailRequest
import com.jparkbro.model.dto.mypage.useredit.UpdateNicknameRequest
import com.jparkbro.model.dto.mypage.useredit.UpdatePasswordRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    /** MyPage Info - 마이페이지 화면용 (캐시 기반) */
    fun getUserInfo(): Flow<GetUserInfoResult?>
    suspend fun loadUserInfo(): Result<Unit>
    suspend fun refreshUserInfo(): Result<Unit>

    /** Profile Image */
    suspend fun loadProfileImage(): Result<Unit>
    suspend fun updateProfileImage(contentUri: Uri): Result<Unit>

    /** User Content - 캐시만 관리 */
    val userContentCache: MutableStateFlow<GetUserContentResult>
    fun getUserContent(): Flow<GetUserContentResult>

    /** Setting User */
    fun getUser(): Flow<UserResult?>
    suspend fun loadUser(): Result<Unit>

    suspend fun updateNickname(request: UpdateNicknameRequest): Result<Unit>
    suspend fun updateEmail(request: UpdateEmailRequest): Result<Unit>
    suspend fun updatePassword(request: UpdatePasswordRequest): Result<Unit>
    suspend fun userWithdrawal(): Result<Unit>
    suspend fun userLogout(): Result<Unit>

    /** Cache Clear (로그아웃, 회원탈퇴) */
    fun clearCache()
}
