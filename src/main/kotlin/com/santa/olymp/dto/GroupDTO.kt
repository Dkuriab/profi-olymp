package com.santa.olymp.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.BeanDescription

data class GroupParamsDTO(
    val name: String? = null,
    val description: String? = null,
)
