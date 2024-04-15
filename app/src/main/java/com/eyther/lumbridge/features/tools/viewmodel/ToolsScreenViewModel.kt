package com.eyther.lumbridge.features.tools.viewmodel

import androidx.lifecycle.ViewModel
import com.eyther.lumbridge.features.tools.viewmodel.ToolsScreenViewModelInterface.ToolScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ToolsScreenViewModel @Inject constructor() : ViewModel(), ToolsScreenViewModelInterface {
	
	override val viewState = MutableStateFlow<ToolScreenViewState>(ToolScreenViewState.Initial)
	
	init {
		viewState.update {
			ToolScreenViewState.Content(listOf("item1", "item2", "item3"))
		}
	}
}
