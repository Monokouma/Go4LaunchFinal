package com.despaircorp.atoms.coworker

import javax.inject.Inject

class CoworkerRowMapper @Inject constructor() {
    fun map(imageUrl: String?, sentence: String, onClick: () -> Unit) = CoworkerRowViewState(
        imageUrl = imageUrl,
        sentence = sentence,
        onClick = onClick,
    )
}