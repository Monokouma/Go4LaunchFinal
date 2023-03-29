package com.despaircorp.domain.authentication.model

data class AuthenticatedUser(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?,
)