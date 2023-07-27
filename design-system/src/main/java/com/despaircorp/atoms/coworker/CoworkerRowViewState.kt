package com.despaircorp.atoms.coworker

data class CoworkerRowViewState(
    val imageUrl: String?,
    val sentence: String,
    val onClick: () -> Unit,
)