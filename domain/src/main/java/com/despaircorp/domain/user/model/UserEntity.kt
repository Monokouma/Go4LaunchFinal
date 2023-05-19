package com.despaircorp.domain.user.model

data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?,
    val isEating: Boolean
)
