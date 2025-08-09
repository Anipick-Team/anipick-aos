package com.jparkbro.model.setting

data class UpdateUserRequest(
    val nickname: String? = null,
    val email: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null,
    val newPasswordConfirm: String? = null
)
