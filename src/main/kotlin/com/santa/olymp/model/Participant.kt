package com.santa.olymp.model

data class Participant(
        val id: Long,
        val name: String,
        val wish: String,
        var recipient: Participant?,
)
