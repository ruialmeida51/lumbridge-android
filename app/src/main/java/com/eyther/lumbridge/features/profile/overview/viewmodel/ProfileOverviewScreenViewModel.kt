package com.eyther.lumbridge.features.profile.overview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.eyther.lumbridge.features.profile.overview.model.ProfileOverviewScreenViewState
import com.eyther.lumbridge.features.profile.navigation.ProfileNavigationItem
import com.eyther.lumbridge.ui.navigation.NavigationItem
import com.eyther.lumbridge.usecase.user.profile.GetUserProfileStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileOverviewScreenViewModel @Inject constructor(
    private val getUserProfileStream: GetUserProfileStream
) : ViewModel(), ProfileOverviewScreenViewModelInterface {

    override val viewState = MutableStateFlow<ProfileOverviewScreenViewState>(
        ProfileOverviewScreenViewState.Loading
    )

    init {
        observeChanges()
    }

    private fun observeChanges() {
        getUserProfileStream().onEach { user ->
            viewState.update {
                ProfileOverviewScreenViewState.Content(
                    username = user?.name,
                    email = user?.email,
                    locale = user?.locale
                )
            }
        }
            .launchIn(viewModelScope)
    }

    override fun navigate(navItem: NavigationItem, navController: NavController) {
        navController.navigate(navItem.route)
    }
}
