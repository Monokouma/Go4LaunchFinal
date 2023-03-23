package com.despaircorp.ui.bottom_navigation

import androidx.lifecycle.ViewModel
import com.despaircorp.domain.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {
}