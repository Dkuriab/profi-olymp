package com.santa.olymp.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.BeanDescription

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GroupDTO(
    val id: Long? = null,
    val name: String,
    val description: String,
)
