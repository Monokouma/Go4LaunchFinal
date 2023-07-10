package com.despaircorp.data.user

data class UserDto(
    val name: String? = null,
    val emailAddress: String? = null,
    val uuid: String? = null,
    val picture: String? = null,
    val eating: Boolean? = null,
    val hadNotificationOn: Boolean? = null
)