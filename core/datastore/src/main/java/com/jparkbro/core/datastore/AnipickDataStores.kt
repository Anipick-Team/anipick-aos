package com.jparkbro.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * v1부터 쓰던 DataStore 파일. 이름과 키를 바꾸면 업데이트한 사용자의 로그인·사용자 정보·최근 본 애니가 사라진다.
 * 같은 파일에 delegate가 둘 이상 생기면 런타임에 터지므로 여기에서만 선언한다.
 */
internal val Context.anipickDataStore: DataStore<Preferences> by preferencesDataStore(name = "anipick_preferences")

/** 암호화한 토큰 전용 파일 - 백업 대상에서 뺀다(`backup_rules.xml`, `data_extraction_rules.xml`). */
internal val Context.jwtTokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "jwt_tokens_secure")
