package com.santa.olymp.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Group(
        val groupId: Long? = null,
        val name: String,
        val description: String,
        val participants: MutableList<Participant>? = null,
)