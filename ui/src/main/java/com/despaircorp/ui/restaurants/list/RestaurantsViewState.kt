package com.despaircorp.ui.restaurants.list

data class RestaurantsViewState(
    val restaurants: List<RestaurantsViewStateItems>,
    val isSpinnerVisible: Boolean,
)