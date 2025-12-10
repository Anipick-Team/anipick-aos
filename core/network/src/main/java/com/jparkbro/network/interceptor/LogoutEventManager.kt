package com.jparkbro.network.interceptor

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutEventManager @Inject constructor() {
    private val _logoutEvent = MutableSharedFlow<Unit>(replay = 0)
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    suspend fun emitLogoutEvent() {
        _logoutEvent.emit(Unit)
    }
}
