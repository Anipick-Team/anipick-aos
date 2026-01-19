package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.Cast

data class GetInfoCharactersResult(
    val cursor: Cursor? = null,
    val casts: List<Cast> = emptyList()
)
