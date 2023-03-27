package com.despaircorp.ui.restaurants

data class RestaurantsViewState(
    val restaurants: List<RestaurantsViewStateItems>,
    val isSpinnerVisible: Boolean,
)