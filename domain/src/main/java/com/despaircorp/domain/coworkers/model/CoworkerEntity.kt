package com.despaircorp.domain.coworkers.model

data class CoworkerEntity(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?,
    val eating: Boolean,
    val hadNotificationOn: Boolean
)